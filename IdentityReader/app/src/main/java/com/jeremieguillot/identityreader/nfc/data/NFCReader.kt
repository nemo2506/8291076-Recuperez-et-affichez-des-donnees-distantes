package com.jeremieguillot.identityreader.nfc.data

import android.content.Context
import android.nfc.NfcAdapter
import com.jeremieguillot.identityreader.core.domain.MRZ
import com.jeremieguillot.identityreader.nfc.domain.NfcReaderStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NFCReader(mrz: MRZ, context: Context) {

    private val scope = CoroutineScope(Dispatchers.Main)
    private val nfcAdapter: NfcAdapter = NfcAdapter.getDefaultAdapter(context)
    private val _status: MutableStateFlow<NfcReaderStatus> = MutableStateFlow(NfcReaderStatus.IDLE)
    val status: MutableStateFlow<NfcReaderStatus> = _status

    init {
        if (!nfcAdapter.isEnabled) {
            scope.launch {
                _status.emit(NfcReaderStatus.DISABLED)
            }
        }
    }
}