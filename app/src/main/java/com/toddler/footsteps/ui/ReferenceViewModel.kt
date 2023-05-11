package com.toddler.footsteps.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.toddler.footsteps.database.reference.User
import com.toddler.footsteps.database.reference.UserDao
import com.toddler.footsteps.database.reference.UserDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


// cannot create an instance of class ReferenceViewModel
// because it is an abstract class

// DO NOT ADD DAO TO CONSTRUCTOR
// Instead, add DAO to init block
// This is because the DAO is not available until the database is created?
// and the the database is not available until the application is created?
class ReferenceViewModel(
    application: Application
) : AndroidViewModel(application) {

    // set User to val
    // here, we are setting the val to the DAO from the UserDatabase
    // the UserDatabase is a singleton, so we can access it from anywhere
    // the use of getInstance is to ensure that the UserDatabase is created
    // it is created in the companion object of UserDatabase
    // the companion object is a singleton, so it is only created once
    // not necessary to use getInstance, but it is good practice
    private val userDao: UserDao = UserDatabase.getInstance(application).userDao

    val _users = MutableStateFlow<Flow<List<User>>>(userDao.getAllUsers())

    private lateinit var  _user: MutableLiveData<User>
    val user: MutableLiveData<User>
        get() = _user

    fun setReference(user: User) {
        _user.value = user
    }

    init {
        _user.value = User()
        Log.i("ReferenceViewModel", "ReferenceViewModel Created")
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            if(userDao.getUserById(user.id) == null)
                userDao.insertUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            if(userDao.getUserById(user.id) != null)
                userDao.updateUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userDao.deleteUser(user)
        }
    }

    fun getUsers(): Flow<List<User>> {
        return _users.value
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("ReferenceViewModel", "ReferenceViewModel destroyed")
    }

}