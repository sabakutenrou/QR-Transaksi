package com.example.qrtransaksi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val idProses: Int = 0,
    val idTransaksi: String,
    val bankSumber: String,
    val namaMerchant: String,
    val nominal: Int
)
