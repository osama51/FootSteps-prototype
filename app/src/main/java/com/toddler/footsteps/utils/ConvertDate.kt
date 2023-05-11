package com.toddler.footsteps.utils

class ConvertDate {

    // Convert timestamp to date
    fun getDateFromTimestamp(value: Long?):
            java.sql.Date {
        return java.sql.Date(value ?: 0)
    }

    // Convert date to timestamp
    fun dateToTimestamp(date: java.sql.Date?)
            : Long {
        return date?.time ?: 0
    }

    // get time in hours, minutes from timestamp
    fun getTimeFromTimestamp(timestamp: Long): String {
        val date = java.sql.Date(timestamp)
        val hours = date.hours
        val minutes = date.minutes
        return "$hours:$minutes"
    }
}