package com.toddler.footsteps

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class HeatMapViewModel(application: Application) : AndroidViewModel(application) {

//    private val _scientificEnabled = MutableLiveData<Boolean>()
//    val scientificEnabled: MutableLiveData<Boolean>
//        get() = _scientificEnabled

    private val scientificEnabled = MutableLiveData<Boolean>()
    var heatMapMode = MutableLiveData<HeatMapMode>()

    init {
        scientificEnabled.value = false
        heatMapMode.value = HeatMapMode.USER_FRIENDLY
    }

    fun setScientific() {
        scientificEnabled.value = true
        heatMapMode.value = HeatMapMode.SCIENTIFIC
    }

    fun turnOffScientific() {
        scientificEnabled.value = false
        heatMapMode.value = HeatMapMode.USER_FRIENDLY
    }

    fun isScientific(): Boolean {
        return scientificEnabled.value!!
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("HeatmapViewModel", "HeatmapViewModel destroyed")
    }
}
