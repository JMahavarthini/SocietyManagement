package com.horizon.societyapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.horizon.societyapp.SocietyApplication
import com.horizon.societyapp.ui.screens.announcement.AddAnnouncementScreen
import com.horizon.societyapp.ui.screens.announcement.AnnouncementListScreen
import com.horizon.societyapp.ui.screens.auth.LoginScreen
import com.horizon.societyapp.ui.screens.auth.RegisterScreen
import com.horizon.societyapp.ui.screens.complaint.AddComplaintScreen
import com.horizon.societyapp.ui.screens.complaint.ComplaintListScreen
import com.horizon.societyapp.ui.screens.directory.MemberDirectoryScreen
import com.horizon.societyapp.ui.screens.home.HomeScreen
import com.horizon.societyapp.ui.viewmodel.AnnouncementViewModel
import com.horizon.societyapp.ui.viewmodel.AuthViewModel
import com.horizon.societyapp.ui.viewmodel.ComplaintViewModel
import com.horizon.societyapp.ui.viewmodel.DirectoryViewModel
import com.horizon.societyapp.ui.viewmodel.ViewModelFactory

@Composable
fun SocietyNavGraph(application: SocietyApplication) {
    val navController: NavHostController = rememberNavController()

    val factory = remember {
        ViewModelFactory(
            authRepository = application.authRepository,
            announcementRepository = application.announcementRepository,
            complaintRepository = application.complaintRepository,
            sessionManager = application.sessionManager
        )
    }

    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val announcementViewModel: AnnouncementViewModel = viewModel(factory = factory)
    val complaintViewModel: ComplaintViewModel = viewModel(factory = factory)
    val directoryViewModel: DirectoryViewModel = viewModel(factory = factory)

    // Restore a previously saved session once, at app start, so users don't
    // have to log in again every time they reopen the app.
    LaunchedEffect(Unit) {
        authViewModel.restoreSession()
    }
    val currentUser by authViewModel.currentUser.collectAsState()

    val startDestination = Screen.Login.route

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            val user = currentUser
            if (user == null) {
                // Session not resolved yet or logged out - send back to login.
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            } else {
                HomeScreen(
                    currentUser = user,
                    onNavigateToAnnouncements = { navController.navigate(Screen.Announcements.route) },
                    onNavigateToComplaints = { navController.navigate(Screen.Complaints.route) },
                    onNavigateToDirectory = { navController.navigate(Screen.Directory.route) },
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Screen.Announcements.route) {
            val user = currentUser
            if (user != null) {
                AnnouncementListScreen(
                    viewModel = announcementViewModel,
                    currentUserRole = user.role,
                    onBack = { navController.popBackStack() },
                    onAddAnnouncement = { navController.navigate(Screen.AddAnnouncement.route) }
                )
            }
        }

        composable(Screen.AddAnnouncement.route) {
            val user = currentUser
            if (user != null) {
                AddAnnouncementScreen(
                    viewModel = announcementViewModel,
                    postedByName = user.name,
                    onBack = { navController.popBackStack() },
                    onPosted = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.Complaints.route) {
            val user = currentUser
            if (user != null) {
                ComplaintListScreen(
                    viewModel = complaintViewModel,
                    currentUserRole = user.role,
                    currentUserFlat = user.flatNumber,
                    onBack = { navController.popBackStack() },
                    onAddComplaint = { navController.navigate(Screen.AddComplaint.route) }
                )
            }
        }

        composable(Screen.AddComplaint.route) {
            val user = currentUser
            if (user != null) {
                AddComplaintScreen(
                    viewModel = complaintViewModel,
                    raisedByName = user.name,
                    raisedByFlat = user.flatNumber,
                    onBack = { navController.popBackStack() },
                    onSubmitted = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.Directory.route) {
            MemberDirectoryScreen(
                viewModel = directoryViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
