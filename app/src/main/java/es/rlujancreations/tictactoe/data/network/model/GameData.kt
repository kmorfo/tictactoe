package es.rlujancreations.tictactoe.data.network.model

import es.rlujancreations.tictactoe.ui.model.GameModel
import es.rlujancreations.tictactoe.ui.model.PlayerModel
import es.rlujancreations.tictactoe.ui.model.PlayerType
import java.util.Calendar

/**
 * Created by Raúl Luján Colilla on 9/12/23.
 */
data class GameData(
    val board: List<Int?>? = null,
    val gameId: String? = null,
    val player1: PlayerData? = null,
    val player2: PlayerData? = null,
    val playerTurn: PlayerData? = null
) {
    fun toModel(): GameModel {
        return GameModel(
            board = board?.map { PlayerType.getPlayerById(it) } ?: mutableListOf(),
            gameId = gameId.orEmpty(),
            player1 = player1!!.toModel(),
            player2 = player2?.toModel(),
            playerTurn = playerTurn!!.toModel()
        )
    }
}

data class PlayerData(
    val userId: String? = Calendar.getInstance().timeInMillis.hashCode().toString(),
    val playerType: Int? = null
) {
    fun toModel(): PlayerModel {
        return PlayerModel(userId!!, PlayerType.getPlayerById(playerType))
    }
}