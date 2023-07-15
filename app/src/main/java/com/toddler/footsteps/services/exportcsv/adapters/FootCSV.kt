package com.toddler.footsteps.services.exportcsv.adapters


import com.opencsv.bean.CsvBindByName
import com.toddler.footsteps.database.rawdata.LeftFootFrame
import com.toddler.footsteps.database.rawdata.RightFootFrame
import com.toddler.footsteps.services.exportcsv.Exportable
import java.sql.Timestamp


data class FootCSV(
    @CsvBindByName(column = "sensor1")
    var sensor1: Double = 0.0,
    @CsvBindByName(column = "sensor2")
    var sensor2: Double = 0.0,
    @CsvBindByName(column = "sensor3")
    var sensor3: Double = 0.0,
    @CsvBindByName(column = "sensor4")
    var sensor4: Double = 0.0,
    @CsvBindByName(column = "sensor5")
    var sensor5: Double = 0.0,
    @CsvBindByName(column = "sensor6")
    var sensor6: Double = 0.0,

//    @CsvBindByName(column = "sensorL1")
//    var sensorL1: Double = 0.0,
//    @CsvBindByName(column = "sensorL2")
//    var sensorL2: Double = 0.0,
//    @CsvBindByName(column = "sensorL3")
//    var sensorL3: Double = 0.0,
//    @CsvBindByName(column = "sensorL4")
//    var sensorL4: Double = 0.0,
//    @CsvBindByName(column = "sensorL5")
//    var sensorL5: Double = 0.0,
//    @CsvBindByName(column = "sensorL6")
//    var sensorL6: Double = 0.0,

    @CsvBindByName(column = "acc0")
    var acc0: Double = 0.0,
    @CsvBindByName(column = "acc1")
    var acc1: Double = 0.0,
    @CsvBindByName(column = "acc2")
    var acc2: Double = 0.0,
    @CsvBindByName(column = "gyro0")
    var gyro0: Double = 0.0,
    @CsvBindByName(column = "gyro1")
    var gyro1: Double = 0.0,
    @CsvBindByName(column = "gyro2")
    var gyro2: Double = 0.0,

//    @CsvBindByName(column = "accL0")
//    var accL0: Double = 0.0,
//    @CsvBindByName(column = "accL1")
//    var accL1: Double = 0.0,
//    @CsvBindByName(column = "accL2")
//    var accL2: Double = 0.0,
//    @CsvBindByName(column = "gyroL0")
//    var gyroL0: Double = 0.0,
//    @CsvBindByName(column = "gyroL1")
//    var gyroL1: Double = 0.0,
//    @CsvBindByName(column = "gyroL2")
//    var gyroL2: Double = 0.0,

    @CsvBindByName(column = "timestamp")
    var timestamp: Long = Timestamp(System.currentTimeMillis()).time,

    ) : Exportable

fun List<LeftFootFrame>.toLeftFootCSV(): List<FootCSV> = map {
    FootCSV(
        sensor1 = it.sensor1,
        sensor2 = it.sensor2,
        sensor3 = it.sensor3,
        sensor4 = it.sensor4,
        sensor5 = it.sensor5,
        sensor6 = it.sensor6,

        acc0 = it.acc0,
        acc1 = it.acc1,
        acc2 = it.acc2,
        gyro0 = it.gyro0,
        gyro1 = it.gyro1,
        gyro2 = it.gyro2,

        timestamp = it.timestamp
    )
}

fun List<RightFootFrame>.toRightFootCSV(): List<FootCSV> = map {
    FootCSV(
        sensor1 = it.sensor1,
        sensor2 = it.sensor2,
        sensor3 = it.sensor3,
        sensor4 = it.sensor4,
        sensor5 = it.sensor5,
        sensor6 = it.sensor6,

        acc0 = it.acc0,
        acc1 = it.acc1,
        acc2 = it.acc2,
        gyro0 = it.gyro0,
        gyro1 = it.gyro1,
        gyro2 = it.gyro2,

        timestamp = it.timestamp
    )
}