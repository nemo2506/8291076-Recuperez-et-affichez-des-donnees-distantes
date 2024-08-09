package com.jeremieguillot.designsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeremieguillot.designsystem.ui.theme.DesignSystemTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DesignSystemTheme {
                DesignSystemScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignSystemScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Design System") }
            )
        }
    ) {

        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            item {
                // Buttons
                Text("Buttons")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {}) { Text("Button") }
                    OutlinedButton(onClick = {}) { Text("Outlined Button") }
                    TextButton(onClick = {}) { Text("Text Button") }
                }

                // Text Fields
                Text("Text Fields")
                var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
                TextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    label = { Text("Text Field") })
                var basicTextFieldValue by remember { mutableStateOf("") }
                BasicTextField(
                    value = basicTextFieldValue,
                    onValueChange = { basicTextFieldValue = it })

                // Cards
                Text("Cards")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Card Title", style = MaterialTheme.typography.titleLarge)
                        Text("Card content goes here.")
                    }
                }

                // Sliders
                Text("Sliders")
                var sliderPosition by remember { mutableStateOf(0f) }
                Slider(value = sliderPosition, onValueChange = { sliderPosition = it })

                // Switches
                Text("Switches")
                var switchState by remember { mutableStateOf(true) }
                Switch(checked = switchState, onCheckedChange = { switchState = it })

                // Checkboxes
                Text("Checkboxes")
                var checkboxState by remember { mutableStateOf(true) }
                Checkbox(checked = checkboxState, onCheckedChange = { checkboxState = it })

                // RadioButtons
                Text("Radio Buttons")
                var selectedOption by remember { mutableStateOf("Option 1") }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedOption == "Option 1",
                            onClick = { selectedOption = "Option 1" }
                        )
                        Text("Option 1")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedOption == "Option 2",
                            onClick = { selectedOption = "Option 2" }
                        )
                        Text("Option 2")
                    }
                }

                // Lists
                Text("Lists")
                val items = listOf("Item 1", "Item 2", "Item 3")
                items.forEach { item ->
                    Text(item)
                }

                // Dialogs
                Text("Dialogs")
                var showDialog by remember { mutableStateOf(false) }
                Button(onClick = { showDialog = true }) {
                    Text("Show Dialog")
                }
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Dialog Title") },
                        text = { Text("This is a dialog.") },
                        confirmButton = {
                            Button(onClick = { showDialog = false }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DesignSystemScreenPreview() {
    DesignSystemTheme {
        DesignSystemScreen()
    }
}
