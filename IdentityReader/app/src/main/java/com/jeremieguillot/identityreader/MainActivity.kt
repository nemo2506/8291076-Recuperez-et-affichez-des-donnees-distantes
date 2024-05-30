package com.jeremieguillot.identityreader

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jeremieguillot.identityreader.core.domain.MRZ
import com.jeremieguillot.identityreader.core.ui.theme.IdentityReaderTheme
import com.jeremieguillot.identityreader.nfc.presentation.reader.NfcReaderScreen

class MainActivity : ComponentActivity() {

    override fun onResume() {
        super.onResume()
        val adapter = NfcAdapter.getDefaultAdapter(this)
        if (adapter != null) {
            val intent = Intent(applicationContext, this.javaClass)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
            val filter = arrayOf(arrayOf("android.nfc.tech.IsoDep"))
            adapter.enableForegroundDispatch(this, pendingIntent, null, filter)
        }
    }

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
                val mrz by remember { mutableStateOf(MRZ.FAKE) }


                NfcReaderScreen(mrz)
                val haptic = LocalHapticFeedback.current
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