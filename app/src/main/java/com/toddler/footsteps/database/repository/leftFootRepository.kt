package com.toddler.footsteps.database.repository

import android.app.Application
import com.toddler.footsteps.database.rawdata.FrameDatabase
import com.toddler.footsteps.database.rawdata.LeftFootFrame
import com.toddler.footsteps.database.rawdata.LeftFootFrameDao
import kotlinx.coroutines.flow.Flow

class leftFootRepository(application: Application) {

    val database = FrameDatabase.getInstance(application)
    private var leftFootFrameDao: LeftFootFrameDao = database.leftFootFrameDao


    suspend fun insertFrame(frame: LeftFootFrame) {
        return leftFootFrameDao.insertFrame(frame)
    }

    suspend fun deleteFrame(frame: LeftFootFrame) {
        return leftFootFrameDao.deleteFrame(frame)
    }

    fun getAllFrames(): Flow<List<LeftFootFrame>> {
        return leftFootFrameDao.getAllFrames()
    }

    fun getFramesOrderedByTimestamp(): Flow<List<LeftFootFrame>> {
        return leftFootFrameDao.getFramesOrderedByTimestamp()
    }

    fun getFramesByTimestamp(timestamp: Long): List<LeftFootFrame> {
        return leftFootFrameDao.getFramesByTimestamp(timestamp)
    }

    fun getFramesByTimestampRange(startTimestamp: Long, endTimestamp: Long): Flow<List<LeftFootFrame>> {
        return leftFootFrameDao.getFramesByTimestampRange(startTimestamp, endTimestamp)
    }

    /**
     * Get the last 50 frames from the database
     */

    fun getLast50LeftFrames(): Flow<List<LeftFootFrame>> {
        return leftFootFrameDao.getLast50LeftFrames()
    }

    /**
     * Get the last minute of frames from the database
     */

    fun getLastMinuteLeftFrames(startTimestamp: Long, endTimestamp: Long): Flow<List<LeftFootFrame>> {
        return leftFootFrameDao.getLastMinuteLeftFrames(startTimestamp, endTimestamp)
    }

    /**
     * Get the count of frames in the database
     */

    fun getLeftFramesCount(): Flow<Int> {
        return leftFootFrameDao.getLeftFramesCount()
    }
}