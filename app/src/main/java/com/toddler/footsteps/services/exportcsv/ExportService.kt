package com.toddler.footsteps.services.exportcsv

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.WorkerThread
import com.opencsv.CSVWriter
import com.opencsv.bean.StatefulBeanToCsvBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileWriter
import javax.inject.Inject



object ExportService {

    fun <T : Exportable> export(type: Exports, content: List<T>) : Flow<Boolean> =
        when (type) {
            is Exports.CSV -> writeToCSV<T>(type.csvConfig, content)
        }

    @WorkerThread
    private fun <T : Exportable> writeToCSV(csvConfig: CsvConfig, content: List<T>) =
        flow<Boolean>{
            with(csvConfig) {

                hostPath.ifEmpty { throw IllegalStateException("Wrong Path") }
                val hostDirectory = File(hostPath)
                if (!hostDirectory.exists()) {
                    Log.i("ExportService", "Creating directory: $hostPath")
                    hostDirectory.mkdir() // 👈 create directory
                }

                // 👇 create csv file
                val csvFile = File("${hostDirectory.path}/$fileName")
                val csvWriter = CSVWriter(FileWriter(csvFile))
                Log.i("ExportService", "Creating file: ${csvFile.path}")

                // 👇 write csv file
                StatefulBeanToCsvBuilder<T>(csvWriter)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build()
                    .write(content)

                csvWriter.close()
                Log.i("ExportService", "CSV file created: ${csvFile.path}")
                Log.i("ExportService", "CSV file size: ${csvFile.length()}")
                Log.i("ExportService", "CSV file content: ${csvFile.readText()}")
            }
            // 👇 emit success
            emit(true)
        }
}


// Working perfectly on the emulator

class ExportCsvService @Inject constructor(
    private val appContext: Context
) {

    @WorkerThread
    fun <T> writeToCSV(csvConfig: CsvConfig, csvFileUri: Uri, content: List<T>) = flow<Uri> {

        csvConfig.hostPath.ifEmpty { throw IllegalStateException("Wrong Path") }
        val hostDirectory = File(csvConfig.hostPath)
        if (!hostDirectory.exists()) {
            Log.i("ExportService", "Creating directory: ${csvConfig.hostPath}")
            hostDirectory.mkdir() // 👈 create directory
        }

        // 👇 create csv file
        val csvFile = File("${hostDirectory.path}/${csvConfig.fileName}")
        val csvWriter = CSVWriter(FileWriter(csvFile))
        Log.i("ExportService", "Creating file: ${csvFile.path}")

        // 👇 write csv file
        StatefulBeanToCsvBuilder<T>(csvWriter)
            .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
            .build()
            .write(content)

        csvWriter.close()
        // 👇 emit success
        emit(csvFileUri)


//        val fileDescriptor = appContext.contentResolver.openFileDescriptor(csvFileUri, "w")
//        if (fileDescriptor != null) {
//            fileDescriptor.use {
//                val csvWriter = CSVWriter(FileWriter(it.fileDescriptor))
//                StatefulBeanToCsvBuilder<T>(csvWriter)
//                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
//                    .build()
//                    .write(content)
//                csvWriter.close()
//                emit(csvFileUri)
//            }
//        } else {
//            throw IllegalStateException("failed to read fileDescriptor")
//        }
    }
}