package com.toddler.footsteps.ui

import android.os.Bundle
import android.util.Log
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


    var leftf1LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var leftf2LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var leftf3LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var leftf4LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var leftf5LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var leftf6LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")

    var rightf1LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var rightf2LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var rightf3LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var rightf4LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var rightf5LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var rightf6LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")

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


//        leftf0LineDataSet.lineWidth = 3F
//        leftf0LineDataSet.formLineWidth = 3F
//        leftf1LineDataSet.lineWidth = 3F
//        leftf1LineDataSet.formLineWidth = 3F

        sensor6Chart = binding.sensor6Chart
        sensor5Chart = binding.sensor5Chart
        sensor4Chart = binding.sensor4Chart
        sensor3Chart = binding.sensor3Chart
        sensor2Chart = binding.sensor2Chart
        sensor1Chart = binding.sensor1Chart

//        rightf0LineDataSet.lineWidth = 3F
//        rightf0LineDataSet.formLineWidth = 3F
//        rightf1LineDataSet.lineWidth = 3F
//        rightf1LineDataSet.formLineWidth = 3F

        chartStyle = LineChartStyle(requireContext())

        chartStyle.styleChart(sensor6Chart)
        chartStyle.styleChart(sensor5Chart)
        chartStyle.styleChart(sensor4Chart)
        chartStyle.styleChart(sensor3Chart)
        chartStyle.styleChart(sensor2Chart)
        chartStyle.styleChart(sensor1Chart)


//        chartStyle.drawLineGraph(binding.sensor5Chart)
//        chartStyle.drawLineGraph(binding.sensor6Chart)

        chartStyle.styleLineDataSet(leftf6LineDataSet, LeftRight.LEFT)
        chartStyle.styleLineDataSet(rightf6LineDataSet, LeftRight.RIGHT)

        chartStyle.styleLineDataSet(leftf5LineDataSet, LeftRight.LEFT)
        chartStyle.styleLineDataSet(rightf5LineDataSet, LeftRight.RIGHT)

        chartStyle.styleLineDataSet(leftf4LineDataSet, LeftRight.LEFT)
        chartStyle.styleLineDataSet(rightf4LineDataSet, LeftRight.RIGHT)

        chartStyle.styleLineDataSet(leftf3LineDataSet, LeftRight.LEFT)
        chartStyle.styleLineDataSet(rightf3LineDataSet, LeftRight.RIGHT)

        chartStyle.styleLineDataSet(leftf2LineDataSet, LeftRight.LEFT)
        chartStyle.styleLineDataSet(rightf2LineDataSet, LeftRight.RIGHT)

        chartStyle.styleLineDataSet(leftf1LineDataSet, LeftRight.LEFT)
        chartStyle.styleLineDataSet(rightf1LineDataSet, LeftRight.RIGHT)


//        test()
        chartsViewModel.right5.observe(viewLifecycleOwner) {
//            Log.i("ChartsFragment", "right5: ${it}")
//            Log.i("ChartsFragment", "left5 ${chartsViewModel.left5.value?.get(0)}")
            updateGraphRight(it, 5, rightf6LineDataSet, leftf6LineDataSet, iLineDataSet6, sensor6Chart, arrayListOf(lineData6))
        }

//        chartsViewModel.left5.observe(viewLifecycleOwner) {
//            updateGraphLeft(it, leftf6LineDataSet, iLineDataSet6, sensor6Chart, arrayListOf(lineData6))
//        }

        chartsViewModel.right4.observe(viewLifecycleOwner) {
            updateGraphRight( it, 4, rightf5LineDataSet, leftf5LineDataSet, iLineDataSet5, sensor5Chart, arrayListOf(lineData5))
        }

