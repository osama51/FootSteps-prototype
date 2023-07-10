package com.toddler.footsteps.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.toddler.footsteps.LeftRight
import com.toddler.footsteps.LineChartStyle
import com.toddler.footsteps.R
import com.toddler.footsteps.Screens
import com.toddler.footsteps.chat.ChatViewModel
import com.toddler.footsteps.databinding.FragmentChartsBinding
import com.toddler.footsteps.heatmap.Insole
import kotlin.math.abs

class ChartsFragment : Fragment() {

    private lateinit var binding: FragmentChartsBinding
    private lateinit var chartsViewModel: ChartViewModel
    private lateinit var chatViewModel: ChatViewModel


    private lateinit var sensor6Chart: LineChart
    private lateinit var sensor5Chart: LineChart
    private lateinit var sensor4Chart: LineChart
    private lateinit var sensor3Chart: LineChart
    private lateinit var sensor2Chart: LineChart
    private lateinit var sensor1Chart: LineChart


    private var left1LineDataSet: LineDataSet = LineDataSet(null, "Left 1")
    private var left2LineDataSet: LineDataSet = LineDataSet(null, "Left 2")
    private var left3LineDataSet: LineDataSet = LineDataSet(null, "Left 3")
    private var left4LineDataSet: LineDataSet = LineDataSet(null, "Left 4")
    private var left5LineDataSet: LineDataSet = LineDataSet(null, "Left 5")
    private var left6LineDataSet: LineDataSet = LineDataSet(null, "Left 6")

    private var right1LineDataSet: LineDataSet = LineDataSet(null, "Right 1")
    private var right2LineDataSet: LineDataSet = LineDataSet(null, "Right 2")
    private var right3LineDataSet: LineDataSet = LineDataSet(null, "Right 3")
    private var right4LineDataSet: LineDataSet = LineDataSet(null, "Right 4")
    private var right5LineDataSet: LineDataSet = LineDataSet(null, "Right 5")
    private var right6LineDataSet: LineDataSet = LineDataSet(null, "Right 6")

    private var iLineDataSet1: java.util.ArrayList<ILineDataSet> = java.util.ArrayList()
    private var iLineDataSet2: java.util.ArrayList<ILineDataSet> = java.util.ArrayList()
    private var iLineDataSet3: java.util.ArrayList<ILineDataSet> = java.util.ArrayList()
    private var iLineDataSet4: java.util.ArrayList<ILineDataSet> = java.util.ArrayList()
    private var iLineDataSet5: java.util.ArrayList<ILineDataSet> = java.util.ArrayList()
    private var iLineDataSet6: java.util.ArrayList<ILineDataSet> = java.util.ArrayList()

    private var lineData1: LineData = LineData()
    private var lineData2: LineData = LineData()
    private var lineData3: LineData = LineData()
    private var lineData4: LineData = LineData()
    private var lineData5: LineData = LineData()
    private var lineData6: LineData = LineData()

    private lateinit var chartStyle: LineChartStyle

    private var charts: ArrayList<LineChart> = ArrayList()
    private var lineData: ArrayList<LineData> = ArrayList()
    private var leftLineDataSets: ArrayList<LineDataSet> = ArrayList()
    private var rightLineDataSets: ArrayList<LineDataSet> = ArrayList()
    private var iLineDataSets: ArrayList<ArrayList<ILineDataSet>> = ArrayList()

    private var sensorCards: ArrayList<View> = ArrayList()
    private var imageButtons: ArrayList<View> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChartsBinding.inflate(inflater, container, false)
        chartsViewModel = ViewModelProvider(requireActivity())[ChartViewModel::class.java]
        chatViewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]


//        blur()

        initRightGraphs()
        initLeftGraphs()

        var appbar = binding.appBarLayout
        var toolbar = binding.toolbar

//        toolbar.setNavigationOnClickListener {
//            chatViewModel.setScreen(Screens.MAIN_SCREEN)
//            // close the fragment
//            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
//        }

