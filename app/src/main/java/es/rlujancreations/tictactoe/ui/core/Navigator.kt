package es.rlujancreations.tictactoe.ui.core

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import es.rlujancreations.tictactoe.ui.core.Routes.Game
import es.rlujancreations.tictactoe.ui.core.Routes.Home
import es.rlujancreations.tictactoe.ui.game.GameScreen
import es.rlujancreations.tictactoe.ui.home.HomeScreen

/**
 * Created by Raúl Luján Colilla on 9/12/23.
 *
 * https://developer.android.com/jetpack/compose/navigation?hl=es-419
 */

@Composable
fun ContentWrapper(navController: NavHostController = rememberNavController()) {
    val uri = "https://rlujancreations.es"
    var isUsedDeepLink by rememberSaveable { mutableStateOf(false) }

    NavHost(navController = navController, startDestination = Home.route) {
        composable(
            route = Home.route,
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/{id}" })
        ) {
            val id: String = if (!isUsedDeepLink)
                it.arguments?.getString("id").orEmpty()
            else ""

            isUsedDeepLink = true
            HomeScreen(
                gameId = id,
                navigateToGame = { gameId, userId, owner ->
                    navController.navigate(
                        Game.createRoute(gameId, userId, owner)
                    )
                })
        }
        composable(
            Game.route,
            arguments = listOf(
                navArgument("gameId") { type = NavType.StringType },
                navArgument("userId") { type = NavType.StringType },
                navArgument("owner") { type = NavType.BoolType }
            ),
        ) {
            GameScreen(
                gameId = it.arguments?.getString("gameId").orEmpty(),
                userId = it.arguments?.getString("userId").orEmpty(),
                owner = it.arguments?.getBoolean("owner") ?: false,
                navigateToHome = { navController.popBackStack(Home.route, inclusive = false) }
            )
        }
    }
}

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Game : Routes("game/{gameId}/{userId}/{owner}") {
        fun createRoute(gameId: String, userId: String, owner: Boolean): String {
            return "game/$gameId/$userId/$owner"
        }
    }
}