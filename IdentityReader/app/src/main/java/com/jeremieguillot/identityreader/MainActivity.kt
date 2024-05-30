package com.jeremieguillot.identityreader

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jeremieguillot.identityreader.core.ui.theme.IdentityReaderTheme
import com.jeremieguillot.identityreader.nfc.presentation.reader.NfcReaderScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, 0
            )
        }

        enableEdgeToEdge()
        setContent {
            IdentityReaderTheme {
                NfcReaderScreen()
//                val haptic = LocalHapticFeedback.current
//
//                var mrz by remember { mutableStateOf<MRZ?>(null) }
//                if (mrz != null) {
//                    Box(modifier = Modifier.fillMaxSize()) {
//                        LaunchedEffect(Unit) {
//                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                        }
//
//                        Column(Modifier.align(Alignment.Center)) {
//                            Text(text = mrz.toString())
//                            Button(onClick = {
//                                mrz = null
//                            }, modifier = Modifier.padding(16.dp)) {
//                                Text(text = "Retry")
//                            }
//                        }
//                    }
//                } else {
//                    ScanScreen {
//                        mrz = it
//                    }
//                }
            }
        }
    }


    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )
    }
}