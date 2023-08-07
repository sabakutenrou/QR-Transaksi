package com.example.qrtransaksi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrtransaksi.data.model.Transaction
import com.example.qrtransaksi.data.room.TransactionDao
import com.example.qrtransaksi.data.TransactionEvent
import com.example.qrtransaksi.ui.state.TransactionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val dao: TransactionDao
): ViewModel() {

    private val _state = MutableStateFlow(TransactionState())
//    val state: StateFlow<TransactionState> = _state.asStateFlow()
    private val _transactions = dao.getTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _transactions) { state, transactions ->
        state.copy(
            transactions = transactions
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TransactionState())

    fun onEvent(event: TransactionEvent) {
        when (event) {
            TransactionEvent.SaveTransaction -> {
                val id = state.value.id
                val bank = state.value.bank
                val merchant = state.value.merchant
                val nominal = state.value.nominal

                if (id.isBlank()) {
                    return
                }

                val transaction = Transaction(
                    idTransaksi = id,
                    bankSumber = bank,
                    namaMerchant = merchant,
                    nominal = nominal
                )
                viewModelScope.launch {
                    dao.insert(transaction)
                }
                _state.update { it.copy(
                    id = "",
                    bank = "",
                    merchant = "",
                    nominal = 0
                ) }
            }
            is TransactionEvent.SetBank -> {
                _state.update { it.copy(
                    bank = event.bank
                ) }
            }
            is TransactionEvent.SetIdTransaksi -> {
                _state.update { it.copy(
                    id = event.id
                ) }
            }
            is TransactionEvent.SetMerchant -> {
                _state.update { it.copy(
                    merchant = event.merchant
                ) }
            }
            is TransactionEvent.SetNominal -> {
                _state.update { it.copy(
                    nominal = event.nominal
                ) }
            }
        }
    }
}