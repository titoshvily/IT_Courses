package com.titoshvily.it_courses.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Courses : BottomNavItem("courses", "Главная", Icons.Default.Home)
    object Favorites : BottomNavItem("favorites", "Избранное", Icons.Default.Bookmark)
    object Account : BottomNavItem("account", "Аккаунт", Icons.Default.Person)
}
