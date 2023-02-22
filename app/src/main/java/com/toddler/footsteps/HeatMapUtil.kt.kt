package com.toddler.footsteps

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.provider.CalendarContract
import android.util.Log
import androidx.collection.ArrayMap
import ca.hss.heatmaplib.HeatMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

enum class LeftRight {
    NONE,
    RIGHT,
    LEFT
}

enum class HeatMapMode {
    USER_FRIENDLY,
    SCIENTIFIC
}

class HeatMapUtil(private var activity: Activity,
    private var rightHeatMap: HeatMap,
    private var leftHeatMap: HeatMap
) {

    private lateinit var pointLA: HeatMap.DataPoint
    private lateinit var pointLB: HeatMap.DataPoint
    private lateinit var pointLC: HeatMap.DataPoint
    private lateinit var pointLD: HeatMap.DataPoint
    private lateinit var pointLE: HeatMap.DataPoint
    private lateinit var pointLF: HeatMap.DataPoint
    private lateinit var pointLG: HeatMap.DataPoint
    private lateinit var pointLH: HeatMap.DataPoint
    private lateinit var pointLI: HeatMap.DataPoint
    private lateinit var pointLJ: HeatMap.DataPoint
    private lateinit var pointLK: HeatMap.DataPoint

    private lateinit var pointRA: HeatMap.DataPoint
    private lateinit var pointRB: HeatMap.DataPoint
    private lateinit var pointRC: HeatMap.DataPoint
    private lateinit var pointRD: HeatMap.DataPoint
    private lateinit var pointRE: HeatMap.DataPoint
    private lateinit var pointRF: HeatMap.DataPoint
    private lateinit var pointRG: HeatMap.DataPoint
    private lateinit var pointRH: HeatMap.DataPoint
    private lateinit var pointRI: HeatMap.DataPoint
    private lateinit var pointRJ: HeatMap.DataPoint
    private lateinit var pointRK: HeatMap.DataPoint

    private var pixels: Double = 0.0

    private lateinit var ExecutorRightFoot: Handler
    private lateinit var ExecutorLeftFoot: Handler

    private var colorStops: ArrayMap<Float, Int> = ArrayMap()

    init {
        leftHeatMap.setMinimum(0.0)
        leftHeatMap.setMaximum(4095.0)

        rightHeatMap.setMinimum(0.0)
        rightHeatMap.setMaximum(4095.0)

        pixels = activity.resources.getDimensionPixelSize(R.dimen.heatmap_point).toDouble()


        //        //make the minimum opacity completely transparent
//        heatMap.setMinimumOpacity(0)
//        //make the maximum opacity 50% transparent
//        heatMap.setMaximumOpacity(255)

        userFriendlyTheme()

//        val y = (y + 45.0) * 10.0
//        Log.i("YYYYYYYYYYY", "$y")
//        val point = HeatMap.DataPoint(0.5F, 0.5F, y )
//        val point2 = HeatMap.DataPoint(0.2F, 0.5F, y / 2.0)
//        val point3 = HeatMap.DataPoint(0.8F, 0.5F, y / 2.0)
    }

    fun scientificTheme() {

        // ARGB --> 0xAARRGGBB
        //make the colour gradient from pink to yellow
        colorStops.put(0.0f, 0xff75898C.toInt()) // gray background

        colorStops.put(0.1f, 0x604294ff.toInt()) // bluish
        colorStops.put(0.15f, 0xa02684ff.toInt()) // bluish
        colorStops.put(0.20f, 0xff60f52a.toInt()) // greenish
        colorStops.put(0.35f, 0xff60f52a.toInt()) // greenish

        colorStops.put(0.4f, 0xfffff600.toInt()) // yellowish
        colorStops.put(0.6f, 0xfffff600.toInt()) // yellowish
        colorStops.put(0.65f, 0xfff9c320.toInt()) // yellowish

        colorStops.put(0.7f, 0xfff9aa20.toInt()) // reddish
        colorStops.put(0.75f, 0xaaff2100.toInt()) // reddish
        colorStops.put(0.85f, 0xaaff2100.toInt()) // reddish
        colorStops.put(1.0f, 0xaaff2100.toInt()) // reddish

//        colorStops.put(0.0f, 0xffee42f4.toInt());
//        colorStops.put(1.0f, 0xffeef442.toInt());
        leftHeatMap.setMinimumOpacity(220)
        leftHeatMap.setMaximumOpacity(250)

        rightHeatMap.setMinimumOpacity(220)
        rightHeatMap.setMaximumOpacity(250)

        leftHeatMap.setRadius(pixels)
        rightHeatMap.setRadius(pixels)

        leftHeatMap.setColorStops(colorStops)
        rightHeatMap.setColorStops(colorStops)

    }

    fun userFriendlyTheme() {

        // ARGB --> 0xAARRGGBB
        //make colour gradients of turquoise
        colorStops.put(0.0f, 0xff75898C.toInt()) // gray background

//        colorStops.put(0.05f, 0xff005af4.toInt()) // -0x11bd0c
//        colorStops.put(0.25f, 0xff00fff4.toInt()) //#4287f5 -0x110bbe
//        colorStops.put(1.0f, 0xff00fff4.toInt()) //#4287f5 -0x110bbe

        colorStops.put(0.1f, 0xff82ecff.toInt())  // turquoise
        colorStops.put(0.15f, 0xff00fff4.toInt()) // turquoise
        colorStops.put(0.20f, 0xff00fff4.toInt()) // turquoise

        colorStops.put(0.35f, 0xff00fff4.toInt()) // turquoise
        colorStops.put(0.4f, 0xff00fff4.toInt())  // turquoise
        colorStops.put(0.6f, 0xff00fff4.toInt())  // turquoise
        colorStops.put(0.65f, 0xff00fff4.toInt()) // turquoise

        colorStops.put(0.7f, 0xff00fff4.toInt())  // turquoise
        colorStops.put(0.75f, 0xff00fff4.toInt()) // turquoise
        colorStops.put(0.85f, 0xfff72d63.toInt()) // reddish
        colorStops.put(1.0f, 0xfff72d63.toInt())  // reddish

        leftHeatMap.setMinimumOpacity(30)
        leftHeatMap.setMaximumOpacity(250)

        rightHeatMap.setMinimumOpacity(30)
        rightHeatMap.setMaximumOpacity(250)

        leftHeatMap.setRadius(pixels)
        rightHeatMap.setRadius(pixels)

        leftHeatMap.setColorStops(colorStops)
        rightHeatMap.setColorStops(colorStops)
    }

    fun leftFootPoints(
        foot: HeatmapPoints
    ) {
//        y1Left = (y1 / 4095) * 100.0
//        y2Left = (y2 / 4095) * 100.0

//        ExecutorLeftFoot = Handler(Looper.getMainLooper())
//
//        ExecutorLeftFoot.post {
            // Perform task here with data

            pointLA = HeatMap.DataPoint(0.65F, 0.08F, (foot.sensor5 * 0.3) + (foot.sensor6 * 0.7))
            pointLB = HeatMap.DataPoint(0.33F, 0.17F, (foot.sensor5 * 0.7) + (foot.sensor6 * 0.3))
            pointLC = HeatMap.DataPoint(0.8F, 0.27F, foot.sensor6 / 1.0)
            pointLD = HeatMap.DataPoint(0.29F, 0.28F, foot.sensor5 / 1.0)
            pointLE = HeatMap.DataPoint(0.38F, 0.35F, foot.sensor4 / 1.0)
            pointLF = HeatMap.DataPoint(0.28F, 0.45F, (foot.sensor4+foot.sensor3) / 2.0)
            pointLG = HeatMap.DataPoint(0.27F, 0.55F, (foot.sensor4+foot.sensor3) / 2.0)
            pointLH = HeatMap.DataPoint(0.44F, 0.65F, foot.sensor3 / 2.0)
            pointLI = HeatMap.DataPoint(0.44F, 0.78F, foot.sensor3 / 1.0)
            pointLJ = HeatMap.DataPoint(0.64F, 0.78F, foot.sensor2 / 1.0)
            pointLK = HeatMap.DataPoint(0.56F, 0.91F, foot.sensor1 / 1.0)

            leftHeatMap.apply {
                addData(pointLA)
                addData(pointLB)
                addData(pointLC)
                addData(pointLD)
                addData(pointLE)
                addData(pointLF)
                addData(pointLG)
                addData(pointLH)
                addData(pointLI)
                addData(pointLJ)
                addData(pointLK)
//            forceRefresh()
                forceRefreshOnWorkerThread()
                invalidate()
//            postInvalidateOnAnimation()
            }

//            activity.runOnUiThread {
//                // Call function to run on UI thread with result
//                leftHeatMap.invalidate()
//            }
//        }
    }


    fun rightFootPoints(
        foot: HeatmapPoints
    ) {
//        y1Right = (y1 / 4095) * 100.0
//        y2Right = (y2 / 4095) * 100.0

//        ExecutorRightFoot = Handler(Looper.getMainLooper())

//        ExecutorRightFoot.post {
            // Perform task here with data

            pointRA = HeatMap.DataPoint(0.35F, 0.08F, ((foot.sensor5 * 0.3) + (foot.sensor6 * 0.7)) / 1.5)
            pointRB = HeatMap.DataPoint(0.67F, 0.17F, ((foot.sensor5 * 0.7) + (foot.sensor6 * 0.3)) / 2.0)
            pointRC = HeatMap.DataPoint(0.3F, 0.22F, foot.sensor6 / 1.0)
            pointRD = HeatMap.DataPoint(0.65F, 0.23F, foot.sensor5 / 1.0)
            pointRE = HeatMap.DataPoint(0.62F, 0.35F, foot.sensor4 / 1.0)
            pointRF = HeatMap.DataPoint(0.70F, 0.4F, (foot.sensor4+foot.sensor3) / 2.0)
            pointRG = HeatMap.DataPoint(0.65F, 0.5F, (foot.sensor4+foot.sensor3) / 2.0)
            pointRH = HeatMap.DataPoint(0.56F, 0.6F, foot.sensor3 / 2.0)
            pointRI = HeatMap.DataPoint(0.65F, 0.78F, foot.sensor3 / 1.0)
            pointRJ = HeatMap.DataPoint(0.3F, 0.78F, foot.sensor2 / 1.0)
            pointRK = HeatMap.DataPoint(0.44F, 0.91F, foot.sensor1 / 1.0)

//        val startTime = System.currentTimeMillis()
            rightHeatMap.apply {
                addData(pointRA)
                addData(pointRB)
                addData(pointRC)
                addData(pointRD)
                addData(pointRE)
                addData(pointRF)
                addData(pointRG)
                addData(pointRH)
                addData(pointRI)
                addData(pointRJ)
                addData(pointRK)
//            forceRefresh()                  // what I used the first time, Medhat's vid (smoother if given higher delays near 100ms)
                forceRefreshOnWorkerThread()
                invalidate()
//            postInvalidateOnAnimation()
            }
//        val endTime = System.currentTimeMillis()
//        val elapsedTime = endTime - startTime
//        Log.d("___RENDERRRR___", "Elapsed time: $elapsedTime milliseconds")

//            activity.runOnUiThread {
//                // Call function to run on UI thread with result
//                rightHeatMap.invalidate()
//            }
//        }
    }
}