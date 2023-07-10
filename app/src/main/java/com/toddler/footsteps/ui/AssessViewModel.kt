package com.toddler.footsteps.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AssessViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val defaultCounterValue = 60.0
    // create a livedata variable of type double to act as counter
    private val _counter = MutableLiveData<Double>(0.0)

    // create a getter for the counter
    val counter: LiveData<Double>
        get() = _counter

    init {
        setCounter(defaultCounterValue)
    }
    fun setCounter(counter: Double) {
        _counter.value = counter
    }

    fun incrementCounter() {
        // decrement the counter by 1
        if (_counter.value != 0.0) {
            _counter.value = _counter.value?.minus(1)

        }
    }
}