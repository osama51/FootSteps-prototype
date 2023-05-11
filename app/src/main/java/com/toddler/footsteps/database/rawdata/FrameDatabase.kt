package com.toddler.footsteps.database.rawdata

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [LeftFootFrame::class, RightFootFrame::class],
    version = 1,
    exportSchema = false
)

//@TypeConverters(Converters::class)
abstract class FrameDatabase : RoomDatabase() {

    abstract val leftFootFrameDao: LeftFootFrameDao
    abstract val rightFootFrameDao: RightFootFrameDao

    companion object {
        // @Volatile tells the compiler that writes to this field are immediately made visible to other threads
        @Volatile
        // INSTANCE will keep a reference to any database returned via getInstance
        private var INSTANCE: FrameDatabase? = null

        // getInstance will return INSTANCE
        // if INSTANCE is null, then create the database
        // synchronized means that only one thread of execution at a time can enter this block of code
        // this is to prevent multiple threads from attempting to create the database at the same time
        // if multiple threads try to create the database at the same time, then you will end up with multiple instances of the database
        // this is bad because it will cause the database to be inconsistent
        // this is why we use the singleton pattern
        // the getInstance method will only allow one thread to create the database at a time

        fun getInstance(application: Application): FrameDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        application.applicationContext,
                        FrameDatabase::class.java,
                        "frame_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}