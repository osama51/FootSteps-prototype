package com.toddler.footsteps.ui

import android.app.Application
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.*
import com.opencsv.CSVWriter
import com.toddler.footsteps.database.rawdata.*
import com.toddler.footsteps.database.reference.UserDao
import com.toddler.footsteps.database.reference.UserDatabase
import com.toddler.footsteps.services.exportcsv.CsvConfig
import com.toddler.footsteps.services.exportcsv.ExportCsvService
import com.toddler.footsteps.services.exportcsv.adapters.toLeftFootCSV
import com.toddler.footsteps.services.exportcsv.adapters.toRightFootCSV
import com.toddler.footsteps.services.exportcsv.adapters.toUserCSV
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.apache.commons.lang3.mutable.Mutable
import java.io.File
import java.io.FileWriter

class AssessViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val defaultCounterValue = 60
    // create a livedata variable of type double to act as counter
    private val _counter = MutableLiveData<Int>(0)
    // create a getter for the counter
    val counter: LiveData<Int>
        get() = _counter

    private val _counterText: MutableLiveData<Int> = MutableLiveData(counter.value)
    val counterTextInt: LiveData<Int>
        get() = _counterText
    val counterText: LiveData<CharSequence>
        get() = Transformations.map(_counterText) {
            if (it < 0) {
                "00:00"
            } else if (it < 10) {
                "00:0${it}"
            } else if (it < 60) {
                "00:${it}"
            } else {
                "01:00"
            }
    }

    var paused = false
    var pausedTime = 0

    private val _req1 = MutableLiveData<Boolean>(false)
    val req1: MutableLiveData<Boolean>
        get() = _req1

    private val _req1Text = MutableLiveData<Boolean>(false)
    val req1Text: MutableLiveData<Boolean>
        get() = _req1Text

    private val _req2 = MutableLiveData<Boolean>(false)
    val req2: MutableLiveData<Boolean>
        get() = _req2

    private val _req2Text = MutableLiveData<Boolean>(false)
    val req2Text: MutableLiveData<Boolean>
        get() = _req2Text

    private val _req3 = MutableLiveData<Boolean>(false)
    val req3: MutableLiveData<Boolean>
        get() = _req3

    private val _req3Text = MutableLiveData<Boolean>(false)
    val req3Text: MutableLiveData<Boolean>
        get() = _req3Text

    private val _req4 = MutableLiveData<Boolean>(false)
    val req4: MutableLiveData<Boolean>
        get() = _req4

    private val _req5Text = MutableLiveData<Boolean>(false)
    val req4Text: MutableLiveData<Boolean>
        get() = _req5Text

    private val _allRequirementsMet = MutableLiveData<Boolean>(false)
    val allRequirementsMet: MutableLiveData<Boolean>
        get() = _allRequirementsMet

    private val _timerFinished = MutableLiveData<Boolean>(false)
    val timerFinished: MutableLiveData<Boolean>
        get() = _timerFinished


    private val database = FrameDatabase.getInstance(application)

    private val leftFootFrameDao: LeftFootFrameDao = database.leftFootFrameDao
    private val rightFootFrameDao: RightFootFrameDao = database.rightFootFrameDao

    private val _storeData = MutableLiveData<Boolean>(false)
    val storeData: MutableLiveData<Boolean>
        get() = _storeData

    private var job: Job = Job()

    init {
        _storeData.value = false
        setCounter(defaultCounterValue)
    }

    fun setStoreData(bool: Boolean) {
        _storeData.value = bool
    }
    fun setCounter(counter: Int) {
        _counter.value = counter
    }

    fun incrementCounter() {
        // increment the counter by 1
        if (_counter.value!! < 60) {
            _counter.value = _counter.value?.plus(1)
        } else {
            _counter.value = 60
        }
    }

    fun decrementCounter() {
        // decrement the counter by 1
        if (_counter.value!! > 10) {
            _counter.value = _counter.value?.minus(1)
        } else {
            _counter.value = 10
        }
    }

    fun startTimer() {
        if(paused) {
//            _counterText.value = pausedTime
//            job.start()
            paused = false
        } else {
            _counterText.value = _counter.value
            _timerFinished.value = false
        }
        // start the timer in a coroutine

        // name this couroutine so we can cancel it if needed
        job = viewModelScope.launch() {
            // loop until the counter is 0
            while (_counterText.value!! > 0 && !paused) {
                // delay for 1 second
                delay(1000)
                // decrement the counter
                _counterText.postValue(_counterText.value?.minus(1))
            }
            if(_counterText.value!! <= 0) {
                _counterText.value = 0
            }
            if(_counterText.value!! == 0) {
                setStoreData(false)
                _timerFinished.value = true
            }
        }

    }

    fun stopTimer() {
        job.cancel()
        _counterText.value = 0
        paused = false
        pausedTime = 0
    }

    fun pauseTimer() {
        if(paused){
            setStoreData(true)
            _counterText.value = pausedTime
            startTimer()
            Log.i("AssessViewModel", "Resume at: ${pausedTime}")

//            job.start()
//            paused = false
        } else {
            setStoreData(false)
            job.cancel()
            pausedTime = _counterText.value!!
            paused = true
        }
    }

    fun returnCounterText(): Int {
        return _counterText.value!!
    }

    fun setReq1(req1: Boolean) {
        this._req1.value = req1
        this._req1Text.value = req1
    }

    fun setReq1Text() {
//        this._req_1.value = !this._req_1.value!!
        this._req1Text.value = !this._req1Text.value!!
    }

    fun setReq2(req2: Boolean) {
        this._req2.value = req2
        this._req2Text.value = req2
    }

    fun setReq2Text() {
//        this._req_2.value = !this._req_2.value!!
        this._req2Text.value = !this._req2Text.value!!
    }

    fun setReq3(req3: Boolean) {
        this._req3.value = req3
        this._req3Text.value = req3
    }

    fun setReq3Text() {
//        this._req_3.value = !this._req_3.value!!
        this._req3Text.value = !this._req3Text.value!!
    }

    fun setReq4(req4: Boolean) {
        this._req4.value = req4
        this._req5Text.value = req4
    }

    fun setReq4Text() {
//        this._req_4.value = !this._req_4.value!!
        this._req5Text.value = !this._req5Text.value!!
    }

    fun checkAllRequirements() {
        val bool = _req1.value!! && _req2.value!! && _req3.value!! && _req4.value!!
        if(_allRequirementsMet.value != bool) {
            _allRequirementsMet.value = bool
        }
    }

    fun exportData(startTime: Long, endTime: Long){
        var leftFrames: Flow<List<LeftFootFrame>> = flow { emit(emptyList()) }
        var rightFrames: Flow<List<RightFootFrame>> = flow { emit(emptyList()) }
        Log.i("AssessViewModel", "I should be exporting any moment now")
        viewModelScope.launch {
            // get the frames from the database between the start and end times for th left foot
            try {
//                leftFrames = leftFootFrameDao.getFramesByTimestampRange(startTime, endTime)
                // for testing , just get frames of all time
                // ToDo: change this to get frames between start and end time
                leftFrames = leftFootFrameDao.getAllFrames()
                Log.i("AssessViewModel", "Left frames acquired successfully")
            } catch (e: Exception) {
                Log.i("AssessViewModel", "Error getting left frames: ${e.message}")
            }
            // get the frames from the database between the start and end times for the right foot
            try {
//                rightFrames = rightFootFrameDao.getFramesByTimestampRange(startTime, endTime)
                // for testing , just get frames of all time
                // ToDo: change this to get frames between start and end time
                rightFrames = rightFootFrameDao.getAllFrames()
                Log.i("AssessViewModel", "Right frames acquired successfully")
            } catch (e: Exception) {
                Log.i("AssessViewModel", "Error getting right frames: ${e.message}")
            }
//            exportRightToCSV(rightFrames)
            exportToCSV(leftFrames, rightFrames)
            // wait for exportToCSV to finish
            delay(2000)
        }


    }


    private suspend fun exportToCSV(
        leftFrame: Flow<List<LeftFootFrame>>,
        rightFrame: Flow<List<RightFootFrame>>
    ){
//        viewModelScope.launch{
            Log.i("AssessViewModel", "I entered the exportToCSV function")
            leftFrame.collect { left ->
                val csvConfig = CsvConfig()
                // set the Uri of the file from the csvConfig hostPath and fileName
                val uri = Uri.parse("${csvConfig.hostPath}/${csvConfig.fileName}")
                Log.i("AssessViewModel", "Uri: $uri")
                // write the left data to the csv file
                ExportCsvService(getApplication()).writeToCSV(csvConfig, uri, "Left_${csvConfig.fileName}", left.toLeftFootCSV())
                    .catch { error ->
                        // 👇 handle error here
                        Toast.makeText(getApplication(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        Log.e("AssessViewModel", "Error: ${error.message}")
                    }.collect { _ ->
                        // 👇 do anything on success
                         Toast.makeText(getApplication(), "Data Successfully Exported to CSV", Toast.LENGTH_SHORT).show()

                        Log.i("AssessViewModel", "Left Success: ${left.toLeftFootCSV()}")

                        exportRightToCSV(rightFrame)
                    }
            }
//        }
    }

    private suspend fun exportRightToCSV(
        rightFrame: Flow<List<RightFootFrame>>
    ){
        rightFrame.collect { right ->
            Log.i("AssessViewModel", "I entered the right exportToCSV function")
            val csvConfig = CsvConfig()
            // set the Uri of the file from the csvConfig hostPath and fileName
            val uri = Uri.parse("${csvConfig.hostPath}/${csvConfig.fileName}")
            Log.i("AssessViewModel", "Uri: $uri")
            // write the left data to the csv file
            ExportCsvService(getApplication()).writeToCSV(csvConfig, uri, "Right_${csvConfig.fileName}",right.toRightFootCSV())
                .catch { error ->
                    // 👇 handle error here
                    Toast.makeText(getApplication(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    Log.e("AssessViewModel", "Error: ${error.message}")
                }.collect { _ ->
                    // 👇 do anything on success
                    Toast.makeText(getApplication(), "Data Successfully Exported to CSV", Toast.LENGTH_SHORT).show()

                    Log.i("AssessViewModel", "Right Success: ${right.toRightFootCSV()}")
                }
        }
    }



//    private fun exportToCSV(
//        leftFrames: Flow<List<LeftFootFrame>>,
//        rightFrames: Flow<List<RightFootFrame>>
//    ){
//        val leftFootFile = File(Environment.getExternalStorageDirectory().toString() + "/leftFoot.csv")
//        val rightFootFile = File(Environment.getExternalStorageDirectory().toString() + "/rightFoot.csv")
//
//        val leftFootWriter = CSVWriter(FileWriter(leftFootFile))
//        val rightFootWriter = CSVWriter(FileWriter(rightFootFile))
//
//        val leftFootHeader = arrayOf("Timestamp", "X", "Y", "Z")
//        val rightFootHeader = arrayOf("Timestamp", "X", "Y", "Z")
//
//        leftFootWriter.writeNext(leftFootHeader)
//        rightFootWriter.writeNext(rightFootHeader)
//
//        viewModelScope.launch {
//            // get the frames from the database between the start and end times for th left foot
//            try {
////                val leftFrames = leftFootFrameDao.getFramesByTimestampRange(0, 1000000000000000000)
//               // iterate over the frames and write them to the csv file
//                leftFrames.collect { frames ->
//                    for (frame in frames) {
//                        val row = arrayOf(frame.timestamp.toString(), frame.x.toString(), frame.y.toString(), frame.z.toString())
//                        leftFootWriter.writeNext(row)
//                    }
//                }
//            } catch (e: Exception) {
//                Log.i("AssessViewModel", "Error getting left frames: ${e.message}")
//            }
//            // get the frames from the database between the start and end times for the right foot
//            try {
////                val rightFrames = rightFootFrameDao.getFramesByTimestampRange(0, 1000000000000000000)
//                // iterate over the frames and write them to the csv file
//                rightFrames.collect { frames ->
//                    for (frame in frames) {
//                        val row = arrayOf(frame.timestamp.toString(), frame.x.toString(), frame.y.toString(), frame.z.toString())
//                        rightFootWriter.writeNext(row)
//                    }
//                }
//            } catch (e: Exception) {
//                Log.i("AssessViewModel", "Error getting right frames: ${e.message}")
//            }
//        }
//
//        leftFootWriter.close()
//        rightFootWriter.close()
//    }
    fun resetCounter() {
        _counter.value = defaultCounterValue
    }
}