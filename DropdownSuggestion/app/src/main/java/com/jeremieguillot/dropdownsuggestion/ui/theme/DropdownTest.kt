package com.jeremieguillot.dropdownsuggestion.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties

@Composable
fun DropdownTest() {

    var isFocus by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    val suggestions = listOf("PV1", "Old PVSuge2", "PVSuge3", "PV Suge4").filter { it.contains(selectedText) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon =
        if (isFocus) Icons.Filled.KeyboardArrowUp
        else Icons.Filled.ArrowDropDown


    Column {
        OutlinedTextField(value = selectedText,
            onValueChange = { selectedText = it },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isFocus = it.isFocused
                }
//                    ,
                .onGloballyPositioned { coordinates ->
//                    This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            label = { Text("Label") },
            trailingIcon = {
                Icon(icon, "contentDescription", Modifier.clickable { isFocus = !isFocus })
            })
        DropdownMenu(
            expanded = isFocus && selectedText.isNotBlank(),
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            onDismissRequest = { isFocus = false },
            modifier = Modifier.width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(text = { Text(text = label) }, onClick = {
                    isFocus= false
                    selectedText = label
                })
            }
        }
    }
}


@Preview
@Composable
private fun DropdownTestPrev() {
    DropdownTest()
}