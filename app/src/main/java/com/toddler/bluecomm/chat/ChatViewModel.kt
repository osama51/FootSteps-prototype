package com.toddler.bluecomm.chat


import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


/**
 *                 You can't share a ViewModel across Activities.
 *      That's specifically one of the downsides of using multiple activities
 *
 * */

@SuppressLint("MissingPermission")
class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val context by lazy { getApplication<Application>().applicationContext }


    val chatList = MutableLiveData<MutableList<ChatBubble?>>()
    val id = MutableLiveData<Long>()


    private val _selectedMessage = MutableLiveData<ChatBubble?>()
    val selectedMessage: LiveData<ChatBubble?>
        get() = _selectedMessage

    private val _seen: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val seen: LiveData<Boolean>
        get() = _seen


    init {
        Log.i("ChatViewModel", "ChatViewModel Created")
        chatList.value = mutableListOf()
        _seen.value = false
        id.value = 0
        _selectedMessage.value = null
    }

    fun updateChatList(message: ChatBubble) {
        Log.i("updateChatList", "ChatViewModel UPDATING")
        chatList.value?.add(message)
    }

    fun messageSelected(message: ChatBubble) {
        _selectedMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("BluetoothViewModel", "BluetoothViewModel destroyed")
    }
}