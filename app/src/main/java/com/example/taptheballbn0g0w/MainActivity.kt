package com.example.taptheballbn0g0w

import androidx.compose.material3.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.compose.ui.geometry.Offset
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.example.taptheballbn0g0w.ui.theme.TapTheBallBN0G0WTheme
import kotlin.random.Random

// Made by Fülöp Bence BN0G0W using Andriko Imre's (http://eprog.hu/oktatas/)
// and various other (youtube, stackowerflow, etc...) sources, to pass the 'Mobilprog' subject.

// No extraordinary tapping game to pass the time.
// Featuring higscore storage and

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TapTheBallBN0G0WTheme {
                TapTheBallGame()
            }
        }
    }
}


@Composable
fun TapTheBallGame() {
    var score by remember { mutableStateOf(0) }
    var ballPositionX by remember { mutableStateOf(150f) } // Initial X position
    var ballPositionY by remember { mutableStateOf(200f) } // Initial Y position
    var isGameOver by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(10) }
    val ballRadius = 50f

    var previousScore by remember { mutableStateOf(0) }
    var highScore by remember { mutableStateOf(0) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    LaunchedEffect(timeLeft) {
        if (!isGameOver) {
            while (timeLeft > 0) {
                kotlinx.coroutines.delay(1000)
                timeLeft--
            }
            isGameOver = true

            // Update hi'score
            previousScore = score
            if (score > highScore) {
                highScore = score
            }
        }
    }

    if (isGameOver) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Game Over!", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Score: $score", style = MaterialTheme.typography.bodyMedium)
            Text(text = "High Score: $highScore", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                score = 0
                ballPositionX = 150f
                ballPositionY = 200f
                timeLeft = 10
                isGameOver = false
            }) {
                Text(text = "Restart")
            }
        }
    } else {
        // Main game UI
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        val dx = tapOffset.x - ballPositionX
                        val dy = tapOffset.y - ballPositionY
                        // Ball tapped score trigger
                        if (dx * dx + dy * dy <= ballRadius * ballRadius) {
                            ballPositionX = Random.nextFloat() * (screenWidth.value - 2 * ballRadius) + ballRadius
                            ballPositionY = Random.nextFloat() * (screenHeight.value - 2 * ballRadius) + ballRadius
                            score++
                        }
                    }
                }
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawCircle(
                    color = Color.Red,
                    center = Offset(
                        ballPositionX.coerceIn(ballRadius, screenWidth.value - ballRadius),
                        ballPositionY.coerceIn(ballRadius, screenHeight.value - ballRadius)
                    ),
                    radius = ballRadius
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            ) {
                Text(text = "Score: $score", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Time Left: $timeLeft s", style = MaterialTheme.typography.bodyMedium)
                Text(text = "High Score: $highScore", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
