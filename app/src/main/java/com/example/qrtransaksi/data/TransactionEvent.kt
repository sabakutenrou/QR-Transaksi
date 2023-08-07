package com.example.qrtransaksi.data

sealed interface TransactionEvent {
    object SaveTransaction: TransactionEvent
    data class SetIdTransaksi(val id: String): TransactionEvent
    data class SetBank(val bank: String): TransactionEvent
    data class SetMerchant(val merchant: String): TransactionEvent
    data class SetNominal(val nominal: Int): TransactionEvent
}