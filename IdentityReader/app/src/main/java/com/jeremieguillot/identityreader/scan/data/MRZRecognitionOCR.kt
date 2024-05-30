package com.jeremieguillot.identityreader.scan.data

import com.google.mlkit.vision.text.Text
import com.jeremieguillot.identityreader.core.domain.MRZ
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * The optical zone of a biometric passport, also known as the Machine Readable Zone (MRZ), consists of two lines of text containing essential information about the passport holder and the document itself. This information is encoded to be quickly readable by machines. Here's the composition of each line, section by section:
 *
 * ### First line of the Machine Readable Zone (MRZ)
 * 1. **Positions 1-2**: Document type
 *    - Typically "P" for passport.
 * 2. **Positions 3-5**: Issuing country code
 *    - A three-letter code corresponding to ISO 3166-1 alpha-3.
 * 3. **Positions 6-44**: Full name
 *    - The name is separated by two chevrons ("<<") between the surname and given names. Spaces in names are also represented by chevrons.
 *
 * ### Second line of the Machine Readable Zone (MRZ)
 * 1. **Positions 1-9**: Passport number
 *    - Followed by a check digit at position 10.
 * 2. **Positions 11-13**: Nationality code
 *    - A three-letter code corresponding to ISO 3166-1 alpha-3 of the holder's nationality.
 * 3. **Positions 14-19**: Date of birth
 *    - YYMMDD format, followed by a check digit at position 20.
 * 4. **Position 21**: Gender
 *    - "M" for male, "F" for female, or "X" for unspecified.
 * 5. **Positions 22-27**: Passport expiration date
 *    - YYMMDD format, followed by a check digit at position 28.
 * 6. **Positions 29-42**: Personal identification number (if applicable)
 *    - Sometimes used for a national identity card number or other relevant identifier, followed by a check digit at position 43.
 * 7. **Positions 44-44**: Overall check character
 *    - This check character is calculated over all the data from both lines to verify the integrity of the information.
 *
 * ### Example MRZ for a passport:
 * First line:
 * ```
 * P<UTOERIKSSON<<ANNA<MARIA<<<<<<<<<<<<<<<<<<<
 * ```
 * Second line:
 * ```
 * L898902C36UTO7408122F1204159<<<<<<<<<<<<<<00
 * ```
 *
 * ### Explanation of the example:
 *
 * 1. **P**: Document type (Passport)
 * 2. **UTO**: Issuing country code (Utopia)
 * 3. **ERIKSSON<<ANNA<MARIA**: Surname (ERIKSSON), Given name (ANNA MARIA)
 * 4. **L898902C3**: Passport number
 * 5. **6**: Check digit for passport number
 * 6. **UTO**: Nationality (Utopia)
 * 7. **740812**: Date of birth (12 August 1974)
 * 8. **2**: Check digit for date of birth
 * 9. **F**: Gender (Female)
 * 10. **120415**: Expiration date (15 April 2012)
 * 11. **9**: Check digit for expiration date
 * 12. **<<<<<<<<<<<<<<00**: Personal identifier and overall check character
 */
sealed class MRZResult {
    data class Success(val mrz: MRZ) : MRZResult()
    data object Failure : MRZResult()
}

class MRZRecognitionOCR {
    private val DOCUMENT_NUMBER = "documentNumber"
    private val DIGIT_DOCUMENT_NUMBER = "checkDigitDocumentNumber"
    private val BIRTH_DATE = "dateOfBirth"
    private val EXPIRATION_DATE = "expirationDate"

    private val REGEX_OLD_PASSPORT =
        "(?<documentNumber>[A-Z0-9<]{9})(?<checkDigitDocumentNumber>[0-9ILDSOG]{1})(?<nationality>[A-Z<]{3})(?<dateOfBirth>[0-9ILDSOG]{6})(?<checkDigitDateOfBirth>[0-9ILDSOG]{1})(?<sex>[FM<]){1}(?<expirationDate>[0-9ILDSOG]{6})(?<checkDigitExpiration>[0-9ILDSOG]{1})"
    private val REGEX_IP_PASSPORT_LINE_1 = "\\bIP[A-Z<]{3}[A-Z0-9<]{9}[0-9]{1}"
    private val REGEX_IP_PASSPORT_LINE_2 = "[0-9]{6}[0-9]{1}[FM<]{1}[0-9]{6}[0-9]{1}[A-Z<]{3}"

    fun recognize(blocks: List<Text.TextBlock>): MRZResult {
        val fullRead = blocks.joinToString("-") { block ->
            block.lines.joinToString("-") { line ->
                line.text.trim()
                    .replace("\r", "")
                    .replace("\n", "")
                    .replace("\t", "")
                    .replace(" ", "")
            }
        }.uppercase(Locale.getDefault())

        val oldPassportMatcher = Pattern.compile(REGEX_OLD_PASSPORT).matcher(fullRead)

        if (oldPassportMatcher.find()) {
            return processOldPassport(oldPassportMatcher)
        } else {
            val ipPassportMatcherLine1 = Pattern.compile(REGEX_IP_PASSPORT_LINE_1).matcher(fullRead)
            val ipPassportMatcherLine2 = Pattern.compile(REGEX_IP_PASSPORT_LINE_2).matcher(fullRead)

            return if (ipPassportMatcherLine1.find() && ipPassportMatcherLine2.find()) {
                processIPPassport(ipPassportMatcherLine1, ipPassportMatcherLine2)
            } else {
                MRZResult.Failure
            }
        }
    }

