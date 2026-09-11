package com.horizon.societyapp.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Announcements : Screen("announcements")
    data object AddAnnouncement : Screen("add_announcement")
    data object Complaints : Screen("complaints")
    data object AddComplaint : Screen("add_complaint")
    data object Directory : Screen("directory")
}
