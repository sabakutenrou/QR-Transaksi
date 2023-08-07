package com.example.qrtransaksi.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.qrtransaksi.ui.screens.QrCodeScreen
import com.example.qrtransaksi.data.TransactionEvent
import com.example.qrtransaksi.ui.screens.TransactionScreen
import com.example.qrtransaksi.ui.state.TransactionState

@Composable
fun NavigationArguments(
    navController: NavHostController,
    state: TransactionState,
    onEvent: (TransactionEvent) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = "mainscreen"
    ) {
        composable("mainscreen") {
            TransactionScreen(
                navController = navController,
                state = state,
                balance = 1000000
            )
        }
        composable(
            route = "mainscreen/{balance}",
            arguments = listOf(navArgument("balance") {
                type = NavType.IntType
            })
        ) {navBackStack ->
            val balance = navBackStack.arguments?.getInt("balance")
            TransactionScreen(
                navController = navController,
                state = state,
                balance = balance
            )
        }
        composable(
            route = "qrscreen/{balance}",
            arguments = listOf(navArgument("balance") {
                type = NavType.IntType
            })
        ) {navBackStack ->
            val balance = navBackStack.arguments?.getInt("balance")
            QrCodeScreen(
                navController = navController,
                state = state,
                onEvent = onEvent,
                balance = balance
            )
        }
    }


}