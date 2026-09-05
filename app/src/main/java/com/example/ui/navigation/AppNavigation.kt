package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.model.UserRole
import com.example.data.repository.CallAndGoRepository
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.auth.OnboardingScreen
import com.example.ui.screens.auth.OtpVerificationScreen
import com.example.ui.screens.auth.SplashScreen
import com.example.ui.screens.driver.DriverEarningsScreen
import com.example.ui.screens.driver.DriverHomeScreen
import com.example.ui.screens.driver.DriverKycScreen
import com.example.ui.screens.rider.RiderHomeScreen
import com.example.ui.screens.rider.RiderProfileScreen
import com.example.ui.screens.rider.TripHistoryScreen

object NavDestinations {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val OTP = "otp"
    const val RIDER_HOME = "rider_home"
    const val RIDER_HISTORY = "rider_history"
    const val RIDER_PROFILE = "rider_profile"
    const val DRIVER_HOME = "driver_home"
    const val DRIVER_EARNINGS = "driver_earnings"
    const val DRIVER_KYC = "driver_kyc"
    const val ADMIN_DASHBOARD = "admin_dashboard"
}

@Composable
fun AppNavigation(repository: CallAndGoRepository) {
    val navController = rememberNavController()
    val language by repository.currentLanguage.collectAsState()
    var targetPhoneForOtp by remember { mutableStateOf("01712345678") }

    NavHost(
        navController = navController,
        startDestination = NavDestinations.SPLASH
    ) {
        // 1. Splash Screen
        composable(NavDestinations.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(NavDestinations.ONBOARDING) {
                        popUpTo(NavDestinations.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // 2. Onboarding Screen
        composable(NavDestinations.ONBOARDING) {
            OnboardingScreen(
                language = language,
                onComplete = {
                    navController.navigate(NavDestinations.LOGIN) {
                        popUpTo(NavDestinations.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        // 3. Login / Auth Screen
        composable(NavDestinations.LOGIN) {
            LoginScreen(
                language = language,
                repository = repository,
                onLoginSuccess = { role ->
                    repository.setActiveRole(role)
                    val destination = when (role) {
                        UserRole.RIDER -> NavDestinations.RIDER_HOME
                        UserRole.DRIVER -> NavDestinations.DRIVER_HOME
                        UserRole.ADMIN -> NavDestinations.ADMIN_DASHBOARD
                    }
                    navController.navigate(destination) {
                        popUpTo(NavDestinations.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToOtp = { phone ->
                    targetPhoneForOtp = phone
                    navController.navigate(NavDestinations.OTP)
                }
            )
        }

        // 4. OTP Verification Screen
        composable(NavDestinations.OTP) {
            OtpVerificationScreen(
                phoneNumber = targetPhoneForOtp,
                language = language,
                repository = repository,
                onVerified = {
                    val destination = when (repository.activeRole.value) {
                        UserRole.RIDER -> NavDestinations.RIDER_HOME
                        UserRole.DRIVER -> NavDestinations.DRIVER_HOME
                        UserRole.ADMIN -> NavDestinations.ADMIN_DASHBOARD
                    }
                    navController.navigate(destination) {
                        popUpTo(NavDestinations.LOGIN) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // 5. Rider Screens
        composable(NavDestinations.RIDER_HOME) {
            RiderHomeScreen(
                repository = repository,
                onNavigateToHistory = { navController.navigate(NavDestinations.RIDER_HISTORY) },
                onNavigateToProfile = { navController.navigate(NavDestinations.RIDER_PROFILE) },
                onSwitchRole = { newRole ->
                    repository.setActiveRole(newRole)
                    when (newRole) {
                        UserRole.DRIVER -> navController.navigate(NavDestinations.DRIVER_HOME)
                        UserRole.ADMIN -> navController.navigate(NavDestinations.ADMIN_DASHBOARD)
                        UserRole.RIDER -> {}
                    }
                }
            )
        }

        composable(NavDestinations.RIDER_HISTORY) {
            TripHistoryScreen(
                repository = repository,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavDestinations.RIDER_PROFILE) {
            RiderProfileScreen(
                repository = repository,
                onBack = { navController.popBackStack() },
                onSwitchRole = { newRole ->
                    repository.setActiveRole(newRole)
                    when (newRole) {
                        UserRole.DRIVER -> navController.navigate(NavDestinations.DRIVER_HOME)
                        UserRole.ADMIN -> navController.navigate(NavDestinations.ADMIN_DASHBOARD)
                        UserRole.RIDER -> {}
                    }
                }
            )
        }

        // 6. Driver Partner Screens
        composable(NavDestinations.DRIVER_HOME) {
            DriverHomeScreen(
                repository = repository,
                onNavigateToEarnings = { navController.navigate(NavDestinations.DRIVER_EARNINGS) },
                onNavigateToKyc = { navController.navigate(NavDestinations.DRIVER_KYC) },
                onSwitchRole = { newRole ->
                    repository.setActiveRole(newRole)
                    when (newRole) {
                        UserRole.RIDER -> navController.navigate(NavDestinations.RIDER_HOME)
                        UserRole.ADMIN -> navController.navigate(NavDestinations.ADMIN_DASHBOARD)
                        UserRole.DRIVER -> {}
                    }
                }
            )
        }

        composable(NavDestinations.DRIVER_EARNINGS) {
            DriverEarningsScreen(
                repository = repository,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavDestinations.DRIVER_KYC) {
            DriverKycScreen(
                repository = repository,
                onBack = { navController.popBackStack() }
            )
        }

        // 7. Admin Dashboard Screen
        composable(NavDestinations.ADMIN_DASHBOARD) {
            AdminDashboardScreen(
                repository = repository,
                onBack = { navController.popBackStack() },
                onSwitchRole = { newRole ->
                    repository.setActiveRole(newRole)
                    when (newRole) {
                        UserRole.RIDER -> navController.navigate(NavDestinations.RIDER_HOME)
                        UserRole.DRIVER -> navController.navigate(NavDestinations.DRIVER_HOME)
                        UserRole.ADMIN -> {}
                    }
                }
            )
        }
    }
}
