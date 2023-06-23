package com.toddler.footsteps.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.toddler.footsteps.LineChartStyle
import com.toddler.footsteps.databinding.FragmentChartsBinding

class ChartsFragment : Fragment() {

    private lateinit var binding: FragmentChartsBinding

    private lateinit var leftLineChart: LineChart
    private lateinit var sensor6Chart: LineChart
    var leftf0LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var leftf1LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")

    var rightf0LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var rightf1LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var iLineDataSet: java.util.ArrayList<ILineDataSet> = java.util.ArrayList()
    private lateinit var lineData: LineData

    private lateinit var chartStyle: LineChartStyle


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChartsBinding.inflate(inflater, container, false)


//        blur()


//        leftf0LineDataSet.lineWidth = 3F
//        leftf0LineDataSet.formLineWidth = 3F
//        leftf1LineDataSet.lineWidth = 3F
//        leftf1LineDataSet.formLineWidth = 3F

        sensor6Chart = binding.sensor6Chart
//        rightf0LineDataSet.lineWidth = 3F
//        rightf0LineDataSet.formLineWidth = 3F
//        rightf1LineDataSet.lineWidth = 3F
//        rightf1LineDataSet.formLineWidth = 3F

        chartStyle = LineChartStyle(requireContext())
        chartStyle.styleChart(binding.sensor5Chart)
        chartStyle.styleChart(binding.sensor6Chart)

//        chartStyle.drawLineGraph(binding.tempChart)
//        chartStyle.drawLineGraph(binding.ecgChart)

        chartStyle.styleLineDataSet(leftf0LineDataSet)
        chartStyle.styleLineDataSet(leftf1LineDataSet)
        chartStyle.styleLineDataSet(rightf0LineDataSet)
        chartStyle.styleLineDataSet(rightf1LineDataSet)

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

    fun updateLeftGraphs(leftF0Data: MutableList<Entry?>, leftF1Data: MutableList<Entry?>) {
//        Log.i("updateLEFTgraph", "$leftF0Data")
        leftf0LineDataSet.values = leftF0Data
        leftf0LineDataSet.label = "FSR 0"

        leftf1LineDataSet.values = leftF1Data
        leftf1LineDataSet.label = "FSR 1"

        iLineDataSet.clear()
        iLineDataSet.add(leftf0LineDataSet)
        iLineDataSet.add(leftf1LineDataSet)
        lineData = LineData(iLineDataSet)

        leftLineChart.clear()
        leftLineChart.data = lineData
        leftLineChart.invalidate()
    }

    fun updateRightGraphs(rightF0Data: MutableList<Entry?>, rightF1Data: MutableList<Entry?>) {
        rightf0LineDataSet.values = rightF0Data
        rightf1LineDataSet.values = rightF1Data

        rightf0LineDataSet.label = "FSR 0"
        rightf1LineDataSet.label = "FSR 1"

        iLineDataSet.clear()
        iLineDataSet.add(rightf0LineDataSet)
        iLineDataSet.add(rightf1LineDataSet)
        lineData = LineData(iLineDataSet)

        sensor6Chart.clear()
        sensor6Chart.data = lineData
        sensor6Chart.invalidate()
    }

    override fun onStop() {
        super.onStop()

    }

    override fun onStart() {
        super.onStart()

    }
}