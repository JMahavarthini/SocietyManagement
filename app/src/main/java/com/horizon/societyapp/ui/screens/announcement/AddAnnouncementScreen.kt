package com.horizon.societyapp.ui.screens.announcement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.horizon.societyapp.ui.viewmodel.AnnouncementViewModel
import com.horizon.societyapp.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAnnouncementScreen(
    viewModel: AnnouncementViewModel,
    postedByName: String,
    onBack: () -> Unit,
    onPosted: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val postState by viewModel.postState.collectAsState()

    LaunchedEffect(postState) {
        if (postState is Resource.Success) {
            viewModel.resetPostState()
            onPosted()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Announcement") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth().height(160.dp).padding(bottom = 12.dp)
            )

            if (postState is Resource.Error) {
                Text(
                    text = (postState as Resource.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = { viewModel.postAnnouncement(title.trim(), description.trim(), postedByName) },
                enabled = postState !is Resource.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (postState is Resource.Loading) {
                    CircularProgressIndicator(modifier = Modifier.padding(4.dp))
                } else {
                    Text("Post Announcement")
                }
            }
        }
    }
}
