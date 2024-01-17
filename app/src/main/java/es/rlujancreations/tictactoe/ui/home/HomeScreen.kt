package es.rlujancreations.tictactoe.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import es.rlujancreations.tictactoe.R
import es.rlujancreations.tictactoe.ui.theme.Accent
import es.rlujancreations.tictactoe.ui.theme.Background
import es.rlujancreations.tictactoe.ui.theme.Orange
import es.rlujancreations.tictactoe.ui.theme.OrangeLight

/**
 * Created by Raúl Luján Colilla on 9/12/23.
 */

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToGame: (String, String, Boolean) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Background
            )
    ) {
        Header()
        Body(
            onCreateGame = { homeViewModel.onCreateGame(navigateToGame) },
            onJoinGame = { gameId -> homeViewModel.onJoinGame(gameId, navigateToGame) })
    }
}

@Composable
fun Header() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            Modifier
                .size(200.dp)
                .padding(12.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(2.dp, Orange, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                painter = painterResource(id = R.drawable.tictactoe),
                contentDescription = stringResource(id = R.string.applogo)
            )
        }
        Text(
            text = stringResource(id = R.string.firebase),
            fontSize = 32.sp,
            color = Orange,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 28.sp,
            color = OrangeLight,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Body(onCreateGame: () -> Unit, onJoinGame: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(24.dp),
        backgroundColor = Background,
        border = BorderStroke(4.dp, Orange),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            var createGame by remember { mutableStateOf(false) }
            Switch(
                checked = createGame,
                onCheckedChange = { createGame = it },
                colors = SwitchDefaults.colors(checkedThumbColor = OrangeLight)
            )
            AnimatedContent(targetState = createGame, label = "") {
                when (it) {
                    true -> CreateGame(onCreateGame)
                    false -> JoinGame(onJoinGame)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun CreateGame(onCreateGame: () -> Unit) {
    Button(
        onClick = { onCreateGame() },
        colors = ButtonDefaults.buttonColors(backgroundColor = Orange)
    ) {
        Text(text = stringResource(id = R.string.creategame), color = Accent)
    }
}

@Composable
fun JoinGame(onJoinGame: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Accent,
                focusedBorderColor = Orange,
                unfocusedBorderColor = Accent,
                cursorColor = Orange
            )
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { onJoinGame(text) },
            enabled = text.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Orange)
        ) {
            Text(text = stringResource(id = R.string.joingame), color = Accent)
        }
    }
}