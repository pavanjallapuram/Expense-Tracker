package com.example.zobazeexpensetracker.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val notes: String? = null,
    val dateMillis: Long = System.currentTimeMillis(),
    val receiptUri: String? = null // store Uri as String, convert in UI if needed
) {
    fun toUri(): Uri? = receiptUri?.let { Uri.parse(it) }
}
