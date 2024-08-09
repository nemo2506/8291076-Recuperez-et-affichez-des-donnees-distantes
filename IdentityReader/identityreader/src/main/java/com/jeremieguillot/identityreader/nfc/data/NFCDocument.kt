package com.jeremieguillot.identityreader.nfc.data

import android.nfc.tech.IsoDep
import android.util.Log
import com.jeremieguillot.identityreader.core.domain.DocumentType
import com.jeremieguillot.identityreader.core.domain.IdentityDocument
import com.jeremieguillot.identityreader.core.domain.MRZ
import com.jeremieguillot.identityreader.core.domain.util.DataError
import com.jeremieguillot.identityreader.core.domain.util.Error
import com.jeremieguillot.identityreader.core.domain.util.Result
import net.sf.scuba.smartcards.CardService
import org.jmrtd.BACKey
import org.jmrtd.BACKeySpec
import org.jmrtd.PassportService
import org.jmrtd.lds.CardAccessFile
import org.jmrtd.lds.PACEInfo
import org.jmrtd.lds.icao.DG11File
import org.jmrtd.lds.icao.DG1File


class NFCDocument {

    fun startReadTask(isoDep: IsoDep, mrz: MRZ): Result<IdentityDocument, Error> {
        val bacKey: BACKeySpec = BACKey(mrz.documentNumber, mrz.dateOfBirth, mrz.dateOfExpiry)
        return readPassport(isoDep, bacKey)
    }

    private fun readPassport(
        isoDep: IsoDep,
        bacKey: BACKeySpec
    ): Result<IdentityDocument, Error> {

        try {
            isoDep.timeout = 15_000
            val cardService = CardService.getInstance(isoDep).apply { open() }
            val service = PassportService(
                /* service = */ cardService,
                /* maxTranceiveLengthForSecureMessaging = */
                PassportService.NORMAL_MAX_TRANCEIVE_LENGTH,
                /* maxBlockSize = */
                PassportService.DEFAULT_MAX_BLOCKSIZE,
                /* isSFIEnabled = */
                false,
                /* shouldCheckMAC = */
                false
            ).apply { open() }


            val paceSucceeded = doPace(service, bacKey)
            service.sendSelectApplet(paceSucceeded)
            if (!paceSucceeded) try {
                service.getInputStream(PassportService.EF_COM).read()
            } catch (e: Exception) {
                service.doBAC(bacKey)
            }

            val dg1File = DG1File(service.getInputStream(PassportService.EF_DG1))
            val dg11File = DG11File(service.getInputStream(PassportService.EF_DG11))


            return Result.Success(
                IdentityDocument(
                    type = DocumentType.PASSPORT,
                    documentNumber = dg1File.mrzInfo.documentNumber,
                    firstName = dg1File.mrzInfo.primaryIdentifier,
                    lastName = dg1File.mrzInfo.secondaryIdentifier,
                    gender = dg1File.mrzInfo.gender.toString(),
                    origin = dg1File.mrzInfo.issuingState,
                    nationality = dg1File.mrzInfo.nationality,
                    address = dg11File.permanentAddress.first(),
                    addressNumber = "",
                    city = dg11File.permanentAddress[2],
                    postalCode = dg11File.permanentAddress[1],
                    country = dg11File.permanentAddress[4],
                    placeOfBirth = ""
//                birthDate = dg1File.mrzInfo.birthDate,
//                expirationDate = dg1File.mrzInfo.expirationDate
                )
            )

        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            return Result.Error(DataError.Local.INVALID_DATA)
        }
    }

    private fun doPace(service: PassportService, bacKey: BACKeySpec): Boolean = runCatching {
        val inputStream = service.getInputStream(
            PassportService.EF_CARD_SECURITY,
            PassportService.DEFAULT_MAX_BLOCKSIZE
        )
        CardAccessFile(inputStream)
            .securityInfos
            .filterIsInstance<PACEInfo>()
            .forEach {
                service.doPACE(
                    bacKey,
                    it.objectIdentifier,
                    PACEInfo.toParameterSpec(it.parameterId),
                    null
                )
            }
        true
    }.getOrElse {
        Log.w(TAG, it)
        false
    }

    companion object {
        private const val TAG = "ReadTask"
    }
}
