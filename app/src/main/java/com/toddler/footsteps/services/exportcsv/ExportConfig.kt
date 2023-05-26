package com.toddler.footsteps.services.exportcsv

import android.os.Environment
import java.text.DateFormat

data class CsvConfig(
    private val prefix: String = "airfrier",
    private val suffix: String = DateFormat
        .getDateTimeInstance()
        .format(System.currentTimeMillis())
        .toString()
        .replace(",","")
        .replace(":","")
        .replace(" ", "_"),

    val fileName: String = "$prefix-$suffix.csv",
    @Suppress("DEPRECATION")
    val hostPath: String = Environment
        .getExternalStorageDirectory()?.absolutePath?.plus("/Documents/AirFrier") ?: ""
)