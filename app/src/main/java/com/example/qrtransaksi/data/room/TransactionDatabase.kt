package com.example.qrtransaksi.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.qrtransaksi.data.model.Transaction

@Database(
    entities = [Transaction::class],
    version = 1
)
abstract class TransactionDatabase : RoomDatabase() {

    abstract val dao: TransactionDao
}