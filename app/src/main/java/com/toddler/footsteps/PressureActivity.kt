package com.toddler.footsteps

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.annotation.AnyThread
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.ArrayMap
import ca.hss.heatmaplib.HeatMap
import ca.hss.heatmaplib.HeatMap.OnMapClickListener
import ca.hss.heatmaplib.HeatMapMarkerCallback.CircleHeatMapMarker
import kotlinx.coroutines.delay
import java.util.*

/*
* MainActivity.java
*
* Copyright 2020 Heartland Software Solutions Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the license at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the LIcense is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

class PressureActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    private var map: HeatMap? = null
    private var testAsync = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pressure_layout)
        val box = findViewById<CheckBox>(R.id.change_async_status)
        box.setOnCheckedChangeListener(this)
        map = findViewById<HeatMap>(R.id.example_map)
        map?.let {
           it.apply {
               setMinimum(0.0)
               setMaximum(100.0)
               setLeftPadding(100)
               setRightPadding(100)
               setTopPadding(100)
               setBottomPadding(100)
               setMarkerCallback(CircleHeatMapMarker(-0x6bff2d))
               setRadius(80.0) }
        }
        val colors: MutableMap<Float, Int> = ArrayMap()
        //build a color gradient in HSV from red at the center to green at the outside
        for (i in 0..20) {
            val stop = i.toFloat() / 20.0f
            val color: Int = doGradient(
                (i * 5).toDouble(),
                0.0,
                100.0,
                -0xff0100,
                -0x10000
            )
            colors[stop] = color
        }
        map!!.setColorStops(colors)
        map!!.setOnMapClickListener(OnMapClickListener { x, y, closest ->
                addData()
        })
    }

    private fun addData() {
        if (testAsync) {
            AsyncTask.execute {
                while (true) {
                    try {
                        Thread.sleep(10)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    drawNewMap()
                    map!!.forceRefreshOnWorkerThread()
                    runOnUiThread {
                        map!!.invalidate()
                    }

                }
//                drawNewMap()
//                map!!.forceRefreshOnWorkerThread()

//                runOnUiThread { map!!.invalidate() }
            }
        } else {
            drawNewMap()
            map!!.forceRefresh()
        }
    }

    @AnyThread
    private fun drawNewMap() {
        map!!.clearData()
        val rand = Random()
        //add 20 random points of random intensity
        for (i in 0..19) {
            val point = HeatMap.DataPoint(
                clamp(rand.nextFloat(), 0.0f, 1.0f),
                clamp(rand.nextFloat(), 0.0f, 1.0f), clamp(rand.nextDouble(), 0.0, 100.0)
            )
            map!!.addData(point)
        }
    }

    private fun clamp(value: Float, min: Float, max: Float): Float {
        return value * (max - min) + min
    }

    private fun clamp(value: Double, min: Double, max: Double): Double {
        return value * (max - min) + min
    }

    override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {
        testAsync = !testAsync
    }

    companion object {
        private fun doGradient(
            value: Double,
            min: Double,
            max: Double,
            min_color: Int,
            max_color: Int
        ): Int {
            if (value >= max) {
                return max_color
            }
            if (value <= min) {
                return min_color
            }
            val hsvmin = FloatArray(3)
            val hsvmax = FloatArray(3)
            val frac = ((value - min) / (max - min)).toFloat()
            Color.RGBToHSV(
                Color.red(min_color),
                Color.green(min_color),
                Color.blue(min_color),
                hsvmin
            )
            Color.RGBToHSV(
                Color.red(max_color),
                Color.green(max_color),
                Color.blue(max_color),
                hsvmax
            )
            val retval = FloatArray(3)
            for (i in 0..2) {
                retval[i] = interpolate(
                    hsvmin[i],
                    hsvmax[i], frac
                )
            }
            return Color.HSVToColor(retval)
        }

        private fun interpolate(a: Float, b: Float, proportion: Float): Float {
            return a + (b - a) * proportion
        }
    }
}