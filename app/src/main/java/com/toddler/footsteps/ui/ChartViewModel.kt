package com.toddler.footsteps.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.toddler.footsteps.heatmap.Insole
import kotlinx.coroutines.launch

class ChartViewModel(application: Application) : AndroidViewModel(application) {


    private val queueSize = 100 // the maximum size of the queue

    // Vessels for the data, only intermediate role
    private val dataQueue0R = ArrayDeque<Entry?>(queueSize)
    private val dataQueue1R = ArrayDeque<Entry?>(queueSize)
    private val dataQueue2R = ArrayDeque<Entry?>(queueSize)
    private val dataQueue3R = ArrayDeque<Entry?>(queueSize)
    private val dataQueue4R = ArrayDeque<Entry?>(queueSize)
    private val dataQueue5R = ArrayDeque<Entry?>(queueSize)

    // Vessels for the data, only intermediate role
    private val dataQueue0L = ArrayDeque<Entry?>(queueSize)
    private val dataQueue1L = ArrayDeque<Entry?>(queueSize)
    private val dataQueue2L = ArrayDeque<Entry?>(queueSize)
    private val dataQueue3L = ArrayDeque<Entry?>(queueSize)
    private val dataQueue4L = ArrayDeque<Entry?>(queueSize)
    private val dataQueue5L = ArrayDeque<Entry?>(queueSize)

    // collececting things together for easier iteration
    private val rightQueueList = arrayListOf(
        dataQueue0R, dataQueue1R, dataQueue2R,
        dataQueue3R, dataQueue4R, dataQueue5R
    )

    private val leftQueueList = arrayListOf(
        dataQueue0L, dataQueue1L, dataQueue2L,
        dataQueue3L, dataQueue4L, dataQueue5L
    )

    // sensors data holders
    val left0 = MutableLiveData<MutableList<Entry?>>()
    val left1 = MutableLiveData<MutableList<Entry?>>()
    val left2 = MutableLiveData<MutableList<Entry?>>()
    val left3 = MutableLiveData<MutableList<Entry?>>()
    val left4 = MutableLiveData<MutableList<Entry?>>()
    val left5 = MutableLiveData<MutableList<Entry?>>()

    val right0 = MutableLiveData<MutableList<Entry?>>()
    val right1 = MutableLiveData<MutableList<Entry?>>()
    val right2 = MutableLiveData<MutableList<Entry?>>()
    val right3 = MutableLiveData<MutableList<Entry?>>()
    val right4 = MutableLiveData<MutableList<Entry?>>()
    val right5 = MutableLiveData<MutableList<Entry?>>()

    private val rightSensorsList = arrayListOf(
        right0, right1, right2,
        right3, right4, right5
    )
    private val leftSensorsList = arrayListOf(
        left0,
        left1,
        left2,
        left3,
        left4,
        left5
    )
    val id = MutableLiveData<Long>()
    var leftTimer = 0
    var rightTimer = 0
    var Timer = 0


    init {
        Log.i("ChatViewModel", "ChatViewModel Created")

        left0.value = mutableListOf()
        right0.value = mutableListOf()

        left1.value = mutableListOf()
        right1.value = mutableListOf()

        id.value = 0
        leftTimer = 0
        rightTimer = 0
        Timer = 0
    }


    // Whenever new data arrives, add it to the queue and update the LiveData
    fun addDataToRightQueue(foot: Insole) {
        Timer++
        viewModelScope.launch {

            rightQueueList.forEachIndexed { index, queue ->
                queue.addLast(Entry(Timer.toFloat(), foot.propertyMap[index.toString()]!!.toFloat()))
                if (queue.size > queueSize) {
                    queue.removeFirst()
                }
                rightSensorsList[index].value = ArrayList(queue)
            }
        }
    }

    fun addDataToLeftQueue(foot: Insole) {
        Timer++
        viewModelScope.launch {
            leftQueueList.forEachIndexed { index, queue ->
                queue.addLast(
                    Entry(
                        Timer.toFloat(),
                        foot.propertyMap[index.toString()]!!.toFloat()
                    )
                )
                if (queue.size > queueSize) {
                    queue.removeFirst()
                }
                leftSensorsList[index].value = ArrayList(queue)
            }
        }
    }

    fun updateLeftPointsList(f0: Int, f1: Int) {
//        Log.i("updateChatList", "ChatViewModel UPDATING")
//        chatList.value?.add(message)
        leftTimer++
        left0.value?.add(Entry(leftTimer.toFloat(), f0.toFloat()))
        left1.value?.add(Entry(leftTimer.toFloat(), f1.toFloat()))
//        Log.i("Update Left List", "${leftf1List.value}")
    }

    fun updateRightPointsList(f0: Int, f1: Int) {
//        Log.i("updateChatList", "ChatViewModel UPDATING")
//        chatList.value?.add(message)
        rightTimer++
//        Log.i("Update Right List", "$f0")
        right0.value?.add(Entry(rightTimer.toFloat(), f0.toFloat()))
        right1.value?.add(Entry(rightTimer.toFloat(), f1.toFloat()))
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("ChartsViewModel", "ChartsViewModel destroyed")
    }

}