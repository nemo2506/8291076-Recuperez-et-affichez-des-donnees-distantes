package com.jeremieguillot.identityreader.nfc.presentation.reader

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.util.Consumer
import com.jeremieguillot.identityreader.MainActivity
import com.jeremieguillot.identityreader.core.domain.IdentityDocument
import com.jeremieguillot.identityreader.core.domain.MRZ
import com.jeremieguillot.identityreader.core.domain.util.Result
import com.jeremieguillot.identityreader.nfc.data.NFCReader
import com.jeremieguillot.identityreader.nfc.domain.NfcReaderStatus
import com.jeremieguillot.identityreader.nfc.presentation.reader.components.ReaderAnimation
import com.jeremieguillot.identityreader.nfc.presentation.reader.components.getDescription
import com.jeremieguillot.identityreader.nfc.presentation.reader.components.getTitle
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NfcReaderScreen(mrz: MRZ) {

    val context = LocalContext.current
    val reader = remember { NFCReader(mrz, context) }
    val status by reader.status.collectAsState(NfcReaderStatus.IDLE)
    val scope = rememberCoroutineScope()

    var identity by remember {
        mutableStateOf<IdentityDocument?>(null)
    }
    if (identity != null) {
        AlertDialog(onDismissRequest = { identity = null }) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                Text(text = "Document")
                Text(text = identity!!.type.name)
                Text(text = identity!!.documentNumber)
                Text(text = identity!!.origin)
                Text(text = " - ")
                Text(text = "Identité")
                Text(text = identity!!.lastName)
                Text(text = identity!!.firstName)
                Text(text = identity!!.nationality)
                Text(text = identity!!.gender)
                Text(text = identity!!.addressNumber)
                Text(text = identity!!.address)
                Text(text = identity!!.postalCode)
                Text(text = identity!!.city)
                Text(text = identity!!.country)

                Button(onClick = {
                    identity = null
                    reader.reset()
                }) {
                    Text(text = "OK")
                }
            }

        }
    }

    DisposableEffect(Unit) {
        val listener = Consumer<Intent> { intent ->
            if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
                val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
                scope.launch {
                    when (val result = reader.onTagDiscovered(tag)) {
                        is Result.Error -> identity = null
                        is Result.Success -> identity = result.data
                    }
                }
            }
        }
        (context as MainActivity).addOnNewIntentListener(listener)
        onDispose { context.removeOnNewIntentListener(listener) }
    }


    Scaffold(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            ReaderAnimation(status, Modifier.align(Alignment.Center))

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 160.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    status.getTitle(),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(status.getDescription(), textAlign = TextAlign.Center)
            }
        }
    }
}