//package com.jeremieguillot.identityreader.nfc.data
//
//import android.content.Context
//import android.nfc.Tag
//import android.nfc.tech.IsoDep
//import com.jeremieguillot.identityreader.core.domain.MRZ
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import net.sf.scuba.smartcards.CardService
//import org.bouncycastle.jce.provider.BouncyCastleProvider
//import org.jmrtd.MRTDTrustStore
//import org.jmrtd.PassportService
//import java.security.Security
//import kotlin.math.max
//
//
//class NFCDocumentTag {
//
//    suspend fun handleTag(
//        tag: Tag,
//        mrzInfo: MRZ,
//        mrtdTrustStore: MRTDTrustStore
//    ): Result<Passport> = withContext(Dispatchers.IO) {
//        try {
//            val nfc = IsoDep.get(tag).apply { timeout = max(timeout, 2000) }
//            val cardService = CardService.getInstance(nfc)
//            PassportService(
//                cardService,
//                PassportService.NORMAL_MAX_TRANCEIVE_LENGTH,
//                PassportService.NORMAL_MAX_TRANCEIVE_LENGTH,
//                PassportService.DEFAULT_MAX_BLOCKSIZE,
//                false,
//                false,
//            ).apply { ps ->
//                ps.open()
//                val passportNFC = PassportNFC(ps, mrtdTrustStore, mrzInfo, PassportNFC.MAX_BLOCK_SIZE)
//                val passport = createPassportFromNFC(passportNFC)
//                Result.success(passport)
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    private fun createPassportFromNFC(passportNFC: PassportNFC): Passport {
//        return Passport().apply {
//            featureStatus = passportNFC.features
//            verificationStatus = passportNFC.verificationStatus
//            sodFile = passportNFC.sodFile
//
//            dg1File?.let { dg1 ->
//                val mrzInfo = (dg1 as DG1File).mrzInfo
//                personDetails = PersonDetails(
//                    dateOfBirth = mrzInfo.dateOfBirth,
//                    dateOfExpiry = mrzInfo.dateOfExpiry,
//                    documentCode = mrzInfo.documentCode,
//                    documentNumber = mrzInfo.documentNumber,
//                    optionalData1 = mrzInfo.optionalData1,
//                    optionalData2 = mrzInfo.optionalData2,
//                    issuingState = mrzInfo.issuingState,
//                    primaryIdentifier = mrzInfo.primaryIdentifier,
//                    secondaryIdentifier = mrzInfo.secondaryIdentifier,
//                    nationality = mrzInfo.nationality,
//                    gender = mrzInfo.gender
//                )
//            }
//
//            dg11File?.let { dg11 ->
//                additionalPersonDetails = AdditionalPersonDetails(
//                    custodyInformation = dg11.custodyInformation,
//                    fullDateOfBirth = dg11.fullDateOfBirth,
//                    nameOfHolder = dg11.nameOfHolder,
//                    otherNames = dg11.otherNames,
//                    otherValidTDNumbers = dg11.otherValidTDNumbers,
//                    permanentAddress = dg11.permanentAddress,
//                    personalNumber = dg11.personalNumber,
//                    personalSummary = dg11.personalSummary,
//                    placeOfBirth = dg11.placeOfBirth,
//                    profession = dg11.profession,
//                    proofOfCitizenship = dg11.proofOfCitizenship,
//                    tag = dg11.tag,
//                    tagPresenceList = dg11.tagPresenceList,
//                    telephone = dg11.telephone,
//                    title = dg11.title
//                )
//            }
//
//            dg12File?.let { dg12 ->
//                additionalDocumentDetails = AdditionalDocumentDetails(
//                    dateAndTimeOfPersonalization = dg12.dateAndTimeOfPersonalization,
//                    dateOfIssue = dg12.dateOfIssue,
//                    endorsementsAndObservations = dg12.endorsementsAndObservations,
//                    issuingAuthority = dg12.issuingAuthority,
//                    namesOfOtherPersons = dg12.namesOfOtherPersons,
//                    personalizationSystemSerialNumber = dg12.personalizationSystemSerialNumber,
//                    taxOrExitRequirements = dg12.taxOrExitRequirements
//                )
//            }
//        }
//    }
//
//    companion object {
//        private val TAG = NFCDocumentTag::class.java.simpleName
//
//        init {
//            Security.insertProviderAt(BouncyCastleProvider(), 1)
//        }
//    }
//}
