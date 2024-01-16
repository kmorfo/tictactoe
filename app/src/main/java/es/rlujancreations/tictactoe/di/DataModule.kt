package es.rlujancreations.tictactoe.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.rlujancreations.tictactoe.data.network.FirebaseService
import javax.inject.Singleton

/**
 * Created by Raúl Luján Colilla on 9/12/23.
 */
@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Singleton
    @Provides
    fun provideDatabaseReference() = Firebase.database.reference.child("TicTacToe")

    @Singleton
    @Provides
    fun provideFirebaseService(databaseReference: DatabaseReference) =
        FirebaseService(databaseReference)


}