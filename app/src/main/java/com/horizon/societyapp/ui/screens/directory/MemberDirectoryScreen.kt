package com.horizon.societyapp.ui.screens.directory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.horizon.societyapp.data.local.entity.UserEntity
import com.horizon.societyapp.data.model.Role
import com.horizon.societyapp.ui.components.EmptyState
import com.horizon.societyapp.ui.components.ErrorState
import com.horizon.societyapp.ui.components.LoadingState
import com.horizon.societyapp.ui.viewmodel.DirectoryViewModel
import com.horizon.societyapp.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDirectoryScreen(
    viewModel: DirectoryViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.members.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Member Directory") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when (val s = state) {
            is Resource.Loading -> LoadingState(Modifier.padding(padding))
            is Resource.Empty -> EmptyState(
                message = "No members registered yet.",
                modifier = Modifier.padding(padding)
            )
            is Resource.Error -> ErrorState(message = s.message, modifier = Modifier.padding(padding))
            is Resource.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(s.data, key = { it.id }) { member ->
                        MemberRow(member)
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberRow(member: UserEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = member.name.take(1).uppercase(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(text = member.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Flat ${member.flatNumber} · ${if (member.role == Role.ADMIN) "Admin" else "Resident"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(text = member.phone, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
