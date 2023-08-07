package com.example.qrtransaksi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.qrtransaksi.data.room.TransactionDatabase
import com.example.qrtransaksi.ui.nav.NavigationArguments
import com.example.qrtransaksi.ui.theme.QRTransaksiTheme
import com.example.qrtransaksi.ui.viewmodel.TransactionViewModel

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TransactionDatabase::class.java,
            "transaction.db"
        ).build()
    }

    private val viewModel by viewModels<TransactionViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TransactionViewModel(db.dao) as T
                }
            }
        }
    )

    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRTransaksiTheme {
                navController = rememberNavController()
                val state by viewModel.state.collectAsState()
                
                NavigationArguments(
                    navController = navController,
                    state = state,
                    onEvent = viewModel::onEvent
                )

            }
        }
    }
}