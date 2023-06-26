package com.toddler.footsteps.heatmap

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Insole(
    var sensor1: Double = 0.0,
    var sensor2: Double = 0.0,
    var sensor3: Double = 0.0,
    var sensor4: Double = 0.0,
    var sensor5: Double = 0.0,
    var sensor6: Double = 0.0,



) : Parcelable {
//
//    val propertyMap = mapOf(
//        "0" to sensor1,
//        "1" to sensor2,
//        "2" to sensor3,
//        "3" to sensor4,
//        "4" to sensor5,
//        "5" to sensor6
//    )
    operator fun get(index: Int): Double {
        return when (index) {
            0 -> sensor1
            1 -> sensor2
            2 -> sensor3
            3 -> sensor4
            4 -> sensor5
            5 -> sensor6
            else -> throw IndexOutOfBoundsException("Invalid property index")
        }
    }
}
