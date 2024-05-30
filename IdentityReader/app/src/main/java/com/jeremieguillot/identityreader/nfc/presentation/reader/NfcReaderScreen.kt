package com.jeremieguillot.identityreader.nfc.presentation.reader

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.util.Consumer
import androidx.navigation.NavHostController
import com.jeremieguillot.identityreader.MainActivity
import com.jeremieguillot.identityreader.R
import com.jeremieguillot.identityreader.core.domain.IdentityDocument
import com.jeremieguillot.identityreader.core.domain.MRZ
import com.jeremieguillot.identityreader.core.domain.util.Result
import com.jeremieguillot.identityreader.core.extension.toMRZFormat
import com.jeremieguillot.identityreader.core.presentation.Destination
import com.jeremieguillot.identityreader.nfc.data.NFCReader
import com.jeremieguillot.identityreader.nfc.domain.NfcReaderStatus
import com.jeremieguillot.identityreader.nfc.presentation.reader.components.MRZCard
import com.jeremieguillot.identityreader.nfc.presentation.reader.components.ModifyMRZDialog
import com.jeremieguillot.identityreader.nfc.presentation.reader.components.ReaderAnimation
import com.jeremieguillot.identityreader.nfc.presentation.reader.components.getDescription
import com.jeremieguillot.identityreader.nfc.presentation.reader.components.getTitle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NfcReaderScreen(navController: NavHostController, mrz: MRZ) {

    var localMRZ by remember { mutableStateOf(mrz) }
    val context = LocalContext.current
    val reader = remember { NFCReader(mrz) }
    val status by reader.status.collectAsState(NfcReaderStatus.IDLE)
    var errorCounter by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ModifyMRZDialog(
            mrz = localMRZ,
            onDismiss = { showDialog = false },
            onSave = { documentNumber, dateOfBirth, dateOfExpiry ->
                errorCounter = 0
                val dateOfBirth1 = dateOfBirth.toMRZFormat()
                localMRZ = MRZ(documentNumber, dateOfBirth1, dateOfExpiry.toMRZFormat())
                reader.update(localMRZ)
            })
    }

    var identity by remember {
        mutableStateOf<IdentityDocument?>(null)
    }
    if (identity != null) {
        BasicAlertDialog(onDismissRequest = { identity = null }) {
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
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            identity = when (val result = reader.onTagDiscovered(tag)) {
                is Result.Error -> {
                    errorCounter++
                    null
                }

                is Result.Success -> result.data
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

            MRZCard(localMRZ, Modifier.align(Alignment.TopCenter))

            ReaderAnimation(status, Modifier.align(Alignment.Center))

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    status.getTitle(),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(status.getDescription(), textAlign = TextAlign.Center)

                if (errorCounter > 1) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                navController.navigate(Destination.ScannerScreen) {
                                    popUpTo(
                                        Destination.ScannerScreen
                                    ) { inclusive = true }
                                }
                            }) {
                            Text(text = stringResource(R.string.rescan))
                        }
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = { showDialog = true }) {
                            Text(text = stringResource(R.string.modifier))
                        }
                    }
                }
            }
        }
    }
}