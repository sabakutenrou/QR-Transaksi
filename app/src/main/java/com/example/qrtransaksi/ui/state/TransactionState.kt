package com.example.qrtransaksi.ui.state

import com.example.qrtransaksi.data.model.Transaction

data class TransactionState(
    val transactions: List<Transaction> = emptyList(),
    val id: String = "",
    val bank: String = "",
    val merchant: String = "",
    val nominal: Int = 0

)
