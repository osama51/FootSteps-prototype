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
    var sensor6: Double = 0.0
) : Parcelable
