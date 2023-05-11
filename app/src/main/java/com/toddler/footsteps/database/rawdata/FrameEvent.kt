package com.toddler.footsteps.database.rawdata

import java.sql.Timestamp

sealed interface FrameEvent {
    object SaveFrame: FrameEvent
    object DeleteFrame: FrameEvent
    object GetFrames: FrameEvent
    object GetFramesOrderedByTimestamp: FrameEvent
    data class GetFramesByTimestamp(val timestamp: Timestamp): FrameEvent
    data class GetFramesByTimestampRange(val startTimestamp: Timestamp, val endTimestamp: Timestamp):
        FrameEvent
}