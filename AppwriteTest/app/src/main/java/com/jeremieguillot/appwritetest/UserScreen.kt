package com.jeremieguillot.appwritetest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import io.appwrite.models.User
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    user: MutableState<User<Map<String, Any>>?>,
    accountService: AccountService
) {
    val coroutineScope = rememberCoroutineScope()
    var error by remember { mutableStateOf<String?>(null) }

    fun onLogin(email: String, password: String) {
        coroutineScope.launch {
            error=""
            user.value = accountService.login(email, password)
        }
        error = if (user.value === null) {
            "Unable to login"
        } else {
            null
        }
    }

    fun onRegister(email: String, password: String) {
        error=""
        coroutineScope.launch {
            user.value = accountService.register(email, password)
        }
        error = if (user.value === null) {
            "Unable to register"
        } else {
            null
        }
    }

    fun onLogout() {
        coroutineScope.launch {
            accountService.logout()
            user.value = null
        }
    }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (user.value !== null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Logged in as ${user.value!!.email}")
            Button(onClick = { onLogout() }) {
                Text("Logout")
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Email") }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { onLogin(username, password) }) {
                Text("Login")
            }
            Button(onClick = { onRegister(username, password) }) {
                Text("Register")
            }
        }
        if (error !== null) {
            Text(
                text = error!!,
                modifier = Modifier.padding(16.dp),
                color = androidx.compose.ui.graphics.Color.Red
            )
        }
    }
}