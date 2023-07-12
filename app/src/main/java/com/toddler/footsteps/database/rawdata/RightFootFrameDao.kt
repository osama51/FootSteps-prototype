package com.toddler.footsteps.database.rawdata

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RightFootFrameDao {

    @Insert
    suspend fun insertFrame(frame: RightFootFrame)

    @Delete
    suspend fun deleteFrame(frame: RightFootFrame)

    @Query("SELECT * FROM right_foot_frame_table")
    fun getAllFrames(): Flow<List<RightFootFrame>>

    @Query("SELECT * FROM right_foot_frame_table ORDER BY timestamp ASC")
    fun getFramesOrderedByTimestamp(): Flow<List<RightFootFrame>>

    @Query("SELECT * FROM right_foot_frame_table WHERE timestamp = :timestamp")
    fun getFramesByTimestamp(timestamp: Long): List<RightFootFrame>

    @Query("SELECT * FROM right_foot_frame_table WHERE timestamp BETWEEN :startTimestamp AND :endTimestamp")
    fun getFramesByTimestampRange(startTimestamp: Long, endTimestamp: Long): Flow<List<RightFootFrame>>

    /**
     * Get the last 50 frames from the database
     */

    @Query("SELECT * FROM right_foot_frame_table ORDER BY timestamp DESC LIMIT 50")
    fun getLast50RightFrames(): Flow<List<RightFootFrame>>

    /**
     * Get the last minute of frames from the database
     */

    @Query("SELECT * FROM right_foot_frame_table WHERE timestamp BETWEEN :startTimestamp AND :endTimestamp")
    fun getLastMinuteRightFrames(startTimestamp: Long, endTimestamp: Long): Flow<List<RightFootFrame>>

    /**
     * Get the count of frames in the database
     */

    @Query("SELECT COUNT(*) FROM left_foot_frame_table")
    fun getRightFramesCount(): Flow<Int>

}