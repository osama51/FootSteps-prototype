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

    private val _finished = MutableLiveData<Boolean>(false)
    val finished: MutableLiveData<Boolean>
        get() = _finished

    private val _users = MutableLiveData<List<User>>(ArrayList())
    val users: MutableLiveData<List<User>>
        get() = _users

    private val _nonSelectedUsers = MutableLiveData<List<User>>(ArrayList())
    val nonSelectedUsers: MutableLiveData<List<User>>
        get() = _nonSelectedUsers

    private var _user: MutableLiveData<User> = MutableLiveData()
    val user: MutableLiveData<User>
        get() = _user

    fun setReference(user: User) {
        _user.value = user
    }

    init {
        Log.i("ReferenceViewModel", "ReferenceViewModel Created")
        _user.value = User()
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            val id = userDao.insertUser(user)
            setOtherUsersNotSelected(id)
        }
    }

    fun setOtherUsersNotSelected(exceptionUser: Long) {
        _finished.value = false
        viewModelScope.launch {
            userDao.getUsersBySelected(true).forEach { user ->
                Log.i("ReferenceViewModel", "USER: ${user.id} exceptionUser: ${exceptionUser}")
                if(user.id != exceptionUser){
                    user.selected = false
                    userDao.updateUser(user)
                    _nonSelectedUsers.value = _nonSelectedUsers.value?.plus(user)
                    _finished.value = true
                }
            }
        }
//
//        viewModelScope.launch {
//            _nonSelectedUsers.value?.forEach { user ->
//                userDao.updateUser(user)
//                Log.i("ReferenceViewModel", "USER_SELECTED: $user")
//            }
//            _finished.value = true
//        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userDao.updateUser(user)
            Log.i("ReferenceViewModel", "USER_UPDATED: ${user.selected}" )
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userDao.deleteUser(user)
        }
    }

    fun fetchUsersFromDB() {
        viewModelScope.launch {
            _users.value = userDao.getAllUsers().value ?: ArrayList()
            Log.i("ReferenceViewModel", "users: ${_users.value}")
        }
    }

    fun deleteAllUsers() {
        viewModelScope.launch {
            userDao.deleteAllUsers()
            _nonSelectedUsers.value = ArrayList()
        }
    }

    fun setFinishedFalse() {
        _finished.value = false
    }
    override fun onCleared() {
        super.onCleared()
        Log.i("ReferenceViewModel", "ReferenceViewModel destroyed")
    }

}