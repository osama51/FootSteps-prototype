package com.toddler.footsteps.database.rawdata

import androidx.room.Entity
import java.sql.Timestamp

@Entity (tableName = "right_foot_frame_table", primaryKeys = ["timestamp"])
data class RightFootFrame(
    val id: Int = 1,
    var sensor1: Double = 0.0,
    var sensor2: Double = 0.0,
    var sensor3: Double = 0.0,
    var sensor4: Double = 0.0,
    var sensor5: Double = 0.0,
    var sensor6: Double = 0.0,
    var acc0: Double = 0.0,
    var acc1: Double = 0.0,
    var acc2: Double = 0.0,
    var gyro0: Double = 0.0,
    var gyro1: Double = 0.0,
    var gyro2: Double = 0.0,
    var timestamp: Long = Timestamp(System.currentTimeMillis()).time
    // val timestamp: Timestamp = Timestamp(System.currentTimeMillis())
)
