package com.toddler.footsteps.heatmap

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.toddler.footsteps.HeatMapMode

class HeatMapViewModel(application: Application) : AndroidViewModel(application) {

//    private val _analyticEnabled = MutableLiveData<Boolean>()
//    val analyticEnabled: MutableLiveData<Boolean>
//        get() = _analyticEnabled

    private val analyticEnabled = MutableLiveData<Boolean>()
    var heatMapMode = MutableLiveData<HeatMapMode>()

    init {
        analyticEnabled.value = false
        heatMapMode.value = HeatMapMode.USER_FRIENDLY
    }

    fun setAnalytic() {
        analyticEnabled.value = true
        heatMapMode.value = HeatMapMode.ANALYTIC
    }

    fun turnOffAnalytic() {
        analyticEnabled.value = false
        heatMapMode.value = HeatMapMode.USER_FRIENDLY
    }

    fun isAnalytic(): Boolean {
        return analyticEnabled.value!!
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("HeatmapViewModel", "HeatmapViewModel destroyed")
    }
}