//        appbar.setLiftable(true)



//        left0LineDataSet.lineWidth = 3F
//        left0LineDataSet.formLineWidth = 3F
//        left1LineDataSet.lineWidth = 3F
//        left1LineDataSet.formLineWidth = 3F

        sensor6Chart = binding.sensor6Chart
        sensor5Chart = binding.sensor5Chart
        sensor4Chart = binding.sensor4Chart
        sensor3Chart = binding.sensor3Chart
        sensor2Chart = binding.sensor2Chart
        sensor1Chart = binding.sensor1Chart

//        right0LineDataSet.lineWidth = 3F
//        right0LineDataSet.formLineWidth = 3F
//        right1LineDataSet.lineWidth = 3F
//        right1LineDataSet.formLineWidth = 3F

        chartStyle = LineChartStyle(requireContext())

        chartStyle.styleChart(sensor6Chart)
        chartStyle.styleChart(sensor5Chart)
        chartStyle.styleChart(sensor4Chart)
        chartStyle.styleChart(sensor3Chart)
        chartStyle.styleChart(sensor2Chart)
        chartStyle.styleChart(sensor1Chart)


//        chartStyle.drawLineGraph(binding.sensor5Chart)
//        chartStyle.drawLineGraph(binding.sensor6Chart)

        chartStyle.styleLineDataSet(left6LineDataSet, LeftRight.LEFT)
        chartStyle.styleLineDataSet(right6LineDataSet, LeftRight.RIGHT)

        chartStyle.styleLineDataSet(left5LineDataSet, LeftRight.LEFT)
        chartStyle.styleLineDataSet(right5LineDataSet, LeftRight.RIGHT)

        chartStyle.styleLineDataSet(left4LineDataSet, LeftRight.LEFT)
        chartStyle.styleLineDataSet(right4LineDataSet, LeftRight.RIGHT)

        chartStyle.styleLineDataSet(left3LineDataSet, LeftRight.LEFT)
        chartStyle.styleLineDataSet(right3LineDataSet, LeftRight.RIGHT)

        chartStyle.styleLineDataSet(left2LineDataSet, LeftRight.LEFT)
        chartStyle.styleLineDataSet(right2LineDataSet, LeftRight.RIGHT)

        chartStyle.styleLineDataSet(left1LineDataSet, LeftRight.LEFT)
        chartStyle.styleLineDataSet(right1LineDataSet, LeftRight.RIGHT)


