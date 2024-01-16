package es.rlujancreations.tictactoe.ui.game

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import es.rlujancreations.tictactoe.ui.model.GameModel
import es.rlujancreations.tictactoe.ui.model.PlayerType
import es.rlujancreations.tictactoe.ui.theme.Accent
import es.rlujancreations.tictactoe.ui.theme.Background
import es.rlujancreations.tictactoe.ui.theme.BlueLink
import es.rlujancreations.tictactoe.ui.theme.Orange
import es.rlujancreations.tictactoe.ui.theme.OrangeLight


/**
 * Created by Raúl Luján Colilla on 9/12/23.
 */
@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel(),
    gameId: String, userId: String, owner: Boolean,
    navigateToHome: () -> Unit
) {
    LaunchedEffect(true) {
        gameViewModel.joinToGame(gameId, userId, owner)
    }

    val game: GameModel? by gameViewModel.game.collectAsState()
    val winner: PlayerType? by gameViewModel.winner.collectAsState()

    if (winner != null) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Background), contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(24.dp),
                backgroundColor = Background,
                border = BorderStroke(4.dp, Orange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "¡Felicidades!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Orange
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val currentWinner =
                        if (winner == PlayerType.FirstPlayer) "Player 1" else "Player 2"
                    Text(text = "Ha ganado el jugador:", fontSize = 22.sp, color = Accent)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = currentWinner,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangeLight
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { navigateToHome() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Orange)
                    ) {
                        Text(text = "Volver al inicio", color = Accent)
                    }
                }
            }
        }
    } else {
        Board(game, onItemSelected = { gameViewModel.onItemSelected(it) })

    }

}

@Composable
fun Board(game: GameModel?, onItemSelected: (Int) -> Unit) {
    if (game == null) return

    val clipBoard: ClipboardManager = LocalClipboardManager.current
    val context: Context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .background(Background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = game.gameId, color = BlueLink, modifier = Modifier
            .padding(24.dp)
            .clickable {
                clipBoard.setText(AnnotatedString(game.gameId))
                Toast
                    .makeText(context, "Copiado!", Toast.LENGTH_SHORT)
                    .show()
            }
        )
        val status = if (game.isGameReady == true) {
            if (game.isMyTurn) {
                "Tu turno"
            } else {
                "Turno rival"
            }
        } else {
            "Esperando por el jugador 2"
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = status, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Accent)
            Spacer(modifier = Modifier.width(4.dp))
            if (!game.isGameReady && !game.isMyTurn)
                CircularProgressIndicator(
                    Modifier.size(18.dp),
                    color = Orange,
                    backgroundColor = OrangeLight
                )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            GameItem(game.board[0]) { onItemSelected(0) }
            GameItem(game.board[1]) { onItemSelected(1) }
            GameItem(game.board[2]) { onItemSelected(2) }
        }
        Row {
            GameItem(game.board[3]) { onItemSelected(3) }
            GameItem(game.board[4]) { onItemSelected(4) }
            GameItem(game.board[5]) { onItemSelected(5) }
        }
        Row {
            GameItem(game.board[6]) { onItemSelected(6) }
            GameItem(game.board[7]) { onItemSelected(7) }
            GameItem(game.board[8]) { onItemSelected(8) }
        }
    }
}


@Composable
fun GameItem(playerType: PlayerType, onItemSelected: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(12.dp)
            .size(64.dp)
            .border(BorderStroke(2.dp, Accent))
            .clickable { onItemSelected() },
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(targetState = playerType.symbol, label = "") {
            Text(
                text = it,
                color = if (playerType is PlayerType.FirstPlayer) OrangeLight else Orange,
                fontSize = 32.sp
            )
        }
    }
}