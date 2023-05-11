package com.toddler.footsteps.database.reference

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user_table ORDER BY timestamp ASC")
    fun getUsersOrderedByTimestamp(): Flow<List<User>>

    @Query("SELECT * FROM user_table WHERE title = :title")
    fun getUserByTitle(title: String): List<User>

    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getUserById(id: Int): User?

}