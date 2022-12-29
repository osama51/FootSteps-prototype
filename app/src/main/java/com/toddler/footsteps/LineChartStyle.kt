package com.toddler.footsteps


import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.model.GradientColor


class LineChartStyle (private val context: Context) {

    fun styleChart(lineChart: LineChart) = lineChart.apply {
        axisLeft.isEnabled = false

        axisRight.apply {
            isEnabled = true
//            axisMinimum = 40f
//            axisMaximum = 60f
//            isAutoScaleMinMaxEnabled = true // ruins the last-50-values monitoring and fore displays the whole graph
            setDrawGridLines(false)

        }

        xAxis.apply {
            isGranularityEnabled = true
            granularity = 10f
            setDrawGridLines(false)
            setDrawAxisLine(true)
            position = XAxis.XAxisPosition.BOTTOM
        }

        setTouchEnabled(true)
        isDragEnabled = true
        setScaleEnabled(false)
        setPinchZoom(true)

        description = null
        legend.isEnabled = false
    }

    fun styleLineDataSet(lineDataSet: LineDataSet) = lineDataSet.apply {
        setDrawCircles(false)
        setDrawVerticalHighlightIndicator(true)
        isHighlightEnabled = true
//        valueTextColor = ContextCompat.getColor(context, R.color.icons)
        setDrawValues(false)
        lineWidth = 2f
        mode = LineDataSet.Mode.LINEAR

//        var gradientColor: GradientColor = GradientColor(ContextCompat.getColor(context, R.color.grey),ContextCompat.getColor(context, R.color.teal_500))
//        gradientColors = listOf(gradientColor)
//        setGradientColor(ContextCompat.getColor(context, R.color.grey),ContextCompat.getColor(context, R.color.teal_500))

        setDrawFilled(true)
        fillDrawable = ContextCompat.getDrawable(context, R.drawable.linechart_background)
    }

    // to apply gradient color on the graph (doesn't work.. applies the first color on the whole graph)
    fun drawLineGraph(lineChart: LineChart) {

        // Get the paint renderer to create the line shading.
        val paint: Paint = lineChart.renderer.paintRender
        val height: Float = lineChart.height.toFloat()
        val width: Float = lineChart.width.toFloat()
        val linGrad = LinearGradient(
            0f, 0f, width, height,
            ContextCompat.getColor(context, R.color.gray),
            ContextCompat.getColor(context, R.color.teal_700),
            Shader.TileMode.REPEAT
        )
        paint.shader = linGrad
    }
}