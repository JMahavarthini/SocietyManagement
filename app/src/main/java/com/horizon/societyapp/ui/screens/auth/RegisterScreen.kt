package com.horizon.societyapp.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.horizon.societyapp.data.local.entity.UserEntity
import com.horizon.societyapp.data.model.Role
import com.horizon.societyapp.ui.viewmodel.AuthViewModel
import com.horizon.societyapp.util.Resource

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: (UserEntity) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var flatNumber by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(Role.RESIDENT) }

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        val state = authState
        if (state is Resource.Success) {
            onRegisterSuccess(state.data)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(text = "Create Account", style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Join your residential community",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 20.dp, top = 4.dp)
        )

        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = flatNumber, onValueChange = { flatNumber = it },
            label = { Text("Flat / Unit Number") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = phone, onValueChange = { phone = it },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true
        )

        Text(text = "I am registering as:", style = MaterialTheme.typography.bodyMedium)
        Row(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)) {
            FilterChip(
                selected = role == Role.RESIDENT,
                onClick = { role = Role.RESIDENT },
                label = { Text("Resident") },
                modifier = Modifier.padding(end = 8.dp)
            )
            FilterChip(
                selected = role == Role.ADMIN,
                onClick = { role = Role.ADMIN },
                label = { Text("Admin") }
            )
        }

        if (authState is Resource.Error) {
            Text(
                text = (authState as Resource.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                authViewModel.register(
                    name = name.trim(),
                    email = email.trim(),
                    password = password,
                    flatNumber = flatNumber.trim(),
                    phone = phone.trim(),
                    role = role
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is Resource.Loading
        ) {
            if (authState is Resource.Loading) {
                CircularProgressIndicator(modifier = Modifier.padding(4.dp))
            } else {
                Text("Register")
            }
        }

        TextButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
        ) {
            Text("Already have an account? Login")
        }
    }
}
