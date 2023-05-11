package com.toddler.footsteps.database.reference

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "user_table", indices = [Index(value = ["title"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var selected: Boolean = false,

    var sensorR1: Double = 0.0,
    var sensorR2: Double = 0.0,
    var sensorR3: Double = 0.0,
    var sensorR4: Double = 0.0,
    var sensorR5: Double = 0.0,
    var sensorR6: Double = 0.0,

    var sensorL1: Double = 0.0,
    var sensorL2: Double = 0.0,
    var sensorL3: Double = 0.0,
    var sensorL4: Double = 0.0,
    var sensorL5: Double = 0.0,
    var sensorL6: Double = 0.0,

    var accR0: Double = 0.0,
    var accR1: Double = 0.0,
    var accR2: Double = 0.0,
    var gyroR0: Double = 0.0,
    var gyroR1: Double = 0.0,
    var gyroR2: Double = 0.0,

    var accL0: Double = 0.0,
    var accL1: Double = 0.0,
    var accL2: Double = 0.0,
    var gyroL0: Double = 0.0,
    var gyroL1: Double = 0.0,
    var gyroL2: Double = 0.0,

    var timestamp: Long = Timestamp(System.currentTimeMillis()).time
    // val timestamp: Timestamp = Timestamp(System.currentTimeMillis())
)

