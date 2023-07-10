package com.toddler.footsteps.database.rawdata

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LeftFootFrameDao {

    @Insert
    suspend fun insertFrame(frame: LeftFootFrame)

    @Delete
    suspend fun deleteFrame(frame: LeftFootFrame)

    @Query("SELECT * FROM left_foot_frame_table")
    fun getAllFrames(): Flow<List<LeftFootFrame>>

    @Query("SELECT * FROM left_foot_frame_table ORDER BY timestamp ASC")
    fun getFramesOrderedByTimestamp(): Flow<List<LeftFootFrame>>

    @Query("SELECT * FROM left_foot_frame_table WHERE timestamp = :timestamp")
    fun getFramesByTimestamp(timestamp: Long): List<LeftFootFrame>

    @Query("SELECT * FROM left_foot_frame_table WHERE timestamp BETWEEN :startTimestamp AND :endTimestamp")
    fun getFramesByTimestampRange(startTimestamp: Long, endTimestamp: Long): Flow<List<LeftFootFrame>>

    /**
     * Get the last 50 frames from the database
     */

    @Query("SELECT * FROM left_foot_frame_table ORDER BY timestamp DESC LIMIT 50")
    fun getLast50LeftFrames(): Flow<List<LeftFootFrame>>

    /**
     * Get the last minute of frames from the database
     */

    @Query("SELECT * FROM left_foot_frame_table WHERE timestamp BETWEEN :startTimestamp AND :endTimestamp")
    fun getLastMinuteLeftFrames(startTimestamp: Long, endTimestamp: Long): Flow<List<LeftFootFrame>>

    /**
     * Get the count of frames in the database
     */

    @Query("SELECT COUNT(*) FROM left_foot_frame_table")
    fun getLeftFramesCount(): Flow<Int>
}