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

    private val _req1 = MutableLiveData<Boolean>(false)
    val req1: MutableLiveData<Boolean>
        get() = _req1

    private val _req1Text = MutableLiveData<Boolean>(false)
    val req1Text: MutableLiveData<Boolean>
        get() = _req1Text

    private val _req2 = MutableLiveData<Boolean>(false)
    val req2: MutableLiveData<Boolean>
        get() = _req2

    private val _req2Text = MutableLiveData<Boolean>(false)
    val req2Text: MutableLiveData<Boolean>
        get() = _req2Text

    private val _req3 = MutableLiveData<Boolean>(false)
    val req3: MutableLiveData<Boolean>
        get() = _req3

    private val _req3Text = MutableLiveData<Boolean>(false)
    val req3Text: MutableLiveData<Boolean>
        get() = _req3Text

    private val _req4 = MutableLiveData<Boolean>(false)
    val req4: MutableLiveData<Boolean>
        get() = _req4

    private val _req5Text = MutableLiveData<Boolean>(false)
    val req4Text: MutableLiveData<Boolean>
        get() = _req5Text

    private val _allRequirementsMet = MutableLiveData<Boolean>(false)
    val allRequirementsMet: MutableLiveData<Boolean>
        get() = _allRequirementsMet

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

    fun setReq1(req1: Boolean) {
        this._req1.value = req1
        this._req1Text.value = req1
    }

    fun setReq1Text() {
//        this._req_1.value = !this._req_1.value!!
        this._req1Text.value = !this._req1Text.value!!
    }

    fun setReq2(req2: Boolean) {
        this._req2.value = req2
        this._req2Text.value = req2
    }

    fun setReq2Text() {
//        this._req_2.value = !this._req_2.value!!
        this._req2Text.value = !this._req2Text.value!!
    }

    fun setReq3(req3: Boolean) {
        this._req3.value = req3
        this._req3Text.value = req3
    }

    fun setReq3Text() {
//        this._req_3.value = !this._req_3.value!!
        this._req3Text.value = !this._req3Text.value!!
    }

    fun setReq4(req4: Boolean) {
        this._req4.value = req4
        this._req5Text.value = req4
    }

    fun setReq4Text() {
//        this._req_4.value = !this._req_4.value!!
        this._req5Text.value = !this._req5Text.value!!
    }

    fun checkAllRequirements() {
        val bool = _req1.value!! && _req2.value!! && _req3.value!! && _req4.value!!
        if(_allRequirementsMet.value != bool) {
            _allRequirementsMet.value = bool
        }
    }

    fun resetCounter() {
        _counter.value = defaultCounterValue
    }
}