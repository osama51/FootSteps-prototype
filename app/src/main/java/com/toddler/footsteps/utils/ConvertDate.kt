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
        // get hours, minutes from date in a non deprecated way
        val hours = date.hours.toString().padStart(2, '0')
        val minutes = date.minutes.toString().padStart(2, '0')
        return "$hours:$minutes"
    }
}