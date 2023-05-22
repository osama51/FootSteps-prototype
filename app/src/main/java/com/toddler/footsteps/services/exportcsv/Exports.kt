package com.toddler.footsteps.services.exportcsv

// List of supported export functionality
sealed class Exports {
    data class CSV(val csvConfig: CsvConfig) : Exports()
}