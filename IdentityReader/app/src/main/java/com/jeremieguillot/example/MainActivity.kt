package com.jeremieguillot.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import com.jeremieguillot.identityreader.ReaderActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            val context = LocalContext.current
            val haptic = LocalHapticFeedback.current

            Box(modifier = Modifier.fillMaxSize()) {
                Button(modifier = Modifier.align(Alignment.Center), onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    startActivity(
                        Intent(
                            context,
                            ReaderActivity::class.java
                        )
                    )
                }) {
                    Text(text = "Scanner une pièce d'identité")
                }
            }

        }
    }

}