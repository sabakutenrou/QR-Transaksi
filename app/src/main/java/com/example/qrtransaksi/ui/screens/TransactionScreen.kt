package com.example.qrtransaksi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.qrtransaksi.data.model.Transaction
import com.example.qrtransaksi.ui.state.TransactionState

@Composable
fun TransactionScreen(
    navController: NavHostController,
    state: TransactionState,
    balance: Int?
) {
    var saldo by remember { mutableStateOf(balance) }
    var showHistory by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Saldo saat ini: $saldo", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate(route = "qrscreen/$saldo") },
            modifier = Modifier
//                .fillMaxWidth(0.5f)
                .padding(end = 8.dp)
        ) {
            Text("Scan QR")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { showHistory = !showHistory },
//            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showHistory) "Tutup Riwayat" else "Buka Riwayat")
        }

        if (showHistory) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Riwayat Transaksi:")
            TransactionTable(transaction = state.transactions)

        }
    }
}

@Composable
fun TransactionTable(transaction: List<Transaction>) {
    LazyColumn {
        // Header row
        item {
            TransactionRow("Merchant", "Nominal", isHeader = true)
        }

        // Data rows
        items(transaction) { transaction ->
            TransactionRow(transaction.namaMerchant, transaction.nominal.toString())
        }
    }
}

@Composable
fun TransactionRow(merchant: String, nominal: String, isHeader: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TransactionCell(merchant, isHeader)
        TransactionCell(nominal, isHeader)
    }
}

@Composable
fun TransactionCell(text: String, isHeader: Boolean) {
    Surface(
        modifier = Modifier
            .padding(4.dp),
//            .weight(1f),
//        elevation = if (isHeader) 2.dp else 0.dp
    ) {
        Text(
            text = text,
//            style = if (isHeader) TypographyDefaults.h6.copy(fontWeight = Typography.Bold) else Typography.body1,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewTransactionTable() {
//    val dataList = listOf(
//        TransactionData("2023-07-29", "Merchant A", 100.0),
//        TransactionData("2023-07-30", "Merchant B", 150.0),
//        TransactionData("2023-07-31", "Merchant C", 75.0),
//    )

    MaterialTheme {
//        TransactionTable(dataList = dataList)
    }
}