package com.toddler.footsteps.chat


import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
//import com.github.mikephil.charting.data.Entry


/**
 *                 You can't share a ViewModel across Activities.
 *      That's specifically one of the downsides of using multiple activities
 *
 * */

@SuppressLint("MissingPermission")
class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val context by lazy { getApplication<Application>().applicationContext }


    val chatList = MutableLiveData<MutableList<ChatBubble?>>()

//    val leftf0List = MutableLiveData<MutableList<Entry?>>()
//    val leftf1List = MutableLiveData<MutableList<Entry?>>()
//
//    val rightf0List = MutableLiveData<MutableList<Entry?>>()
//    val rightf1List = MutableLiveData<MutableList<Entry?>>()
    val id = MutableLiveData<Long>()
    var leftTimer = 0
    var rightTimer = 0


    private val _selectedMessage = MutableLiveData<ChatBubble?>()
    val selectedMessage: LiveData<ChatBubble?>
        get() = _selectedMessage

    private val _seen: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val seen: LiveData<Boolean>
        get() = _seen


    init {
        Log.i("ChatViewModel", "ChatViewModel Created")
        chatList.value = mutableListOf()
//        leftf0List.value = mutableListOf()
//        leftf1List.value = mutableListOf()
//        rightf0List.value = mutableListOf()
//        rightf0List.value = mutableListOf()
        _seen.value = false
        id.value = 0
        _selectedMessage.value = null
        leftTimer = 0
        rightTimer = 0
    }

    fun updateChatList(message: ChatBubble) {
//        Log.i("updateChatList", "ChatViewModel UPDATING")
        chatList.value?.add(message)
    }

    fun updateLeftPointsList( f0: Int, f1: Int) {
//        Log.i("updateChatList", "ChatViewModel UPDATING")
//        chatList.value?.add(message)
        leftTimer++
//        leftf0List.value?.add(Entry(leftTimer.toFloat(), f0.toFloat()))
//        leftf1List.value?.add(Entry(leftTimer.toFloat(), f1.toFloat()))
//        Log.i("Update Left List", "${leftf1List.value}")
    }

//    fun updateRightPointsList( f0: Int, f1: Int) {
////        Log.i("updateChatList", "ChatViewModel UPDATING")
////        chatList.value?.add(message)
//        rightTimer++
////        Log.i("Update Right List", "$f0")
//        rightf0List.value?.add(Entry(rightTimer.toFloat(), f0.toFloat()))
//        rightf1List.value?.add(Entry(rightTimer.toFloat(), f1.toFloat()))
//    }

    fun messageSelected(message: ChatBubble) {
        _selectedMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("BluetoothViewModel", "BluetoothViewModel destroyed")
    }
}