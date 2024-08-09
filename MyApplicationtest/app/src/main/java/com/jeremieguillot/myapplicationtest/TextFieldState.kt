@file:OptIn(ExperimentalFoundationApi::class)

package com.jeremieguillot.myapplicationtest

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.TextFieldDecorator
import androidx.compose.foundation.text2.input.InputTransformation
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*

class DefaultTextFieldDecorator(
    private val hint: String,
    private val errorInfo: String?,
    private val displayClearButton: Boolean,
    private val value: TextFieldState,
    private val internalFocus: FocusRequester
) : androidx.compose.foundation.text2.TextFieldDecorator {
    @Composable
    override fun Decoration(innerTextField: @Composable () -> Unit) {
        Column {
            Row(
                modifier = Modifier
                    .height(80.dp)
                    .padding(vertical = 8.dp, horizontal = 2.dp)
                    .fillMaxWidth()
                    .border(1.dp, Color.Black),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.text.isEmpty()) {
                        Text(text = hint, color = Color.Gray)
                    }
                    innerTextField()
                }

                if (value.text.isNotEmpty() && displayClearButton) {
                    IconButton(onClick = {
                        value.clearText()
                        internalFocus.requestFocus()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null,
                        )
                    }
                }
            }

            if (errorInfo != null) {
                Text(text = errorInfo, color = Color.Red)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomBasicTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    inputTransformation: InputTransformation? = null,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(Color.Black),
//    outputTransformation: OutputTransformation? = null,
    decorator: TextFieldDecorator? = null,
    scrollState: ScrollState = rememberScrollState(),
) {
    val focusManager = LocalFocusManager.current
    val internalFocus = remember { FocusRequester() }

    BasicTextField2(
        state = state,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource ?: remember { MutableInteractionSource() },
        cursorBrush = cursorBrush,
        decorator = decorator,
        modifier = modifier
            .focusRequester(internalFocus)
            .fillMaxWidth()
    )
}

// Example usage
@Composable
fun SampleUsage() {
    val textFieldState = remember { TextFieldState() }

    CustomBasicTextField(
        state = textFieldState,
        decorator = DefaultTextFieldDecorator(
            hint = "Hint text",
            errorInfo = "Error message",
            displayClearButton = true,
            value = textFieldState,
            internalFocus = FocusRequester()
        ),
    )

    SuggestionChip(
        onClick = { textFieldState.edit { append("gmail.com") } },
        label = { Text("gmail.com") })

}
