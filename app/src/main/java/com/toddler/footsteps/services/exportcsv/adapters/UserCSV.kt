package com.toddler.footsteps.services.exportcsv.adapters


import com.opencsv.bean.CsvBindByName
import com.toddler.footsteps.database.reference.User
import com.toddler.footsteps.services.exportcsv.Exportable
import java.sql.Timestamp


data class UserCSV(
    @CsvBindByName(column = "title")
    var title: String = "",
    @CsvBindByName(column = "selected")
    var selected: Boolean = false,
    @CsvBindByName(column = "sensorR1")
    var sensorR1: Double = 0.0,
    @CsvBindByName(column = "sensorR2")
    var sensorR2: Double = 0.0,
    @CsvBindByName(column = "sensorR3")
    var sensorR3: Double = 0.0,
    @CsvBindByName(column = "sensorR4")
    var sensorR4: Double = 0.0,
    @CsvBindByName(column = "sensorR5")
    var sensorR5: Double = 0.0,
    @CsvBindByName(column = "sensorR6")
    var sensorR6: Double = 0.0,

    @CsvBindByName(column = "sensorL1")
    var sensorL1: Double = 0.0,
    @CsvBindByName(column = "sensorL2")
    var sensorL2: Double = 0.0,
    @CsvBindByName(column = "sensorL3")
    var sensorL3: Double = 0.0,
    @CsvBindByName(column = "sensorL4")
    var sensorL4: Double = 0.0,
    @CsvBindByName(column = "sensorL5")
    var sensorL5: Double = 0.0,
    @CsvBindByName(column = "sensorL6")
    var sensorL6: Double = 0.0,

    @CsvBindByName(column = "accR0")
    var accR0: Double = 0.0,
    @CsvBindByName(column = "accR1")
    var accR1: Double = 0.0,
    @CsvBindByName(column = "accR2")
    var accR2: Double = 0.0,
    @CsvBindByName(column = "gyroR0")
    var gyroR0: Double = 0.0,
    @CsvBindByName(column = "gyroR1")
    var gyroR1: Double = 0.0,
    @CsvBindByName(column = "gyroR2")
    var gyroR2: Double = 0.0,

    @CsvBindByName(column = "accL0")
    var accL0: Double = 0.0,
    @CsvBindByName(column = "accL1")
    var accL1: Double = 0.0,
    @CsvBindByName(column = "accL2")
    var accL2: Double = 0.0,
    @CsvBindByName(column = "gyroL0")
    var gyroL0: Double = 0.0,
    @CsvBindByName(column = "gyroL1")
    var gyroL1: Double = 0.0,
    @CsvBindByName(column = "gyroL2")
    var gyroL2: Double = 0.0,

    @CsvBindByName(column = "timestamp")
    var timestamp: Long = Timestamp(System.currentTimeMillis()).time,

    ) : Exportable

fun List<User>.toUserCSV(): List<UserCSV> = map {
    UserCSV(
        title = it.title,
        selected = it.selected,
        sensorR1 = it.sensorR1,
        sensorR2 = it.sensorR2,
        sensorR3 = it.sensorR3,
        sensorR4 = it.sensorR4,
        sensorR5 = it.sensorR5,
        sensorR6 = it.sensorR6,

        sensorL1 = it.sensorL1,
        sensorL2 = it.sensorL2,
        sensorL3 = it.sensorL3,
        sensorL4 = it.sensorL4,
        sensorL5 = it.sensorL5,
        sensorL6 = it.sensorL6,

        accR0 = it.accR0,
        accR1 = it.accR1,
        accR2 = it.accR2,
        gyroR0 = it.gyroR0,
        gyroR1 = it.gyroR1,
        gyroR2 = it.gyroR2,

        accL0 = it.accL0,
        accL1 = it.accL1,
        accL2 = it.accL2,
        gyroL0 = it.gyroL0,
        gyroL1 = it.gyroL1,
        gyroL2 = it.gyroL2,

        timestamp = it.timestamp
    )
}


