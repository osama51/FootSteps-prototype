package com.toddler.footsteps


import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter


class LineChartStyle (private val context: Context) {

    fun styleChart(lineChart: LineChart) = lineChart.apply {
        axisRight.isEnabled = false

        axisLeft.axisMaximum = 5000f

        axisLeft.apply {
            isEnabled = true
            axisMinimum = -1000f
            axisMaximum = 4500f
//            isAutoScaleMinMaxEnabled = true // ruins the last-50-values monitoring and fore displays the whole graph
            isGranularityEnabled = true
            granularity = 1400f
            setDrawGridLines(true)
//            setVisibleYRange(-100f, 5000f, YAxis.AxisDependency.LEFT)
            gridLineWidth =0.5f
            gridColor = ContextCompat.getColor(context, R.color.gray_opaque)
            setDrawAxisLine(false)

            setDrawLabels(true)
            valueFormatter = AxisValueFormatter()
            // change the font of the labels
            typeface = ResourcesCompat.getFont(context, R.font.chakra_petch_light)
        }

        xAxis.apply {
            isEnabled = true
            isGranularityEnabled = true
            granularity = 10f
            setDrawLabels(false)
            setDrawGridLines(true)
//            setVisibleYRange(-100f, 5000f, YAxis.AxisDependency.LEFT)
            gridLineWidth =0.5f
            gridColor = ContextCompat.getColor(context, R.color.gray_opaque)
            setDrawAxisLine(true)
            position = XAxis.XAxisPosition.BOTTOM
            typeface = ResourcesCompat.getFont(context, R.font.chakra_petch_light)
        }

        setTouchEnabled(false)
        isDragEnabled = false
        setScaleEnabled(true)
        setPinchZoom(true)

        description = null
        legend.isEnabled = true
        legend.typeface = ResourcesCompat.getFont(context, R.font.chakra_petch_light)
    }

    fun styleLineDataSet(lineDataSet: LineDataSet, leftRight: LeftRight) = lineDataSet.apply {
        setDrawCircles(false)
        setDrawVerticalHighlightIndicator(true)
        isHighlightEnabled = true
//        valueTextColor = ContextCompat.getColor(context, R.color.icons)
        setDrawValues(false)
        lineWidth = 2f
//        mode = LineDataSet.Mode.LINEAR
        mode = LineDataSet.Mode.CUBIC_BEZIER
//        cubicIntensity = 0.1f

//        var gradientColor: GradientColor = GradientColor(ContextCompat.getColor(context, R.color.grey),ContextCompat.getColor(context, R.color.teal_500))
//        gradientColors = listOf(gradientColor)
//        setGradientColor(ContextCompat.getColor(context, R.color.grey),ContextCompat.getColor(context, R.color.teal_500))

        setDrawFilled(true)
        fillDrawable = if(leftRight == LeftRight.LEFT)
            ContextCompat.getDrawable(context, R.drawable.linechart_background_left)
        else
            ContextCompat.getDrawable(context, R.drawable.linechart_background_right)
    }

    // to apply gradient color on the graph (doesn't work.. applies the first color on the whole graph)
    fun drawLineGraph(lineChart: LineChart) {
        // Get the paint renderer to create the line shading.
        val paint: Paint = lineChart.renderer.paintRender
        val height: Float = lineChart.height.toFloat()
        val width: Float = lineChart.width.toFloat()
        val linGrad = LinearGradient(
            0f, 0f, width, height,
            ContextCompat.getColor(context, R.color.gray_teal),
            ContextCompat.getColor(context, R.color.teal_700),
            Shader.TileMode.REPEAT
        )
        paint.shader = linGrad
    }
}

class AxisValueFormatter: ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        // change the range of the values on the y-axis from 0-5000 to 0%-100%

        var value = value
        value = value / 4200 * 100
        return value.toInt().toString() + "%"
    }
}