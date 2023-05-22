package com.toddler.footsteps.database.reference

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(
    tableName = "user_table",
)
data class User(
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "selected")
    var selected: Boolean = false,
    @ColumnInfo(name = "sensorR1")
    var sensorR1: Double = 0.0,
    @ColumnInfo(name = "sensorR2")
    var sensorR2: Double = 0.0,
    @ColumnInfo(name = "sensorR3")
    var sensorR3: Double = 0.0,
    @ColumnInfo(name = "sensorR4")
    var sensorR4: Double = 0.0,
    @ColumnInfo(name = "sensorR5")
    var sensorR5: Double = 0.0,
    @ColumnInfo(name = "sensorR6")
    var sensorR6: Double = 0.0,

    @ColumnInfo(name = "sensorL1")
    var sensorL1: Double = 0.0,
    @ColumnInfo(name = "sensorL2")
    var sensorL2: Double = 0.0,
    @ColumnInfo(name = "sensorL3")
    var sensorL3: Double = 0.0,
    @ColumnInfo(name = "sensorL4")
    var sensorL4: Double = 0.0,
    @ColumnInfo(name = "sensorL5")
    var sensorL5: Double = 0.0,
    @ColumnInfo(name = "sensorL6")
    var sensorL6: Double = 0.0,

    @ColumnInfo(name = "accR0")
    var accR0: Double = 0.0,
    @ColumnInfo(name = "accR1")
    var accR1: Double = 0.0,
    @ColumnInfo(name = "accR2")
    var accR2: Double = 0.0,
    @ColumnInfo(name = "gyroR0")
    var gyroR0: Double = 0.0,
    @ColumnInfo(name = "gyroR1")
    var gyroR1: Double = 0.0,
    @ColumnInfo(name = "gyroR2")
    var gyroR2: Double = 0.0,

    @ColumnInfo(name = "accL0")
    var accL0: Double = 0.0,
    @ColumnInfo(name = "accL1")
    var accL1: Double = 0.0,
    @ColumnInfo(name = "accL2")
    var accL2: Double = 0.0,
    @ColumnInfo(name = "gyroL0")
    var gyroL0: Double = 0.0,
    @ColumnInfo(name = "gyroL1")
    var gyroL1: Double = 0.0,
    @ColumnInfo(name = "gyroL2")
    var gyroL2: Double = 0.0,

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = Timestamp(System.currentTimeMillis()).time,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
    // val timestamp: Timestamp = Timestamp(System.currentTimeMillis())
)