//        chartsViewModel.left4.observe(viewLifecycleOwner) {
//            updateGraphLeft(it, leftf5LineDataSet, iLineDataSet5, sensor5Chart, arrayListOf(lineData5))
//        }

        chartsViewModel.right3.observe(viewLifecycleOwner) {
            updateGraphRight(it, 3, rightf4LineDataSet, leftf4LineDataSet, iLineDataSet4, sensor4Chart, arrayListOf(lineData4))
        }

//        chartsViewModel.left3.observe(viewLifecycleOwner) {
//            updateGraphLeft(it, leftf4LineDataSet, iLineDataSet4, sensor4Chart, arrayListOf(lineData4))
//        }

        chartsViewModel.right2.observe(viewLifecycleOwner) {
            updateGraphRight(it, 2, rightf3LineDataSet, leftf3LineDataSet, iLineDataSet3, sensor3Chart, arrayListOf(lineData3))
        }

//        chartsViewModel.left2.observe(viewLifecycleOwner) {
//            updateGraphLeft(it, leftf3LineDataSet, iLineDataSet3, sensor3Chart, arrayListOf(lineData3))
//        }

        chartsViewModel.right1.observe(viewLifecycleOwner) {
            updateGraphRight(it, 1, rightf2LineDataSet, leftf2LineDataSet, iLineDataSet2, sensor2Chart, arrayListOf(lineData2))
        }

//        chartsViewModel.left1.observe(viewLifecycleOwner) {
//            updateGraphLeft(it, leftf2LineDataSet, iLineDataSet2, sensor2Chart, arrayListOf(lineData2))
//        }

        chartsViewModel.right0.observe(viewLifecycleOwner) {
            updateGraphRight(it, 0, rightf1LineDataSet, leftf1LineDataSet, iLineDataSet1, sensor1Chart, arrayListOf(lineData1))
        }

//        chartsViewModel.left0.observe(viewLifecycleOwner) {
//            updateGraphLeft(it, leftf1LineDataSet, iLineDataSet1, sensor1Chart, arrayListOf(lineData1))
//        }




        return binding.root
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

    private fun initRightGraphs(){
        for(i in arrayListOf(rightf6LineDataSet, rightf5LineDataSet, rightf4LineDataSet, rightf3LineDataSet, rightf2LineDataSet, rightf1LineDataSet)){
            i.label = "Right"
            i.color = resources.getColor(R.color.lightGreen)
        }
    }

    private fun initLeftGraphs(){
        for(i in arrayListOf(leftf6LineDataSet, leftf5LineDataSet, leftf4LineDataSet, leftf3LineDataSet, leftf2LineDataSet, leftf1LineDataSet)){
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
//            rightf6LineDataSet.addEntry(chartsViewModel.right5.value?.last()!!)
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

    fun test(){
        if (chatViewModel.screen.value == Screens.CHART_SCREEN){
            leftf6LineDataSet.values = chartsViewModel.leftSensorsList[5].value
//        leftLineDataSet.label = "Left"
//        leftLineDataSet.color = resources.getColor(R.color.lightRed)

//
//            exampleData.addEntry(...);
//            chart.notifyDataSetChanged(); // let the chart know it's data changed
//            chart.invalidate(); // refresh

            rightf6LineDataSet.values = chartsViewModel.right5.value


//            iLineDataSet.clear()
//            iLineDataSet.add(leftLineDataSet)
            iLineDataSet6.add(rightf6LineDataSet)

            lineData6 = LineData(iLineDataSet6)

//            chart.clear()
            sensor6Chart.data = lineData6
            sensor6Chart.invalidate()
        }
    }

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

        rightf6LineDataSet.values = leftData
        rightf6LineDataSet.label = "Left"
        rightf6LineDataSet.color = resources.getColor(R.color.lightRed)

        rightf1LineDataSet.values = rightData
        rightf1LineDataSet.label = "Right"
        rightf1LineDataSet.color = resources.getColor(R.color.lightGreen)

        iLineDataSet2.clear()
        iLineDataSet2.add(rightf6LineDataSet)
        iLineDataSet2.add(rightf1LineDataSet)
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