package com.example.qrtransaksi.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.qrtransaksi.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM `transaction`")
    fun getTransactions(): Flow<List<Transaction>>
}