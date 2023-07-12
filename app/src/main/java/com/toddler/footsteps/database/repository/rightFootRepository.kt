package com.toddler.footsteps.database.repository

import android.app.Application
import com.toddler.footsteps.database.rawdata.FrameDatabase
import com.toddler.footsteps.database.rawdata.RightFootFrame
import com.toddler.footsteps.database.rawdata.RightFootFrameDao
import kotlinx.coroutines.flow.Flow

class rightFootRepository(application: Application) {

        val database = FrameDatabase.getInstance(application)
        private var rightFootFrameDao: RightFootFrameDao = database.rightFootFrameDao

    suspend fun insertFrame(frame: RightFootFrame) {
            return rightFootFrameDao.insertFrame(frame)
        }

        suspend fun deleteFrame(frame: RightFootFrame) {
            return rightFootFrameDao.deleteFrame(frame)
        }

        fun getAllFrames(): Flow<List<RightFootFrame>> {
            return rightFootFrameDao.getAllFrames()
        }

        fun getFramesOrderedByTimestamp(): Flow<List<RightFootFrame>> {
            return rightFootFrameDao.getFramesOrderedByTimestamp()
        }

        fun getFramesByTimestamp(timestamp: Long): List<RightFootFrame> {
            return rightFootFrameDao.getFramesByTimestamp(timestamp)
        }

        fun getFramesByTimestampRange(startTimestamp: Long, endTimestamp: Long): Flow<List<RightFootFrame>> {
            return rightFootFrameDao.getFramesByTimestampRange(startTimestamp, endTimestamp)
        }

        /**
         * Get the last 50 frames from the database
         */

        fun getLast50RightFrames(): Flow<List<RightFootFrame>> {
            return rightFootFrameDao.getLast50RightFrames()
        }

        /**
         * Get the last minute of frames from the database
         */

        fun getLastMinuteRightFrames(startTimestamp: Long, endTimestamp: Long): Flow<List<RightFootFrame>> {
            return rightFootFrameDao.getLastMinuteRightFrames(startTimestamp, endTimestamp)
        }

        /**
         * Get the count of frames in the database
         */

        fun getRightFramesCount(): Flow<Int> {
            return rightFootFrameDao.getRightFramesCount()
        }
}