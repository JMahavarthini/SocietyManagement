package com.horizon.societyapp.ui.screens.complaint

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.horizon.societyapp.data.local.entity.ComplaintEntity
import com.horizon.societyapp.data.model.ComplaintStatus
import com.horizon.societyapp.data.model.Role
import com.horizon.societyapp.ui.components.EmptyState
import com.horizon.societyapp.ui.components.ErrorState
import com.horizon.societyapp.ui.components.LoadingState
import com.horizon.societyapp.ui.theme.ErrorRed
import com.horizon.societyapp.ui.theme.SuccessGreen
import com.horizon.societyapp.ui.theme.WarningAmber
import com.horizon.societyapp.ui.viewmodel.ComplaintViewModel
import com.horizon.societyapp.util.Resource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintListScreen(
    viewModel: ComplaintViewModel,
    currentUserRole: Role,
    currentUserFlat: String,
    onBack: () -> Unit,
    onAddComplaint: () -> Unit
) {
    val state by viewModel.complaints.collectAsState()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.loadComplaints(isAdmin = currentUserRole == Role.ADMIN, flatNumber = currentUserFlat)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (currentUserRole == Role.ADMIN) "All Complaints" else "My Complaints") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (currentUserRole == Role.RESIDENT) {
                FloatingActionButton(onClick = onAddComplaint) {
                    Icon(Icons.Filled.Add, contentDescription = "Raise complaint")
                }
            }
        }
    ) { padding ->
        when (val s = state) {
            is Resource.Loading -> LoadingState(Modifier.padding(padding))
            is Resource.Empty -> EmptyState(
                message = if (currentUserRole == Role.ADMIN)
                    "No complaints have been raised yet."
                else
                    "You haven't raised any complaints. Tap + to raise one.",
                modifier = Modifier.padding(padding)
            )
            is Resource.Error -> ErrorState(
                message = s.message,
                onRetry = { viewModel.loadComplaints(currentUserRole == Role.ADMIN, currentUserFlat) },
                modifier = Modifier.padding(padding)
            )
            is Resource.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(s.data, key = { it.id }) { complaint ->
                        ComplaintCard(
                            complaint = complaint,
                            isAdmin = currentUserRole == Role.ADMIN,
                            onStatusChange = { newStatus -> viewModel.updateStatus(complaint, newStatus) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ComplaintCard(
    complaint: ComplaintEntity,
    isAdmin: Boolean,
    onStatusChange: (ComplaintStatus) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = complaint.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                StatusChip(complaint.status)
            }
            Text(
                text = complaint.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
            )
            Text(
                text = "${complaint.category} · Flat ${complaint.raisedByFlat} · ${formatDate(complaint.timestamp)}",
                style = MaterialTheme.typography.labelSmall
            )
            if (isAdmin) {
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    AssistChip(
                        onClick = { menuExpanded = true },
                        label = { Text("Update status") }
                    )
                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        ComplaintStatus.entries.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.displayName) },
                                onClick = {
                                    onStatusChange(status)
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusChip(status: ComplaintStatus) {
    val color = when (status) {
        ComplaintStatus.OPEN -> ErrorRed
        ComplaintStatus.IN_PROGRESS -> WarningAmber
        ComplaintStatus.RESOLVED -> SuccessGreen
    }
    AssistChip(
        onClick = {},
        label = { Text(status.displayName) },
        colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
            labelColor = color
        )
    )
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