    // Function to process old passport format
    private fun processOldPassport(matcher: Matcher): MRZResult {
        val documentNumber = matcher.group(DOCUMENT_NUMBER)
        val checkDigitDocumentNumber = cleanDigit(matcher.group(DIGIT_DOCUMENT_NUMBER)).toInt()
        val dateOfBirth = cleanDigit(matcher.group(BIRTH_DATE))
        val expirationDate = cleanDigit(matcher.group(EXPIRATION_DATE))

        val cleanDocumentNumber = cleanDocumentNumber(documentNumber, checkDigitDocumentNumber)
        return if (cleanDocumentNumber != null) {
            val mrzInfo = MRZ(cleanDocumentNumber, dateOfBirth, expirationDate)
            MRZResult.Success(mrzInfo)
        } else {
            MRZResult.Failure
        }
    }

    // Function to process IP passport format
    private fun processIPPassport(
        matcherLine1: Matcher,
        matcherLine2: Matcher
    ): MRZResult {
        val line1 = matcherLine1.group(0)
        val line2 = matcherLine2.group(0)

        val documentNumber = line1.substring(5, 14)
        val checkDigitDocumentNumber = line1.substring(14, 15).toInt()
        val dateOfBirth = line2.substring(0, 6)
        val expirationDate = line2.substring(8, 14)

        val cleanDocumentNumber = cleanDocumentNumber(documentNumber, checkDigitDocumentNumber)
        return if (cleanDocumentNumber != null) {
            val mrzInfo = MRZ(documentNumber, dateOfBirth, expirationDate)
            MRZResult.Success(mrzInfo)
        } else {
            MRZResult.Failure
        }
    }

    private fun cleanDocumentNumber(documentNumber: String, checkDigit: Int): String? {
        // Replace all 'O' with '0'
        val tempDocumentNumber = documentNumber.replace("O", "0")

        // Calculate check digit of the document number
        var calculatedCheckDigit = checkDigit(tempDocumentNumber)

        // If check digits match, return the document number
        if (checkDigit == calculatedCheckDigit) {
            return tempDocumentNumber
        }

        // Try replacing each '0' with 'O' one at a time to see if it matches the check digit
        tempDocumentNumber.forEachIndexed { index, char ->
            if (char == '0') {
                val modifiedDocumentNumber = tempDocumentNumber.replaceRange(index, index + 1, "O")
                calculatedCheckDigit = checkDigit(modifiedDocumentNumber)
                if (checkDigit == calculatedCheckDigit) {
                    return modifiedDocumentNumber
                }
            }
        }

        // If no match is found, return null
        return null
    }

    /**
     * The check digit for the passport number is a digit calculated from the characters of the passport number, used to verify the accuracy and integrity of the data. This check digit is generated by applying a checksum algorithm (modulo 10) to the characters of the passport number.
     *
     * ### Calculation of the check digit
     * The calculation is done in the following steps:
     * 1. **Assignment of numerical values to characters**:
     *    - Digits from 0 to 9 retain their numerical values (e.g., 0 → 0, 1 → 1, ..., 9 → 9).
     *    - Letters from A to Z are converted to numerical values: A = 10, B = 11, ..., Z = 35.
     * 2. **Multiplication by weights**:
     *    - Each character is multiplied by a cyclic weight of 7, 3, and 1. These weights are applied repetitively.
     * 3. **Sum of results**:
     *    - The obtained products are summed.
     * 4. **Calculation of modulo 10**:
     *    - The total sum is divided by 10, and the remainder of this division is the check digit.
     *
     * ### Detailed Example
     * Let's take an example with the passport number: **L898902C3**.
     * 1. **Conversion of characters to numerical values**:
     *    - L = 21
     *    - 8 = 8
     *    - 9 = 9
     *    - 8 = 8
     *    - 9 = 9
     *    - 0 = 0
     *    - 2 = 2
     *    - C = 12
     *    - 3 = 3
     * 2. **Multiplication by weights (7, 3, 1)**:
     *    - 21 × 7 = 147
     *    - 8 × 3 = 24
     *    - 9 × 1 = 9
     *    - 8 × 7 = 56
     *    - 9 × 3 = 27
     *    - 0 × 1 = 0
     *    - 2 × 7 = 14
     *    - 12 × 3 = 36
     *    - 3 × 1 = 3
     * 3. **Sum of products**:
     *    - 147 + 24 + 9 + 56 + 27 + 0 + 14 + 36 + 3 = 316
     * 4. **Calculation of modulo 10**:
     *    - 316 % 10 = 6
     * So, the check digit for the passport number **L898902C3** is **6**.
     * This check digit is then placed at the next position of the passport number in the Machine Readable Zone (MRZ) to verify the integrity of the data during machine reading.
     */
    private fun checkDigit(passportNumber: String): Int {
        require(passportNumber.length == 9) { "Passport number must be 9 characters long" }

        val weights = intArrayOf(7, 3, 1)
        var sum = 0

        for (i in passportNumber.indices) {
            val char = passportNumber[i]
            val value = if (char.isLetter()) {
                // Convert letters to corresponding numerical values (A = 10, B = 11, ..., Z = 35)
                char.uppercaseChar() - 'A' + 10
            } else {
                // Convert digits to numerical values
                char - '0'
            }
            // Multiply by the appropriate weight (7, 3, 1)
            sum += value * weights[i % 3]
        }
        // Return the remainder of the sum divided by 10
        return sum % 10
    }

    private fun cleanDigit(digit: String): String {
        return digit.replace("I", "1")
            .replace("L", "1")
            .replace("D", "0")
            .replace("S", "5")
            .replace("O", "0")
            .replace("G", "6")
    }
}