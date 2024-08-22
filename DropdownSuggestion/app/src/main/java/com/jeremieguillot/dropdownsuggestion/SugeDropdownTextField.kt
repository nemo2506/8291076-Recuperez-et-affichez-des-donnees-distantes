@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.jeremieguillot.dropdownsuggestion

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun <T> SugeDropdownTextField(
    modifier: Modifier = Modifier,
    label: String,
    /** All the available options. */
    options: List<T>,
    /** The selected option. */
    selectedOption: T?,
    /** When the option is selected via tapping on the dropdown option or typing in the option. */
    onOptionSelected: (T?) -> Unit,
    /** Converts [T] to a string for populating the initial text field value. */
    optionToString: (T) -> String,
    /** Returns the filtered options based on the input. This where you need to implement your search. */
    filteredOptions: (searchInput: String) -> List<T>,
    /** Creates the row for the filtered down option in the menu. */
    optionToDropdownRow: @Composable (T) -> Unit = { option ->
        Text(optionToString(option))
    },
    /** Creates the view when [filteredOptions] returns a empty list. */
    noResultsRow: @Composable () -> Unit = {
        // By default, wrap in a menu item to get the same style
        DropdownMenuItem(
            onClick = {},
            text = {
                Text(
                    "No Matches Found",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontStyle = FontStyle.Italic,
                )
            },
        )
    },
    focusRequester: FocusRequester = remember { FocusRequester() },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    bringIntoViewRequester: BringIntoViewRequester = remember { BringIntoViewRequester() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    // Get our text for the selected option
    val selectedOptionText = remember(selectedOption) {
        selectedOption?.let { optionToString(it) }.orEmpty()
    }

    // Default our text input to the selected option
    var textInput by remember(selectedOptionText) {
        mutableStateOf(selectedOptionText)
    }

    var dropDownExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = dropDownExpanded,
        onExpandedChange = { dropDownExpanded = !dropDownExpanded },
        modifier = modifier,
    ) {
        // Text Input
        OutlinedTextField(
            value = textInput,
            onValueChange = {
                // Dropdown may auto hide for scrolling but it's important it always shows when a user
                // does a search
                dropDownExpanded = true
                textInput = it
            },
            modifier = Modifier
                // Match the parent width
                .fillMaxWidth()
                .bringIntoViewRequester(bringIntoViewRequester)
                .menuAnchor()
                .focusRequester(focusRequester)
                .onGloballyPositioned { coordinates ->
//                    This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                }
                .onFocusChanged { focusState ->
                    // When only 1 option left when we lose focus, selected it.
                    if (!focusState.isFocused) {
                        // Whenever we lose focus, always hide the dropdown
                        dropDownExpanded = false

//                        when (filteredOptions.size) {
//                            // Auto select the single option
//                            1 -> if (filteredOptions.first() != selectedOption) {
//                                onOptionSelected(filteredOptions.first())
//                            }
//                            // Nothing to we can auto select - reset our text input to the selected value
//                            else -> textInput = selectedOptionText
//                        }
                    } else {
                        // When focused:
                        // Ensure field is visible by scrolling to it
//                    coroutineScope.launch {
//                        bringIntoViewRequester.bringIntoView()
//                    }
                        // Show the dropdown right away
                        dropDownExpanded = true
                    }
                },
            label = { Text(label) },
        )

        // Dropdown
        if (dropDownExpanded) {
            val dropdownOptions = remember(textInput) {
                if (textInput.isEmpty()) {
                    // Show all options if nothing to filter yet
                    options
                } else {
                    filteredOptions(textInput)
                }
            }

            DropdownMenu(
                expanded = dropDownExpanded,
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                ),
                modifier = Modifier.width(with(LocalDensity.current) { textfieldSize.width.toDp() }),
                onDismissRequest = { dropDownExpanded = false },
            ) {
                if (dropdownOptions.isEmpty()) {
                    noResultsRow()
                } else {
                    dropdownOptions.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                dropDownExpanded = false
                                onOptionSelected(option)
                                focusManager.moveFocus(FocusDirection.Down)
//                                focusManager.clearFocus(force = true)
                            },
                            text = {
                                optionToDropdownRow(option)
                            }
                        )
                    }
                }
            }
        }
    }
}