//        test()
        chartsViewModel.right5.observe(viewLifecycleOwner) {
//            Log.i("ChartsFragment", "right5: ${it}")
//            Log.i("ChartsFragment", "left5 ${chartsViewModel.left5.value?.get(0)}")
            updateGraphRight(it, 5, right6LineDataSet, left6LineDataSet, iLineDataSet6, sensor6Chart, arrayListOf(lineData6))
        }

        chartsViewModel.right4.observe(viewLifecycleOwner) {
            updateGraphRight( it, 4, right5LineDataSet, left5LineDataSet, iLineDataSet5, sensor5Chart, arrayListOf(lineData5))
        }

        chartsViewModel.right3.observe(viewLifecycleOwner) {
            updateGraphRight(it, 3, right4LineDataSet, left4LineDataSet, iLineDataSet4, sensor4Chart, arrayListOf(lineData4))
        }

        chartsViewModel.right2.observe(viewLifecycleOwner) {
            updateGraphRight(it, 2, right3LineDataSet, left3LineDataSet, iLineDataSet3, sensor3Chart, arrayListOf(lineData3))
        }

        chartsViewModel.right1.observe(viewLifecycleOwner) {
            updateGraphRight(it, 1, right2LineDataSet, left2LineDataSet, iLineDataSet2, sensor2Chart, arrayListOf(lineData2))
        }


        chartsViewModel.right0.observe(viewLifecycleOwner) {
            updateGraphRight(it, 0, right1LineDataSet, left1LineDataSet, iLineDataSet1, sensor1Chart, arrayListOf(lineData1))
        }


        // if the user clicks on image button for each graph, it changes it visibilty to visible, when clicked again, it changes it to gone
        // list of all the charts
        charts = arrayListOf(sensor1Chart, sensor2Chart, sensor3Chart, sensor4Chart, sensor5Chart, sensor6Chart)
        // list of all the line data sets
        lineData = arrayListOf(lineData1, lineData2, lineData3, lineData4, lineData5, lineData6)
        // list of all left line data sets
        leftLineDataSets = arrayListOf(left1LineDataSet, left2LineDataSet, left3LineDataSet, left4LineDataSet, left5LineDataSet, left6LineDataSet)
        // list of all right line data sets
        rightLineDataSets = arrayListOf(right1LineDataSet, right2LineDataSet, right3LineDataSet, right4LineDataSet, right5LineDataSet, right6LineDataSet)
        // list of all i line data sets
        iLineDataSets = arrayListOf(iLineDataSet1, iLineDataSet2, iLineDataSet3, iLineDataSet4, iLineDataSet5, iLineDataSet6)


        initCharts()

        // list of all sensor cards
        sensorCards = arrayListOf(binding.sensor1Card, binding.sensor2Card, binding.sensor3Card,
                                      binding.sensor4Card, binding.sensor5Card, binding.sensor6Card)

        // list of all image buttons
        imageButtons = arrayListOf(binding.sensor1Button, binding.sensor2Button, binding.sensor3Button,
            binding.sensor4Button, binding.sensor5Button, binding.sensor6Button)

        // set click listeners for each sensor card
        setClickListenersForSensorCards()

        val fragmentManager = requireActivity().supportFragmentManager
        // go back to the previous fragment upon clicking the back button on the toolbar
        binding.toolbar.setNavigationOnClickListener {
//            requireActivity().onBackPressed()
            fragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun setClickListenersForSensorCards(){

        // loop through each sensor card and each image button and set a click listener for each one
        for (i in sensorCards){
            i.setOnClickListener {
                if (charts[sensorCards.indexOf(i)].visibility == View.GONE) {
                    charts[sensorCards.indexOf(i)].visibility = View.VISIBLE
                } else {
                    charts[sensorCards.indexOf(i)].visibility = View.GONE
                }
            }
        }

        for (i in imageButtons){
            i.setOnClickListener {
                if (charts[imageButtons.indexOf(i)].visibility == View.GONE) {
                    charts[imageButtons.indexOf(i)].visibility = View.VISIBLE
                } else {
                    charts[imageButtons.indexOf(i)].visibility = View.GONE
                }
            }
        }
    }

    private fun initRightGraphs(){
        for(i in arrayListOf(right6LineDataSet, right5LineDataSet, right4LineDataSet, right3LineDataSet, right2LineDataSet, right1LineDataSet)){
            i.label = "Right"
            i.color = resources.getColor(R.color.lightBlue)
        }
    }

    private fun initLeftGraphs(){
        for(i in arrayListOf(left6LineDataSet, left5LineDataSet, left4LineDataSet, left3LineDataSet, left2LineDataSet, left1LineDataSet)){
            i.label = "Left"
            i.color = resources.getColor(R.color.lightRed)
        }
    }


    private fun updateGraphRight(rightData: MutableList<Entry?>, leftIndex: Int, rightLineDataSet: LineDataSet, leftLineDataSet: LineDataSet,
                                 iLineDataSet:  ArrayList<ILineDataSet>, chart: LineChart, lineData: ArrayList<LineData>) {

        if (chatViewModel.screen.value == Screens.CHART_SCREEN){
            leftLineDataSet.values = chartsViewModel.leftSensorsList[leftIndex].value
//        leftLineDataSet.label = "Left"
//        leftLineDataSet.color = resources.getColor(R.color.lightRed)

////
////            exampleData.addEntry(...);
//            right6LineDataSet.addEntry(chartsViewModel.right5.value?.last()!!)
//            chart.notifyDataSetChanged(); // let the chart know it's data changed
//            chart.invalidate(); // refresh

            rightLineDataSet.values = rightData

            iLineDataSet.clear()
            iLineDataSet.add(leftLineDataSet)
            iLineDataSet.add(rightLineDataSet)

            lineData[0] = LineData(iLineDataSet)

            chart.clear()
            chart.data = lineData[0]
            chart.invalidate()
        }
    }

    private fun initCharts(){
        charts.forEachIndexed { index, chart ->
            updateGraphRight(chartsViewModel.rightSensorsList[index].value!!, index, rightLineDataSets[index],
                leftLineDataSets[index], iLineDataSets[index], chart, arrayListOf(lineData[index]))
        }
    }

    fun test(){
        if (chatViewModel.screen.value == Screens.CHART_SCREEN){
            left6LineDataSet.values = chartsViewModel.leftSensorsList[5].value
//        leftLineDataSet.label = "Left"
//        leftLineDataSet.color = resources.getColor(R.color.lightRed)

//
//            exampleData.addEntry(...);
//            chart.notifyDataSetChanged(); // let the chart know it's data changed
//            chart.invalidate(); // refresh

            right6LineDataSet.values = chartsViewModel.right5.value


//            iLineDataSet.clear()
//            iLineDataSet.add(leftLineDataSet)
            iLineDataSet6.add(right6LineDataSet)

            lineData6 = LineData(iLineDataSet6)

//            chart.clear()
            sensor6Chart.data = lineData6
            sensor6Chart.invalidate()
        }
    }

    //    fun blur(
//        viewToBlur: ViewGroup = binding.root as ViewGroup,
//        radius: Float = 16f,
//        isAutoUpdate: Boolean = true,
//        hasFixedTransformationMatrix: Boolean = true,
//    ) {
//        binding.blurView.setupWith(viewToBlur, RenderScriptBlur(requireContext()))
//            .setBlurEnabled(true)
//            .setBlurRadius(radius)
//            .setBlurAutoUpdate(isAutoUpdate)
//
//        binding.blurView.outlineProvider = ViewOutlineProvider.BACKGROUND;
//        binding.blurView.clipToOutline = true;
//
//    }

    private fun updateGraphLeft(leftData: MutableList<Entry?>, leftLineDataSet: LineDataSet, iLineDataSet:  ArrayList<ILineDataSet>,
                                chart: LineChart, lineData: ArrayList<LineData>) {

        leftLineDataSet.values = leftData


        iLineDataSet.clear()
        iLineDataSet.add(leftLineDataSet)


        lineData[0] = LineData(iLineDataSet)

        chart.clear()
        chart.data = lineData[0]
        chart.invalidate()
    }


    fun updateGraph2(leftData: MutableList<Entry?>, rightData: MutableList<Entry?>) {

        right6LineDataSet.values = leftData
        right6LineDataSet.label = "Left"
        right6LineDataSet.color = resources.getColor(R.color.lightRed)

        right1LineDataSet.values = rightData
        right1LineDataSet.label = "Right"
        right1LineDataSet.color = resources.getColor(R.color.lightGreen)

        iLineDataSet2.clear()
        iLineDataSet2.add(right6LineDataSet)
        iLineDataSet2.add(right1LineDataSet)
        lineData2 = LineData(iLineDataSet2)

        sensor5Chart.clear()
        sensor5Chart.data = lineData2
        sensor5Chart.invalidate()
    }

    override fun onStop() {
        chatViewModel.setScreen(Screens.MAIN_SCREEN)
        super.onStop()

    }

    override fun onPause() {
        chatViewModel.setScreen(Screens.NO_SCREEN)
        super.onPause()
    }

    override fun onResume() {
        chatViewModel.setScreen(Screens.CHART_SCREEN)
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }
}