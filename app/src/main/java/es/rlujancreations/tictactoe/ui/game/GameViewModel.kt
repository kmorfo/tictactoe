package es.rlujancreations.tictactoe.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rlujancreations.tictactoe.data.network.FirebaseService
import es.rlujancreations.tictactoe.ui.model.GameModel
import es.rlujancreations.tictactoe.ui.model.PlayerModel
import es.rlujancreations.tictactoe.ui.model.PlayerType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Raúl Luján Colilla on 9/12/23.
 */
@HiltViewModel
class GameViewModel @Inject constructor(
    private val firebaseService: FirebaseService
) : ViewModel() {

    private lateinit var userId: String

    private var _game = MutableStateFlow<GameModel?>(null)
    val game: StateFlow<GameModel?> = _game

    private var _winner = MutableStateFlow<PlayerType?>(null)
    val winner: StateFlow<PlayerType?> = _winner

    fun joinToGame(gameId: String, userId: String, owner: Boolean) {
        this.userId = userId

        if (owner) {
            join(gameId)
        } else {
            joinGameLikeGuest(gameId)
        }
    }

    private fun join(gameId: String) {
        viewModelScope.launch {
            firebaseService.joinToGame(gameId).collect() {
                val result =
                    it?.copy(isGameReady = it.player2 != null, isMyTurn = isMyTurn(it.playerTurn))
                _game.value = result
                verifyWinner()
            }
        }
    }


    private fun joinGameLikeGuest(gameId: String) {
        viewModelScope.launch {
            firebaseService.joinToGame(gameId).take(1).collect() {
                var result = it
                if (result != null) {
                    result = result.copy(
                        player2 = PlayerModel(userId, PlayerType.SecondPlayer)
                    )
                    firebaseService.updateGame(result.toData())
                }
            }
            join(gameId)
        }
    }

    private fun isMyTurn(playerTurn: PlayerModel): Boolean {
        return playerTurn.userId == userId
    }

    fun onItemSelected(position: Int) {
        val currentGame = _game.value ?: return
        if (currentGame.isGameReady && currentGame.board[position] == PlayerType.Empty && isMyTurn(
                currentGame.playerTurn
            )
        ) {
            viewModelScope.launch {
                val newBoard = currentGame.board.toMutableList()
                newBoard[position] = getPlayer() ?: PlayerType.Empty
                firebaseService.updateGame(
                    currentGame.copy(board = newBoard, playerTurn = getEnemyPlayer()!!).toData()
                )
            }
        }
    }

    private fun getPlayer(): PlayerType? {
        return when {
            game.value?.player1?.userId == userId -> PlayerType.FirstPlayer
            game.value?.player2?.userId == userId -> PlayerType.SecondPlayer
            else -> null
        }
    }

    private fun getEnemyPlayer(): PlayerModel? =
        if (game.value?.player1?.userId == userId) game.value?.player2 else game.value?.player1

    private fun verifyWinner() {
        val board = game.value?.board
        if (board != null && board.size == 9) {
            when {
                isGameWon(board, PlayerType.FirstPlayer) -> {
                    _winner.value = PlayerType.FirstPlayer
                }

                isGameWon(board, PlayerType.SecondPlayer) -> {
                    _winner.value = PlayerType.SecondPlayer
                }
            }
        }
    }

    private fun isGameWon(board: List<PlayerType>, playerType: PlayerType): Boolean {
        return when {
            //Row
            (board[0] == playerType && board[1] == playerType && board[2] == playerType) -> true
            (board[3] == playerType && board[4] == playerType && board[5] == playerType) -> true
            (board[6] == playerType && board[7] == playerType && board[8] == playerType) -> true

            //Column
            (board[0] == playerType && board[3] == playerType && board[6] == playerType) -> true
            (board[1] == playerType && board[4] == playerType && board[7] == playerType) -> true
            (board[2] == playerType && board[5] == playerType && board[8] == playerType) -> true

            //Diagonal
            (board[0] == playerType && board[4] == playerType && board[8] == playerType) -> true
            (board[2] == playerType && board[4] == playerType && board[6] == playerType) -> true

            else -> false
        }
    }
}