package com.jeremieguillot.identityreader.nfc.presentation.reader.components.documentcard.passport

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremieguillot.identityreader.core.domain.DocumentType
import com.jeremieguillot.identityreader.core.domain.IdentityDocument
import com.jeremieguillot.identityreader.core.domain.toHumanReadableHeight
import com.jeremieguillot.identityreader.nfc.presentation.reader.components.documentcard.identitycard.AddressField
import com.jeremieguillot.identityreader.nfc.presentation.reader.components.documentcard.identitycard.IdentityField

@Composable
fun PassportCard(
    modifier: Modifier = Modifier,
    identityDocument: IdentityDocument,
) {
    ElevatedCard(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth(),
//            .aspectRatio(1.585f),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(2.dp),
    ) {

        Column(modifier = Modifier.padding(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(3f),
                    text = "Passport".uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    color = Color.Red,
                )
                Row(
                    modifier = Modifier
                        .weight(7f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IdentityField("Type", "P")
                    IdentityField("Code du pays", identityDocument.issuingIsO3Country)
                    IdentityField("N° DU DOCUMENT", identityDocument.documentNumber)

                }
//                Image(
//                    painter = painterResource(id = R.drawable.europe_flag),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .align(
//                            Alignment.TopEnd
//                        )
//                        .height(20.dp)
//                )
            }
            Row {
                Icon(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxHeight(),
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.LightGray
                )
                Column(
                    modifier = Modifier
                        .weight(7f)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Name Row
                    IdentityField("NOM", identityDocument.lastName.uppercase())
                    IdentityField("Prénoms", identityDocument.firstName)


                    // Nationality, Gender, Birthdate Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IdentityField("NATIONALITÉ", identityDocument.nationality)
                        IdentityField("SEXE", identityDocument.gender)
                        IdentityField("TAILLE", identityDocument.size.toHumanReadableHeight())

                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IdentityField(
                            "DATE DE NAISS.",
                            identityDocument.birthDate
                        )
                        IdentityField("LIEU DE NAISSANCE", identityDocument.placeOfBirth)

                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            IdentityField("DATE DE DELIVRANCE", identityDocument.expirationDate)
                            IdentityField(
                                "DATE D'EXPIR",
                                identityDocument.expirationDate
                            )
                        }

                        AddressField(
                            "DOMICILE",
                            listOf(
                                "${identityDocument.addressNumber} ${identityDocument.address}",
                                "${identityDocument.postalCode} ${identityDocument.city}",
                                identityDocument.country
                            )
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun IdentityCardPreview() {
    val document = IdentityDocument(
        type = DocumentType.ID_CARD,
        documentNumber = "13A000026",
        issuingIsO3Country = "FRA",
        lastName = "Doe",
        firstName = "John, Peter, Maxwell",
        nationality = "French",
        gender = "M",
        birthDate = "03/04/1982",
        expirationDate = "23/12/2045",
        placeOfBirth = "Strasbourg",
        addressNumber = "123",
        address = "Main Street",
        postalCode = "75001",
        city = "Paris",
        country = "France"
    )

    Column(modifier = Modifier.padding(16.dp)) {
        PassportCard(identityDocument = document)
    }
}

@Preview(showBackground = true)
@Composable
fun IdentityCardPreviewWithMissingData() {
    val document = IdentityDocument(
        type = DocumentType.ID_CARD,
        documentNumber = "13A000026",
        issuingIsO3Country = "FRA",
        lastName = "Doe",
        firstName = "John, Peter, Maxwell, Alexander",
        nationality = "",  // Missing data
        gender = "M",
        placeOfBirth = "",
        birthDate = "03/04/1982",
        expirationDate = "23/12/2045",
        addressNumber = "",  // Missing data
        address = "",  // Missing data
        postalCode = "",  // Missing data
        city = "Paris",
        country = ""  // Missing data
    )
    Column(modifier = Modifier.padding(16.dp)) {
        PassportCard(identityDocument = document)
    }
}
