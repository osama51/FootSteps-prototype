package com.toddler.footsteps.database.reference

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM user_table")
    suspend fun getAllUsersSuspend(): List<User>

    @Query("SELECT * FROM user_table ORDER BY timestamp ASC")
    fun getUsersOrderedByTimestamp(): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE title = :title")
    fun getUserByTitle(title: String): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getUserById(id: Int): LiveData<User?>

    @Query("SELECT * FROM user_table WHERE selected = :selected")
    suspend fun getUsersBySelected(selected: Boolean): List<User>

}