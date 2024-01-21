package es.rlujancreations.tictactoe.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rlujancreations.tictactoe.data.network.FirebaseService
import es.rlujancreations.tictactoe.data.network.model.GameData
import es.rlujancreations.tictactoe.data.network.model.PlayerData
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * Created by Raúl Luján Colilla on 9/12/23.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseService: FirebaseService
) : ViewModel() {

    fun onCreateGame(navigateToGame: (String, String, Boolean) -> Unit) {
        val game = createNewGame()
        val gameId = firebaseService.createGame(game)
        val userId = game.player1?.userId.orEmpty()
        val owner = true
        navigateToGame(gameId, userId, owner)
    }

    fun onJoinGame(
        gameId: String,
        gameIdNotValid: () -> Unit,
        navigateToGame: (String, String, Boolean) -> Unit
    ) {
        val owner = false
        val userId = createUserId()
        viewModelScope.launch {
            firebaseService.checkIfGameExists(gameId).collect {
                if (it) navigateToGame(gameId, userId, owner)
                else gameIdNotValid()
            }
        }
    }

    private fun createUserId(): String {
        return Calendar.getInstance().timeInMillis.hashCode().toString()
    }

    private fun createNewGame(): GameData {
        val currentPlayer = PlayerData(playerType = 1)
        return GameData(
            board = List(9) { 0 },
            player1 = currentPlayer,
            playerTurn = currentPlayer,
            player2 = null
        )
    }

}