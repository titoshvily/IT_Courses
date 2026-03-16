package com.titoshvily.it_courses.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.titoshvily.it_courses.feature.account.AccountScreen
import com.titoshvily.it_courses.feature.courses.presentation.courses.CoursesScreen
import com.titoshvily.it_courses.feature.courses.presentation.detail.CourseDetailScreen
import com.titoshvily.it_courses.feature.courses.presentation.favorites.FavoritesScreen
import com.titoshvily.it_courses.feature.login.presentation.LoginScreen

@Composable
fun AppNavHost() {
    val rootNavController = rememberNavController()

    NavHost(navController = rootNavController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    rootNavController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            MainScreen(
                onCourseClick = { id ->
                    rootNavController.navigate("course/$id")
                }
            )
        }
        composable(
            route = "course/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStack ->
            val id = backStack.arguments?.getInt("id") ?: return@composable
            CourseDetailScreen(
                courseId = id,
                onBack = { rootNavController.popBackStack() }
            )
        }
    }
}

@Composable
private fun MainScreen(onCourseClick: (Int) -> Unit = {}) {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Courses,
        BottomNavItem.Favorites,
        BottomNavItem.Account
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF1C1C1E)) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF4CAF50),
                            selectedTextColor = Color(0xFF4CAF50),
                            unselectedIconColor = Color(0xFF8E8E93),
                            unselectedTextColor = Color(0xFF8E8E93),
                            indicatorColor = Color(0xFF2C2C2E)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Courses.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Courses.route) { CoursesScreen(onCourseClick = onCourseClick) }
            composable(BottomNavItem.Favorites.route) { FavoritesScreen(onCourseClick = onCourseClick) }
            composable(BottomNavItem.Account.route) { AccountScreen() }
        }
    }
}
