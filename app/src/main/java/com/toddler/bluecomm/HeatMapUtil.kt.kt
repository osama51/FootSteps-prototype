package com.toddler.bluecomm

import androidx.collection.ArrayMap
import ca.hss.heatmaplib.HeatMap

enum class LeftRight {
    LEFT,
    RIGHT
}

class HeatMapUtil(
    private var rightHeatMap: HeatMap,
    private var leftHeatMap: HeatMap
) {

    private lateinit var pointLA: HeatMap.DataPoint
    private lateinit var pointLAA: HeatMap.DataPoint
    private lateinit var pointLB: HeatMap.DataPoint
    private lateinit var pointLC: HeatMap.DataPoint
    private lateinit var pointLD: HeatMap.DataPoint
    private lateinit var pointLE: HeatMap.DataPoint
    private lateinit var pointLF: HeatMap.DataPoint
    private lateinit var pointLG: HeatMap.DataPoint
    private lateinit var pointLH: HeatMap.DataPoint
    private lateinit var pointLI: HeatMap.DataPoint
    private lateinit var pointLK: HeatMap.DataPoint

    private lateinit var pointRA: HeatMap.DataPoint
    private lateinit var pointRAA: HeatMap.DataPoint
    private lateinit var pointRB: HeatMap.DataPoint
    private lateinit var pointRC: HeatMap.DataPoint
    private lateinit var pointRD: HeatMap.DataPoint
    private lateinit var pointRE: HeatMap.DataPoint
    private lateinit var pointRF: HeatMap.DataPoint
    private lateinit var pointRG: HeatMap.DataPoint
    private lateinit var pointRH: HeatMap.DataPoint
    private lateinit var pointRI: HeatMap.DataPoint
    private lateinit var pointRK: HeatMap.DataPoint

    private var y1Left: Double = 0.0
    private var y2Left: Double = 0.0
    private var y1Right: Double = 0.0
    private var y2Right: Double = 0.0

    init {
        leftHeatMap.setMinimum(0.0)
        leftHeatMap.setMaximum(100.0)

        rightHeatMap.setMinimum(0.0)
        rightHeatMap.setMaximum(100.0)

        //        //make the minimum opacity completely transparent
//        heatMap.setMinimumOpacity(0)
//        //make the maximum opacity 50% transparent
//        heatMap.setMaximumOpacity(255)

        //make the colour gradient from pink to yellow
        val colorStops: ArrayMap<Float, Int> = ArrayMap()
        colorStops.put(0.0f, 0xff005af4.toInt()) // -0x11bd0c
        colorStops.put(0.2f, 0x9900fff4.toInt()) //#4287f5 -0x110bbe
        colorStops.put(1.0f, 0x9900fff4.toInt()) //#4287f5 -0x110bbe
        leftHeatMap.setColorStops(colorStops)
        rightHeatMap.setColorStops(colorStops)

//        val y = (y + 45.0) * 10.0
//        Log.i("YYYYYYYYYYY", "$y")
//        val point = HeatMap.DataPoint(0.5F, 0.5F, y )
//        val point2 = HeatMap.DataPoint(0.2F, 0.5F, y / 2.0)
//        val point3 = HeatMap.DataPoint(0.8F, 0.5F, y / 2.0)

    }

    fun leftFootPoints(
        y1: Double,
        y2: Double
    ) {
        y1Left = (y1 / 4095) * 100.0
        y2Left = (y2 / 4095) * 100.0

        pointLA = HeatMap.DataPoint(0.56F, 0.08F, 0 / 1.0)
        pointLAA = HeatMap.DataPoint(0.33F, 0.1F, 0 / 1.0)
        pointLB = HeatMap.DataPoint(0.7F, 0.27F, y1Left / 1.0)
        pointLC = HeatMap.DataPoint(0.29F, 0.28F, 0 / 1.0)
        pointLD = HeatMap.DataPoint(0.38F, 0.35F, 0 / 3.0)
        pointLE = HeatMap.DataPoint(0.28F, 0.45F, 0 / 1.0)
        pointLF = HeatMap.DataPoint(0.27F, 0.55F, 0 / 1.0)
        pointLG = HeatMap.DataPoint(0.44F, 0.65F, 0 / 3.0)
        pointLH = HeatMap.DataPoint(0.44F, 0.78F, y2Left / 1.0)
        pointLI = HeatMap.DataPoint(0.64F, 0.89F, 0 / 1.0)
        pointLK = HeatMap.DataPoint(0.56F, 0.91F, 0 / 1.0)

        leftHeatMap.apply {
            addData(pointLA)
            addData(pointLAA)
            addData(pointLB)
            addData(pointLC)
            addData(pointLD)
            addData(pointLE)
            addData(pointLF)
            addData(pointLG)
            addData(pointLH)
            addData(pointLI)
            addData(pointLK)
            forceRefresh()
        }
    }


    fun rightFootPoints(
        y1: Double,
        y2: Double
    ) {
        y1Right = (y1 / 4095) * 100.0
        y2Right = (y2 / 4095) * 100.0

        pointRA = HeatMap.DataPoint(0.44F, 0.08F, 0 / 1.0)
        pointRAA = HeatMap.DataPoint(0.67F, 0.1F, 0 / 1.0)
        pointRB = HeatMap.DataPoint(0.3F, 0.27F, y1Right / 1.0)
        pointRC = HeatMap.DataPoint(0.71F, 0.28F, 0 / 1.0)
        pointRD = HeatMap.DataPoint(0.62F, 0.35F, 0 / 3.0)
        pointRE = HeatMap.DataPoint(0.72F, 0.45F, 0 / 1.0)
        pointRF = HeatMap.DataPoint(0.73F, 0.55F, 0 / 1.0)
        pointRG = HeatMap.DataPoint(0.56F, 0.65F, 0 / 3.0)
        pointRH = HeatMap.DataPoint(0.56F, 0.78F, y2Right / 1.0)
        pointRI = HeatMap.DataPoint(0.36F, 0.89F, 0 / 1.0)
        pointRK = HeatMap.DataPoint(0.44F, 0.91F, 0 / 1.0)

        rightHeatMap.apply {
            addData(pointRA)
            addData(pointRAA)
            addData(pointRB)
            addData(pointRC)
            addData(pointRD)
            addData(pointRE)
            addData(pointRF)
            addData(pointRG)
            addData(pointRH)
            addData(pointRI)
            addData(pointRK)
            forceRefresh()
        }
    }
}