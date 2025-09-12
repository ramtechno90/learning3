package com.example.restaurantmanager.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import com.example.restaurantmanager.ui.admin.screen.AdminLoginScreen
import com.example.restaurantmanager.ui.admin.screen.AdminOrderDashboardScreen
import com.example.restaurantmanager.ui.admin.screen.MenuManagementScreen
import com.example.restaurantmanager.ui.customer.screen.CartScreen
import com.example.restaurantmanager.ui.customer.screen.MenuScreen
import com.example.restaurantmanager.ui.customer.screen.OrderConfirmationScreen
import com.example.restaurantmanager.ui.customer.screen.ThankYouScreen
import com.example.restaurantmanager.ui.customer.viewmodel.CartViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

sealed class Screen(val route: String) {
    object CustomerFlow : Screen("customer_flow")
    object Menu : Screen("menu")
    object Cart : Screen("cart")
    object OrderConfirmation : Screen("order_confirmation")
    object ThankYou : Screen("thank_you/{orderId}") {
        fun createRoute(orderId: Int) = "thank_you/$orderId"
    }

    object AdminLogin : Screen("admin_login")
    object AdminFlow : Screen("admin_flow")
    object AdminDashboard : Screen("admin_dashboard")
    object MenuManagement : Screen("menu_management")
}

@OptIn(com.google.accompanist.navigation.animation.ExperimentalAnimationApi::class)
@Composable
fun AppNavigator() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.CustomerFlow.route,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        navigation(startDestination = Screen.Menu.route, route = Screen.CustomerFlow.route) {
            composable(Screen.Menu.route) {
                val cartViewModel: CartViewModel = hiltViewModel(
                    remember(it) {
                        navController.getBackStackEntry(Screen.CustomerFlow.route)
                    }
                )
                MenuScreen(
                    cartViewModel = cartViewModel,
                    onNavigateToCart = { navController.navigate(Screen.Cart.route) },
                    onNavigateToAdmin = { navController.navigate(Screen.AdminLogin.route) }
                )
            }
            composable(Screen.Cart.route) {
                val cartViewModel: CartViewModel = hiltViewModel(
                    remember(it) {
                        navController.getBackStackEntry(Screen.CustomerFlow.route)
                    }
                )
                CartScreen(
                    cartViewModel = cartViewModel,
                    onNavigateToCheckout = { navController.navigate(Screen.OrderConfirmation.route) }
                )
            }
            composable(Screen.OrderConfirmation.route) {
                val cartViewModel: CartViewModel = hiltViewModel(
                    remember(it) {
                        navController.getBackStackEntry(Screen.CustomerFlow.route)
                    }
                )
                OrderConfirmationScreen(
                    cartViewModel = cartViewModel,
                    onPlaceOrder = { orderId ->
                        navController.navigate(Screen.ThankYou.createRoute(orderId)) {
                            popUpTo(Screen.CustomerFlow.route)
                        }
                    }
                )
            }
            composable(
                Screen.ThankYou.route,
                arguments = listOf(navArgument("orderId") { type = NavType.IntType })
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
                ThankYouScreen(orderId = orderId)
            }
        }
        composable(Screen.AdminLogin.route) {
            AdminLoginScreen(onLoginClick = { navController.navigate(Screen.AdminFlow.route) })
        }
        navigation(startDestination = Screen.AdminDashboard.route, route = Screen.AdminFlow.route) {
            composable(Screen.AdminDashboard.route) {
                AdminOrderDashboardScreen(
                    onNavigateToMenuManagement = { navController.navigate(Screen.MenuManagement.route) },
                    onLogout = {
                        navController.navigate(Screen.CustomerFlow.route) {
                            popUpTo(Screen.AdminFlow.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.MenuManagement.route) {
                MenuManagementScreen()
            }
        }
    }
}
