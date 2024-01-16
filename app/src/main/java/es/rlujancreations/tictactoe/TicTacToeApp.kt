package es.rlujancreations.tictactoe

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Raúl Luján Colilla on 9/12/23.
 */
@HiltAndroidApp
class TicTacToeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}