package com.toddler.footsteps.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.toddler.footsteps.R
import com.toddler.footsteps.database.reference.User
import com.toddler.footsteps.database.reference.UserDao
import com.toddler.footsteps.database.reference.UserDatabase
import kotlinx.coroutines.flow.MutableStateFlow

class StatsViewModel(
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

        private val _users: MutableLiveData<List<User>> = MutableLiveData()
        val users: LiveData<List<User>>
            get() = _users

        private val _nonSelectedUsers = MutableLiveData<List<User>>(ArrayList())
        val nonSelectedUsers: MutableLiveData<List<User>>
            get() = _nonSelectedUsers

        private var _user: MutableLiveData<User> = MutableLiveData()
        val user: MutableLiveData<User>
            get() = _user

    // create a flow variable of type double to hold the speed
    private val _speed = MutableLiveData<Double>(0.0)
    // create a getter for the speed
    val speed: MutableLiveData<Double>
        get() = _speed

    private val _distance = MutableLiveData<Double>(0.0)
    val distance: MutableLiveData<Double>
        get() = _distance

    private val _cadence = MutableLiveData<Int>(0)
    val cadence: MutableLiveData<Int>
        get() = _cadence

    private val _stepCount = MutableLiveData<Int>(0)
    val stepCount: LiveData<CharSequence>
        get() = Transformations.map(_stepCount) {
            if (it > 1000) {
                "${it / 1000}k"
            } else {
                "$it"
            }
        }

//    private val _stepCount = MutableLiveData<Int>(0)
//    val stepCount: LiveData<Int>
//        get() = _stepCount

    private val _strideLength = MutableLiveData<Double>(0.0)
    val strideLength: MutableLiveData<Double>
        get() = _strideLength

    private val _stepLength = MutableLiveData<Double>(0.0)
    val stepLength: MutableLiveData<Double>
        get() = _stepLength

//    private val _stepCount = MutableLiveData<Int>(0)
//    val stepCount: LiveData<String>
//        get() = _stepCount.map {
//            if (it > 1000) {
//                "${it / 1000}k"
//            } else {
//                "$it"
//            }
//        }



//    // map the step count to use "k" when the step count is greater than 1000
//    val stepCountString: LiveData<String> = _stepCount.map {
//        if (it > 1000) {
//            "${it / 1000}k"
//        } else {
//            "$it"
//        }
//    }


    private val _stepTime = MutableLiveData<Double>(0.0)
    val stepTime: MutableLiveData<Double>
        get() = _stepTime

    private val _stepTimeLeft = MutableLiveData<Double>(0.0)
    val stepTimeLeft: MutableLiveData<Double>
        get() = _stepTimeLeft

    private val _stepTimeRight = MutableLiveData<Double>(0.0)
    val stepTimeRight: MutableLiveData<Double>
        get() = _stepTimeRight

    private val _rightFootAngle = MutableLiveData<Double>(0.0)
    val rightFootAngle: MutableLiveData<Double>
        get() = _rightFootAngle

    private val _leftFootAngle = MutableLiveData<Double>(0.0)
    val leftFootAngle: MutableLiveData<Double>
        get() = _leftFootAngle



    // get reference to the activity's resources
    private val resources = application.resources



        fun setReference(user: User) {
            _user.value = user
        }

        init {
            _stepCount.value = 6253
            Log.i("ReferenceViewModel", "ReferenceViewModel Created")
            _user.value = User()
        }


    fun setPieChart(rightPieChart: PieChart, leftPieChart: PieChart) {

        rightPieChart.setUsePercentValues(true)
        leftPieChart.setUsePercentValues(true)

        rightPieChart.setDrawEntryLabels(false)
        leftPieChart.setDrawEntryLabels(false)

        rightPieChart.isDrawHoleEnabled = true
        leftPieChart.isDrawHoleEnabled = true

        rightPieChart.setHoleColor(resources.getColor(R.color.white))
        leftPieChart.setHoleColor(resources.getColor(R.color.white))

        rightPieChart.holeRadius = 30f
        leftPieChart.holeRadius = 30f

        rightPieChart.transparentCircleRadius = 55f
        leftPieChart.transparentCircleRadius = 55f

        rightPieChart.setDrawCenterText(false)
        leftPieChart.setDrawCenterText(false)

        rightPieChart.description.isEnabled = false
        leftPieChart.description.isEnabled = false

        rightPieChart.legend.isEnabled = false
        leftPieChart.legend.isEnabled = false

        rightPieChart.setTouchEnabled(false)
        leftPieChart.setTouchEnabled(false)


        val rightPieEntries = ArrayList<PieEntry>()
        rightPieEntries.add(PieEntry(50f, "Right"))
        rightPieEntries.add(PieEntry(50f, ""))

        val leftPieEntries = ArrayList<PieEntry>()
        leftPieEntries.add(PieEntry(50f, ""))
        leftPieEntries.add(PieEntry(50f, "Left"))

        val rightPieDataSet = PieDataSet(rightPieEntries, "Right Foot")
        val leftPieDataSet = PieDataSet(leftPieEntries, "Left Foot")

        rightPieDataSet.setDrawValues(false)
        leftPieDataSet.setDrawValues(false)

        // set color for only data labeled as "Right" and "Left"
        // Two methods to set the colors (the only that work so for, otherwise would give weird colors)
        rightPieDataSet.colors = ColorTemplate.createColors(
            resources,
            intArrayOf(R.color.lightBlue, R.color.lightBlueGray40))

        leftPieDataSet.colors = listOf(
            resources.getColor(R.color.lightPink40),
            resources.getColor(R.color.lightRed))


        rightPieDataSet.sliceSpace = 2f
        leftPieDataSet.sliceSpace = 2f

        rightPieDataSet.selectionShift = 0f
        leftPieDataSet.selectionShift = 0f

        rightPieDataSet.setDrawIcons(false)
        leftPieDataSet.setDrawIcons(false)

        // change the angle of the pie chart
//        rightPieChart.rotationAngle = -45f
//        leftPieChart.rotationAngle = 270f

        val rightPieData = PieData(rightPieDataSet)
        val leftPieData = PieData(leftPieDataSet)

        rightPieChart.data = rightPieData
        leftPieChart.data = leftPieData
    }

    fun updateLeftPieCharts(leftPieChart: PieChart){
        leftPieChart.rotationAngle = _leftFootAngle.value!!.toFloat()
    }

    fun updateRightPieCharts(rightPieChart: PieChart){
        rightPieChart.rotationAngle = _rightFootAngle.value!!.toFloat()
    }

    fun setStepCount(stepCount: Int) {
        _stepCount.value = stepCount
    }

    fun setCadence(cadence: Int) {
        _cadence.value = cadence
        Log.i("StatsViewModel", "Cadence: ${_cadence.value}")
        _cadence.value?.let {
            _stepTime.value = 60.0 / it
        }

//        _cadence.postValue(cadence)
    }
}