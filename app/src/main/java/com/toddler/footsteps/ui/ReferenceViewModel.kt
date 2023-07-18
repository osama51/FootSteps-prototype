package com.toddler.footsteps.ui

import android.app.Application
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.toddler.footsteps.R
import com.toddler.footsteps.database.reference.User
import com.toddler.footsteps.database.reference.UserDao
import com.toddler.footsteps.database.reference.UserDatabase
import com.toddler.footsteps.heatmap.Insole
import kotlinx.coroutines.launch
import java.lang.Math.abs


// cannot create an instance of class ReferenceViewModel
// because it is an abstract class

// DO NOT ADD DAO TO CONSTRUCTOR
// Instead, add DAO to init block
// This is because the DAO is not available until the database is created?
// and the the database is not available until the application is created?


enum class AlarmState {
    NONE,
    LEFT,
    RIGHT,
    TOP,
    BOTTOM,
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    ALL
}

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

    private val _referenceSet = MutableLiveData<Boolean>(false)
    val referenceSet: MutableLiveData<Boolean>
        get() = _referenceSet

    private val _users: MutableLiveData<List<User>> = MutableLiveData()
    val users: LiveData<List<User>>
        get() = _users

    private val _nonSelectedUsers = MutableLiveData<List<User>>(ArrayList())
    val nonSelectedUsers: MutableLiveData<List<User>>
        get() = _nonSelectedUsers

    private var _user: MutableLiveData<User?> = MutableLiveData()
    val user: MutableLiveData<User?>
        get() = _user

    private val _alarmState = MutableLiveData<AlarmState>(AlarmState.NONE)
    val alarmState: MutableLiveData<AlarmState>
        get() = _alarmState

    private val _arrayDrawables = MutableLiveData<Array<Drawable?>>()
    val arrayDrawables: MutableLiveData<Array<Drawable?>>
        get() = _arrayDrawables

    private val _arrayDrawables2 = MutableLiveData<Array<Drawable?>>()
    val arrayDrawables2: MutableLiveData<Array<Drawable?>>
        get() = _arrayDrawables2

    fun setReference(user: User) {
        _user.value = user
    }

    fun setReferenceSet(value: Boolean) {
        _referenceSet.value = value
    }

    fun getLeftRefSum(): Double{
        val left = _user.value!!.sensorL1 + _user.value!!.sensorL2 + _user.value!!.sensorL3  +_user.value!!.sensorL4 + _user.value!!.sensorL5 + _user.value!!.sensorL6
        return left
    }

    fun getRightRefSum(): Double{
        val right = _user.value!!.sensorR1 + _user.value!!.sensorR2 + _user.value!!.sensorR3  +_user.value!!.sensorR4 + _user.value!!.sensorR5 + _user.value!!.sensorR6
        return right
    }

    init {
        Log.i("ReferenceViewModel", "ReferenceViewModel Created")
        _user.value = User()

        _arrayDrawables.value = arrayOf(
            ResourcesCompat.getDrawable(
                getApplication<Application>().resources,
                R.drawable.bg_gradient_red,
                null
            ),
            ResourcesCompat.getDrawable(
                getApplication<Application>().resources,
                R.drawable.bg_gradient_red,
                null
            )
        )

        _arrayDrawables2.value = arrayOf(
            ResourcesCompat.getDrawable(
                getApplication<Application>().resources,
                R.drawable.bg_gradient_red,
                null
            ),
            ResourcesCompat.getDrawable(
                getApplication<Application>().resources,
                R.drawable.bg_gradient_red,
                null
            )
        )
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

    var _fromDrawableId: Int = R.drawable.bg_gradient_red
    var _toDrawableId: Int = R.drawable.bg_gradient_red
    var state = AlarmState.NONE

    fun setAlarms(leftFoot: Insole, rightFoot: Insole, leftAcc: Double, rightAcc: Double, frameLayout: FrameLayout) {
        viewModelScope.launch {



            // set the foreground color of the frame layout to drawable named "bg_alert_left" with animation from transpaeent to red
//
//                frameLayout.foreground = ResourcesCompat.getDrawable(
//                    getApplication<Application>().resources,
//                    if (leftAcc > 180) {
//                        R.drawable.bg_alert_right
//                    } else {
//                        R.drawable.bg_alert_left
//                    },
//                    null
//                )

            var leftFootSum = leftFoot.sensor1 + leftFoot.sensor2 + leftFoot.sensor3 + leftFoot.sensor4 + leftFoot.sensor5 + leftFoot.sensor6
            var rightFootSum = rightFoot.sensor1 + rightFoot.sensor2 + rightFoot.sensor3 + rightFoot.sensor4 + rightFoot.sensor5 + rightFoot.sensor6


            frameLayout.foreground = ResourcesCompat.getDrawable(
                getApplication<Application>().resources,
                if (kotlin.math.abs(leftAcc - rightAcc) > 60 ) {
                    state = AlarmState.ALL
                    R.drawable.bg_alert_all
                } else if (leftFootSum > getLeftRefSum() * 3 && _referenceSet.value!!) {
                    state = AlarmState.LEFT
                    R.drawable.bg_alert_left
                } else if (rightFootSum > getRightRefSum() * 3 && _referenceSet.value!!) {
                    state = AlarmState.RIGHT
                    R.drawable.bg_alert_right
                } else {
                    state = AlarmState.NONE
                    R.drawable.bg_gradient_red
                },
                null
            )


//
////            Log.i("ReferenceViewModel", "leftAcc: $leftAcc rightAcc: $rightAcc")
//            if(kotlin.math.abs(leftAcc - rightAcc) > 60) {
//                if(_alarmState.value != AlarmState.ALL){
//                    _fromDrawableId = _toDrawableId
//                    _toDrawableId = R.drawable.bg_alert_all
//                    state = AlarmState.ALL
//                }
//            } else if(leftFootSum > getLeftRefSum() * 1.5){
//            if(_alarmState.value != AlarmState.LEFT){
//                _fromDrawableId = _toDrawableId
//                _toDrawableId = R.drawable.bg_alert_left
//                state = AlarmState.LEFT
//            }
//        } else if(rightFootSum > getRightRefSum() * 1.5) {
//                if (_alarmState.value != AlarmState.RIGHT) {
//                    _fromDrawableId = _toDrawableId
//                    _toDrawableId = R.drawable.bg_alert_right
//                    state = AlarmState.RIGHT
//
//                }
//            }else{
//                Log.i("ReferenceViewModel", "Condition:::Im in the wrong condition")
//                if(_alarmState.value != AlarmState.NONE ){
//                    _fromDrawableId = _toDrawableId
//                    _toDrawableId = R.drawable.bg_gradient_red
//                    state = AlarmState.NONE
//                }
//            }
//
//            Log.i("ReferenceViewModel", "Condition:::${rightFootSum > getRightRefSum() * 1.5}")
//

//



//            if(leftAcc > 180){
//                if(_alarmState.value != AlarmState.RIGHT){
//                    _fromDrawableId = _toDrawableId
//                    _toDrawableId = R.drawable.bg_alert_right
//                    _alarmState.value = AlarmState.RIGHT
//                }
//            } else {
//                if(_alarmState.value != AlarmState.LEFT){
//                    _fromDrawableId = _toDrawableId
//                    _toDrawableId = R.drawable.bg_alert_left
//                    _alarmState.value = AlarmState.LEFT
//                }
//            }

            // array of drawables of left and right alerts
            _arrayDrawables.value = arrayOf(
                ResourcesCompat.getDrawable(
                    getApplication<Application>().resources,
                    _fromDrawableId,
                    null
                ),
                ResourcesCompat.getDrawable(
                    getApplication<Application>().resources,
                    _toDrawableId,
                    null
                )
            )

            _arrayDrawables2.value = arrayOf(
                ResourcesCompat.getDrawable(
                    getApplication<Application>().resources,
                    R.drawable.bg_gradient_red,
                    null
                ),
                ResourcesCompat.getDrawable(
                    getApplication<Application>().resources,
                    _toDrawableId,
                    null
                )
            )

            if(_alarmState.value != state){
                _alarmState.value = state
            }


//            if (user.selected) {
//                if (user.leftFoot == leftFoot && user.rightFoot == rightFoot) {
//                    R.drawable.bg_alert_left
//                } else {
//                    R.drawable.bg_alert_right
//                }
//            } else {
//                R.drawable.bg_alert_right
//            }



//            user.leftFoot = leftFoot
//            user.rightFoot = rightFoot
//            userDao.updateUser(user)
//            Log.i("ReferenceViewModel", "USER_UPDATED: $user")
        }
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
//            val data  = userDao.getAllUsers().value ?: ArrayList()
            val data  = userDao.getAllUsersSuspend()
            _users.value = data
//            Log.i("ReferenceViewModel", "users: ${_users.value}")
        }
    }

    fun getLastUser() {
        viewModelScope.launch {
            val data  = userDao.getLastUser()
            _user.value = data
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