package com.horizon.societyapp.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.horizon.societyapp.data.local.entity.UserEntity
import com.horizon.societyapp.ui.viewmodel.AuthViewModel
import com.horizon.societyapp.util.Resource

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: (UserEntity) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState by authViewModel.authState.collectAsState()

    // React to successful login by handing the user object up to the nav graph.
    // LaunchedEffect ensures this fires once per state change, not on every recomposition.
    LaunchedEffect(authState) {
        val state = authState
        if (state is Resource.Success) {
            onLoginSuccess(state.data)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Society App",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Sign in to your community account",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 24.dp, top = 4.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; authViewModel.clearError() },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it; authViewModel.clearError() },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            singleLine = true
        )

        if (authState is Resource.Error) {
            Text(
                text = (authState as Resource.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = { authViewModel.login(email.trim(), password) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            enabled = authState !is Resource.Loading
        ) {
            if (authState is Resource.Loading) {
                CircularProgressIndicator(modifier = Modifier.padding(4.dp))
            } else {
                Text("Login")
            }
        }

        TextButton(
            onClick = onNavigateToRegister,
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
        ) {
            Text("New here? Create an account")
        }
    }
}
