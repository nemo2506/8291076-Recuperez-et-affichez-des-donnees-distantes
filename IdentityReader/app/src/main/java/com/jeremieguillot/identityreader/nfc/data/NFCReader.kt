package com.jeremieguillot.identityreader.nfc.data

import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.provider.Settings
import com.jeremieguillot.identityreader.core.domain.IdentityDocument
import com.jeremieguillot.identityreader.core.domain.MRZ
import com.jeremieguillot.identityreader.core.domain.util.Error
import com.jeremieguillot.identityreader.core.domain.util.Result
import com.jeremieguillot.identityreader.nfc.domain.NfcReaderStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jmrtd.BACKey
import org.jmrtd.BACKeySpec

class NFCReader(private val mrz: MRZ, private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Main)

    //    private val nfcAdapter: NfcAdapter = NfcAdapter.getDefaultAdapter(context)
    private val _status: MutableStateFlow<NfcReaderStatus> = MutableStateFlow(NfcReaderStatus.IDLE)
    val status: MutableStateFlow<NfcReaderStatus> = _status

    init {
        if (true) {
//            val activity = context as MainActivity
//            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                PendingIntent.getActivity(context, 0, Intent(context, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE)
//            } else{
//                PendingIntent.getActivity(context, 0, Intent(context, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE)
//            }
//            nfcAdapter.enableForegroundDispatch(activity, pendingIntent, null, null)
        } else {
            scope.launch {
                _status.emit(NfcReaderStatus.DISABLED)
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                context.startActivity(intent)
            }
        }
    }

    fun onTagDiscovered(tag: Tag?): Result<IdentityDocument, Error> {
        scope.launch {
            _status.emit(NfcReaderStatus.CONNECTING)
        }
        val bacKey: BACKeySpec = BACKey(mrz.documentNumber, mrz.dateOfBirth, mrz.dateOfExpiry)
        val result = NFCDocument().startReadTask(IsoDep.get(tag), bacKey)
        scope.launch {
            when (result) {
                is Result.Error -> _status.emit(NfcReaderStatus.ERROR)
                is Result.Success -> _status.emit(NfcReaderStatus.CONNECTED)
            }
        }
        return result
    }

    fun reset() {
        scope.launch {
            _status.emit(NfcReaderStatus.IDLE)
        }
    }
}