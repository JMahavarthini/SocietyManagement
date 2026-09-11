package com.horizon.societyapp.ui.screens.announcement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.horizon.societyapp.data.local.entity.AnnouncementEntity
import com.horizon.societyapp.data.model.Role
import com.horizon.societyapp.ui.components.EmptyState
import com.horizon.societyapp.ui.components.ErrorState
import com.horizon.societyapp.ui.components.LoadingState
import com.horizon.societyapp.ui.viewmodel.AnnouncementViewModel
import com.horizon.societyapp.util.Resource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementListScreen(
    viewModel: AnnouncementViewModel,
    currentUserRole: Role,
    onBack: () -> Unit,
    onAddAnnouncement: () -> Unit
) {
    val state by viewModel.announcements.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notice Board") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (currentUserRole == Role.ADMIN) {
                FloatingActionButton(onClick = onAddAnnouncement) {
                    Icon(Icons.Filled.Add, contentDescription = "Post announcement")
                }
            }
        }
    ) { padding ->
        when (val s = state) {
            is Resource.Loading -> LoadingState(Modifier.padding(padding))
            is Resource.Empty -> EmptyState(
                message = "No announcements yet. Check back later.",
                modifier = Modifier.padding(padding)
            )
            is Resource.Error -> ErrorState(
                message = s.message,
                onRetry = { viewModel.loadAnnouncements() },
                modifier = Modifier.padding(padding)
            )
            is Resource.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(s.data, key = { it.id }) { announcement ->
                        AnnouncementCard(
                            announcement = announcement,
                            canDelete = currentUserRole == Role.ADMIN,
                            onDelete = { viewModel.deleteAnnouncement(announcement) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnnouncementCard(
    announcement: AnnouncementEntity,
    canDelete: Boolean,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = announcement.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = announcement.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 6.dp, bottom = 8.dp)
            )
            Text(
                text = "Posted by ${announcement.postedBy} · ${formatDate(announcement.timestamp)}",
                style = MaterialTheme.typography.labelSmall
            )
            if (canDelete) {
                IconButton(onClick = onDelete, modifier = Modifier.padding(top = 4.dp)) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete announcement")
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
