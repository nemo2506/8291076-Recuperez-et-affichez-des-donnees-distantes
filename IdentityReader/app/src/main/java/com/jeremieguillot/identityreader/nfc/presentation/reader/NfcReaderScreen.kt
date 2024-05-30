package com.jeremieguillot.identityreader.nfc.presentation.reader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeremieguillot.identityreader.core.domain.MRZ
import com.jeremieguillot.identityreader.core.ui.theme.IdentityReaderTheme
import com.jeremieguillot.identityreader.nfc.data.NFCReader
import com.jeremieguillot.identityreader.nfc.domain.NfcReaderStatus
import com.jeremieguillot.identityreader.nfc.presentation.reader.components.ReaderAnimation
import com.jeremieguillot.identityreader.nfc.presentation.reader.components.getDescription
import com.jeremieguillot.identityreader.nfc.presentation.reader.components.getTitle

@Composable
fun NfcReaderScreen(mrz: MRZ) {
    Scaffold(modifier = Modifier.fillMaxSize()) {

        val context = LocalContext.current
        val reader = remember { NFCReader(mrz, context) }
        val status by reader.status.collectAsState(NfcReaderStatus.IDLE)


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

@Preview
@Composable
private fun NfcReaderScreenPrev() {
    IdentityReaderTheme {
        NfcReaderScreen(MRZ.EMPTY)
    }
}