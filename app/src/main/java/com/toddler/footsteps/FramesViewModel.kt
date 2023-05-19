package com.toddler.footsteps

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.toddler.footsteps.database.rawdata.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

// cannot create an instance of class FramesViewModel
// because it is an abstract class

// DO NOT ADD DAO TO CONSTRUCTOR
// Instead, add DAO to init block
// This is because the DAO is not available until the database is created?
// and the the database is not available until the application is created?
class FramesViewModel(
    application: Application
) : AndroidViewModel(application) {

   // set LeftFootFrameDao and RightFootFrameDao to vals
    // here, we are setting the vals to the DAOs from the FrameDatabase
    // the FrameDatabase is a singleton, so we can access it from anywhere
    // the use of getInstance is to ensure that the FrameDatabase is created
    // it is created in the companion object of FrameDatabase
    // the companion object is a singleton, so it is only created once
    // not necessary to use getInstance, but it is good practice
    private val leftFootFrameDao: LeftFootFrameDao = FrameDatabase.getInstance(application).leftFootFrameDao
    private val rightFootFrameDao: RightFootFrameDao = FrameDatabase.getInstance(application).rightFootFrameDao

    val _leftFootFrames = MutableStateFlow<Flow<List<LeftFootFrame>>>(leftFootFrameDao.getAllFrames())
    val _rightFootFrames = MutableStateFlow<Flow<List<RightFootFrame>>>(rightFootFrameDao.getAllFrames())

    val last50LeftFrames = MutableStateFlow<Flow<List<LeftFootFrame>>>(leftFootFrameDao.getLast50LeftFrames())
    val last50RightFrames = MutableStateFlow<Flow<List<RightFootFrame>>>(rightFootFrameDao.getLast50RightFrames())


    init {
        Log.i("FramesViewModel", "FramesViewModel Created")
    }

    fun insertLeftFootFrame(frame: LeftFootFrame) {
        viewModelScope.launch {
            leftFootFrameDao.insertFrame(frame)
        }
    }

    fun insertRightFootFrame(frame: RightFootFrame) {
        viewModelScope.launch {
            rightFootFrameDao.insertFrame(frame)
        }
    }

    fun getLeftFootFrames(): Flow<List<LeftFootFrame>> {
        return _leftFootFrames.value
    }

    fun getRightFootFrames(): Flow<List<RightFootFrame>> {
        return _rightFootFrames.value
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("FramesViewModel", "FramesViewModel destroyed")
    }
}