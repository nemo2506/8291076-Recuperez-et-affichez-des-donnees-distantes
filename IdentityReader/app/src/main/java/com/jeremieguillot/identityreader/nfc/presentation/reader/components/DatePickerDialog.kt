@file:OptIn(ExperimentalMaterial3Api::class)

package com.jeremieguillot.identityreader.nfc.presentation.reader.components

import DateVisualTransformation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jeremieguillot.identityreader.R
import com.jeremieguillot.identityreader.core.domain.MRZ
import com.jeremieguillot.identityreader.core.extension.toDate
import com.jeremieguillot.identityreader.core.extension.toLocaleDateString
import java.util.Calendar
import java.util.Date

@Composable
fun ModifyMRZDialog(
    mrz: MRZ,
    onDismiss: () -> Unit,
    onSave: (documentNumber: String, dateOfBirth: Date, dateOfExpiry: Date) -> Unit
) {
    var documentNumber by remember { mutableStateOf(TextFieldValue(mrz.documentNumber)) }
    var dateOfBirth by remember { mutableStateOf(mrz.dateOfBirth.toDate()) }
    var dateOfExpiry by remember { mutableStateOf(mrz.dateOfExpiry.toDate()) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text("Modification", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = documentNumber,
                    onValueChange = { documentNumber = it },
                    label = { Text(stringResource(R.string.document_number)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                EditTextDatePicker(
                    label = stringResource(R.string.birthdate),
                    initialDate = dateOfBirth,
                    onDateSelected = { dateOfBirth = it }
                )
                EditTextDatePicker(
                    label = stringResource(R.string.expiration_date),
                    initialDate = dateOfExpiry,
                    onDateSelected = { dateOfExpiry = it }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Button(
                        onClick = {
                            onSave(documentNumber.text, dateOfBirth, dateOfExpiry)
                            onDismiss()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}

@Composable
fun EditTextDatePicker(
    label: String,
    initialDate: Date,
    onDateSelected: (Date) -> Unit
) {

    var showDatePicker by remember { mutableStateOf(false) }
    if (showDatePicker) {
        EditDatePickerDialog(initialDate.toInstant().toEpochMilli()) {
            onDateSelected(Date(it))
            showDatePicker = false
        }
    }

    var localTextValue = remember {
        TextFieldValue(initialDate.toLocaleDateString())
    }


    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = localTextValue,
        onValueChange = { localTextValue = it },
        label = { Text(label) },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = {
                showDatePicker = true
            }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.select_date)
                )
            }
        },
        visualTransformation = DateVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun EditDatePickerDialog(
    initialSelectedDateMillis: Long,
    onDateSelected: (Long) -> Unit
) {
    var openDialog by remember { mutableStateOf(true) }
    if (openDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialSelectedDateMillis,
            yearRange = 1920..Calendar.getInstance()
                .get(Calendar.YEAR) + 10, //passport validity period
        )
        DatePickerDialog(
            onDismissRequest = {
                openDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                        datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                    },
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
