package com.openclassrooms.visibilitelocaltest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.visibilitelocaltest.ui.theme.VisibiliteLocalTestTheme
import kotlinx.coroutines.channels.Channel


class BottomBarHandler {
    var isServerDown = Channel<Boolean>()

    suspend fun declareServerDown() {
        isServerDown.send(true)
    }
}

class MainActivity : ComponentActivity() {

    val bottomBarHandler = BottomBarHandler()
    private val LocalBottomBarVisibility = compositionLocalOf { bottomBarHandler.isServerDown }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisibiliteLocalTestTheme {
                CompositionLocalProvider(LocalBottomBarVisibility provides true) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column {
                            val bottomBarVisible = LocalBottomBarVisibility.current

                            if (bottomBarVisible) {
                                Greeting("Android")
                            }
                            TextButton(onClick = { setBottomBarVisible(!bottomBarVisible) }) {
                                Text(text = "Visible ?")
                            }
                        }
                    }
                }
            }
        }
    }
    private fun setBottomBarVisible(update: Boolean) {
//        isVisible.value = update
        LocalBottomBarVisibility .provides(false)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VisibiliteLocalTestTheme {
        Greeting("Android")
    }
}