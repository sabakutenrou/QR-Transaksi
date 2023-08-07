package com.example.qrtransaksi.data.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.qrtransaksi.data.model.Transaction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

class TransactionDaoTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    lateinit var transactionDatabase: TransactionDatabase
    lateinit var transactionDao: TransactionDao

    @Before
    fun setup() {
        transactionDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TransactionDatabase::class.java
        ).allowMainThreadQueries().build()
        transactionDao = transactionDatabase.dao
    }

    @Test
    fun insert_expectSingleTransaction() = runBlocking {
        val transaction = Transaction(
            0,
            "test ID",
            "BNI",
            "test merchant",
            12345
        )
        transactionDao.insert(transaction)
        val result = transactionDao.getTransactions().first()
        assertEquals(1, result.size)
        assertEquals("test ID", result[0].idTransaksi)
    }
}