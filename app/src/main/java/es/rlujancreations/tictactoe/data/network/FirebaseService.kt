package es.rlujancreations.tictactoe.data.network

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.snapshots
import es.rlujancreations.tictactoe.data.network.model.GameData
import es.rlujancreations.tictactoe.ui.model.GameModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Raúl Luján Colilla on 9/12/23.
 */
class FirebaseService @Inject constructor(private val reference: DatabaseReference) {

    companion object {
        private const val PATH = "games"
    }

    fun createGame(gameData: GameData): String {
        val gameReference = reference.child(PATH).push()
        val key = gameReference.key
        val newGame: GameData = gameData.copy(gameId = key)
        gameReference.setValue(newGame)
        return newGame.gameId.orEmpty()
    }

    fun joinToGame(gameId: String): Flow<GameModel?> {
        return reference.child("/$PATH/$gameId").snapshots.map { dataSnapshot ->
            dataSnapshot.getValue(GameData::class.java)?.toModel()
        }
    }

    fun updateGame(gameData: GameData) {
        if (gameData.gameId != null) {
            reference.child("/$PATH/${gameData.gameId}").setValue(gameData)
        }
    }

    fun checkIfGameExists(gameId: String): Flow<Boolean> {
        return reference.child("/$PATH/$gameId").snapshots.map { dataSnapshot ->
            dataSnapshot.exists()
        }
    }
}
