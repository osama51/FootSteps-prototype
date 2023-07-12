package com.toddler.footsteps

import android.os.Handler
import androidx.collection.ArrayMap
import ca.hss.heatmaplib.HeatMap
import com.toddler.footsteps.heatmap.Insole
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

enum class LeftRight {
    NONE,
    RIGHT,
    LEFT
}

enum class HeatMapMode {
    USER_FRIENDLY,
    ANALYTIC
}

class HeatMapUtil(
    private var activity: MainActivity,
    private var rightHeatMap: HeatMap,
    private var leftHeatMap: HeatMap
) {

    private var pointLA: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointLB: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointLC: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointLD: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointLD2: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointLE: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointLF: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointLG: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointLH: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointLI: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointLJ: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointLK: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)

    private var pointRA: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointRB: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointRC: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointRD: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointRD2: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointRE: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointRF: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointRG: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointRH: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointRI: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointRJ: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)
    private var pointRK: HeatMap.DataPoint  = HeatMap.DataPoint(0.0F, 0.0F,0.0)

    private var pixels: Double = 650.0

    private lateinit var ExecutorRightFoot: Handler
    private lateinit var ExecutorLeftFoot: Handler

    private var colorStops: ArrayMap<Float, Int> = ArrayMap()

    /**
     * IMPORTANT:
     * I have learned two things now:
     * 1. Setting points next to each other will cause the intersection to add up their intensities
     * (which explains why it gets red no matter how low the intensity I receive from the ESP is,
     * currently reaches red at 2500/4095, which is 61% of the max value! clearly visible in the user friendly mode)
     * 2. Low Opacity is my number one enemy, the lower the opacity the more processing power it takes! (proved wrong)
     * 3. I guess the blur would also be a factor, the higher the blur the more processing power it takes! (proved wrong)
     *
     * 4. Actually it has nothing to do with the opacity or the blur, not even the number of points that are being added
     *    at least for the number we have here, which is not that much, turns out there's that attribute called
     *    "maxDrawingHeight" and "maxDrawingWidth" which seem to be the culprit, the higher the value the more processing power it takes
     *    to draw the heat map, it also seems to be the reason why the heat map is not being drawn correctly, because it is being drawn
     *    on a smaller scale than the actual view, so the points are being drawn on a smaller scale than the actual view,
     *    so I guess the solution would be to set the maxDrawingHeight and maxDrawingWidth to the actual view's height and width,
     *    it's about trial and error now tbh. It also seems to affect the radius of the points, the higher the value the smaller the radius
     *    and vice versa. blurring the heat map also seems to be affected by this attribute, the higher the value the more blur it gets
     *
     *    UPDATE: I have set the maxDrawingHeight and maxDrawingWidth to numbers smaller than 100, and it seems to be working fine now,
     *
     * 5. Adding blur attribute gives the points nicer edges and doesn't affect the processing power that much, so I guess it's a win win
     *
     * 6. I have learned that the heat map library is not thread safe, so we need to use a single thread executor
     *
     *  */

    init {
        leftHeatMap.setMinimum(0.0)
        leftHeatMap.setMaximum(4095.0)

        rightHeatMap.setMinimum(0.0)
        rightHeatMap.setMaximum(4095.0) // 4095.0

//        pixels = activity.resources.getDimensionPixelSize(R.dimen.heatmap_point).toDouble()

        userFriendlyTheme()

    }

    // Apparently, the heat map library is not thread safe, so we need to use a single thread executor
    // to ensure that the data points are added in the correct order.
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    // This single thread executor is used to ensure that the heat map is cleared in the correct order.
    // and this is how it is used:
    // executorService.execute {
    //    heatMap.clearData()
    // }


    fun addDataPoint(x: Float, y: Float, z: Double, leftRight: LeftRight) {
        executorService.execute {
            val point = HeatMap.DataPoint(x, y, z)
            when (leftRight) {
                LeftRight.LEFT -> {
                    leftHeatMap.addData(point)
                }
                LeftRight.RIGHT -> {
                    rightHeatMap.addData(point)
                }
                else -> {
                    // do nothing
                }
            }
        }
    }

    fun clearDataPoints(leftRight: LeftRight) {
        executorService.execute {
            when (leftRight) {
                LeftRight.LEFT -> {
                    leftHeatMap.clearData()
                }
                LeftRight.RIGHT -> {
                    rightHeatMap.clearData()
                }
                else -> {
                    // do nothing
                }
            }
        }
    }


    fun setRadius(radius: Double) {
        leftHeatMap.setRadius(radius)
        rightHeatMap.setRadius(radius)
    }

    fun setOpacity(opacity: Int) {
        leftHeatMap.setOpacity(opacity)
        rightHeatMap.setOpacity(opacity)
    }

    fun setBlur(blur: Double) {
        leftHeatMap.setBlur(blur)
        rightHeatMap.setBlur(blur)
    }


    fun leftFootPoints(
        foot: Insole
    ) {
//        y1Left = (y1 / 4095) * 100.0
//        y2Left = (y2 / 4095) * 100.0

//        ExecutorLeftFoot = Handler(Looper.getMainLooper())
//
//        ExecutorLeftFoot.post {
        // Perform task here with data

        // Use a single thread executor to ensure that the data points are added in the correct order.
        executorService.execute {
            // Clear the data points before adding new ones.
            leftHeatMap.clearData()

            // Add the data points.
            // The data points are added in the order of the points in the array.
            // The first point in the array is the top left corner of the heat map.
            // The last point in the array is the bottom right corner

            pointLC = HeatMap.DataPoint(0.6F, 0.1F, foot.sensor6 / 1.0) // great toe

            pointLD2 = HeatMap.DataPoint(0.7F, 0.255F, foot.sensor5 / 2.0)
            pointLD = HeatMap.DataPoint(0.75F, 0.25F, foot.sensor5 / 1.0)

            pointLB = HeatMap.DataPoint(0.3F, 0.25F, foot.sensor4 / 0.70)

            pointLE = HeatMap.DataPoint(0.3F, 0.35F, (foot.sensor3 / 2.5) + (foot.sensor4 / 2.5))
            pointLF = HeatMap.DataPoint(0.27F, 0.45F, foot.sensor3 / 1.0)

            pointLI = HeatMap.DataPoint(0.3F, 0.6F, (foot.sensor2 / 3.0) + (foot.sensor3 / 3.0))
            pointLH = HeatMap.DataPoint(0.4F, 0.7F, foot.sensor2 / 1.0)   // fake point

            pointLG = HeatMap.DataPoint(0.65F, 0.78F, foot.sensor1 / 2.0) // fake point
            pointLJ = HeatMap.DataPoint(0.35F, 0.79F, foot.sensor1 / 2.0) // fake point
            pointLK = HeatMap.DataPoint(0.56F, 0.91F, foot.sensor1 / 1.0)


            leftHeatMap.apply {
//            addData(pointLA)
                addData(pointLB)
                addData(pointLC)
                addData(pointLD)
                addData(pointLD2)
                addData(pointLE)
                addData(pointLF)
                addData(pointLG)
                addData(pointLH)
                addData(pointLI)
                addData(pointLJ)
                addData(pointLK)
////            forceRefresh()
//            forceRefreshOnWorkerThread()
//            invalidate()
////            postInvalidateOnAnimation()
            }

            leftHeatMap.postInvalidate()

            // run this part from the UI thread
            activity.runOnUiThread {
                try {
                    leftHeatMap.forceRefreshOnWorkerThread()
//                rightHeatMap.forceRefreshOnWorkerThread()
                } catch (e: Exception) {
                    e.printStackTrace()
                    // clear the arrays
                    leftHeatMap.clearData()
//                rightHeatMap.clearData()
                }
            }
        }


//            activity.runOnUiThread {
//                // Call function to run on UI thread with result
//                leftHeatMap.invalidate()
//            }
//        }
    }


    fun rightFootPoints(
        foot: Insole
    ) {
//        y1Right = (y1 / 4095) * 100.0
//        y2Right = (y2 / 4095) * 100.0

//        ExecutorRightFoot = Handler(Looper.getMainLooper())

//        ExecutorRightFoot.post {
        // Perform task here with data

        // Use a single thread executor to ensure that the data points are added in the correct order.
        executorService.execute {
            // Clear the data points before adding new ones.
            rightHeatMap.clearData()


//        pointRA = HeatMap.DataPoint(0.25F, 0.55F, ((foot.sensor5 * 0.5) + (foot.sensor3 * 0.5)) / 2.0)
            pointRC = HeatMap.DataPoint(0.4F, 0.1F, foot.sensor6 / 1.0)

            pointRD2 = HeatMap.DataPoint(0.3F, 0.255F, foot.sensor5 / 2.0)
            pointRD = HeatMap.DataPoint(0.25F, 0.25F, foot.sensor5 / 1.0)

            pointRB = HeatMap.DataPoint(0.7F, 0.25F, foot.sensor4 / 0.70)

            pointRE = HeatMap.DataPoint(0.7F, 0.35F, (foot.sensor3 / 2.5) + (foot.sensor4 / 2.5))
            pointRF = HeatMap.DataPoint(0.73F, 0.45F, foot.sensor3 / 1.0)

            pointRI = HeatMap.DataPoint(0.7F, 0.6F, (foot.sensor2 / 3.0) + (foot.sensor3 / 3.0))
            pointRH = HeatMap.DataPoint(0.6F, 0.7F, foot.sensor2 / 1.0)   // fake point

            pointRG = HeatMap.DataPoint(0.35F, 0.79F, foot.sensor1 / 2.0) // fake point
            pointRJ = HeatMap.DataPoint(0.65F, 0.78F, foot.sensor1 / 2.0)
            pointRK = HeatMap.DataPoint(0.44F, 0.91F, foot.sensor1 / 1.0)

//        val startTime = System.currentTimeMillis()
            rightHeatMap.apply {
//            addData(pointRA)
                addData(pointRB)
                addData(pointRC)
                addData(pointRD)
                addData(pointRD2)
                addData(pointRE)
                addData(pointRF)
                addData(pointRG)
                addData(pointRH)
                addData(pointRI)
                addData(pointRJ)
                addData(pointRK)
////            forceRefresh()                  // what I used the first time, Medhat's vid (smoother if given higher delays near 100ms)
//            forceRefreshOnWorkerThread()
//            invalidate()
////            postInvalidateOnAnimation()
            }
//        val endTime = System.currentTimeMillis()
//        val elapsedTime = endTime - startTime
//        Log.d("___RENDERRRR___", "Elapsed time: $elapsedTime milliseconds")


            rightHeatMap.postInvalidate()

            // run this part from the UI thread
            activity.runOnUiThread {
                try {
//                leftHeatMap.forceRefreshOnWorkerThread()
                    rightHeatMap.forceRefreshOnWorkerThread()
                } catch (e: Exception) {
                    e.printStackTrace()
                    // clear the arrays
//                leftHeatMap.clearData()
                    rightHeatMap.clearData()
                }
            }
        }
    }

//            activity.runOnUiThread {
//                // Call function to run on UI thread with result
//                rightHeatMap.invalidate()
//            }
//        }

    fun analyticTheme() {

        // ARGB --> 0xAARRGGBB
        //make the colour gradient from pink to yellow
//        colorStops.put(0.0f, 0xff75898C.toInt()) // gray background
//
//        colorStops.put(0.25f, 0xff4294ff.toInt()) // bluish
////        colorStops.put(0.15f, 0xff2684ff.toInt()) // bluish
////        colorStops.put(0.20f, 0xff60f52a.toInt()) // greenish
//        colorStops.put(0.35f, 0xff60f52a.toInt()) // greenish
//
//        colorStops.put(0.4f, 0xfffff600.toInt()) // yellowish
//        colorStops.put(0.6f, 0xfffff600.toInt()) // yellowish
//        colorStops.put(0.65f, 0xfff9c320.toInt()) // yellowish
//
//        colorStops.put(0.7f, 0xfff9aa20.toInt()) // reddish
//        colorStops.put(0.75f, 0xffff2100.toInt()) // reddish
//        colorStops.put(0.85f, 0xffff2100.toInt()) // reddish
//        colorStops.put(0.99f, 0xffff2100.toInt()) // reddish


        colorStops.put(0.0f, 0xff75898C.toInt()) // gray background

        colorStops.put(0.1f, 0xff0000ff.toInt()) // blue

        colorStops.put(0.25f, 0xff00ffff.toInt()) // cyan
//        colorStops.put(0.15f, 0xff2684ff.toInt()) // bluish
//        colorStops.put(0.20f, 0xff60f52a.toInt()) // greenish
        colorStops.put(0.35f, 0xff00ff00.toInt()) // green

        colorStops.put(0.4f, 0xff00ff00.toInt()) // green
        colorStops.put(0.6f, 0xfffff600.toInt()) // yellowish
        colorStops.put(0.65f, 0xffffff00.toInt()) // yellowish

        colorStops.put(0.7f, 0xffffa500.toInt()) // orange
        colorStops.put(0.75f, 0xffff2100.toInt()) // reddish
        colorStops.put(0.85f, 0xffff2100.toInt()) // reddish
        colorStops.put(0.99f, 0xffff2100.toInt()) // reddish


//        colorStops.put(0.7f, 0xfff9c320.toInt()) // reddish
//        colorStops.put(0.75f, 0xfff9c320.toInt()) // reddish
//        colorStops.put(0.85f, 0xfff9c320.toInt()) // reddish
//        colorStops.put(1.0f, 0xaaff2100.toInt()) // reddish

//        colorStops.put(0.0f, 0xffee42f4.toInt());
//        colorStops.put(1.0f, 0xffeef442.toInt());

//        leftHeatMap.setRadius(pixels)
//        rightHeatMap.setRadius(pixels)

        leftHeatMap.setColorStops(colorStops)
        rightHeatMap.setColorStops(colorStops)
    }

    fun userFriendlyTheme() {

        // ARGB --> 0xAARRGGBB
        //make colour gradients of turquoise
        colorStops.put(0.0f, 0xff75898C.toInt()) // gray background

        colorStops.put(0.1f, 0xff73D3CE.toInt()) // blue

//        colorStops.put(0.05f, 0xff005af4.toInt()) // -0x11bd0c
//        colorStops.put(0.25f, 0xff00fff4.toInt()) //#4287f5 -0x110bbe
//        colorStops.put(1.0f, 0xff00fff4.toInt()) //#4287f5 -0x110bbe

//        colorStops.put(0.1f, 0xff82ecff.toInt())  // turquoise
//        colorStops.put(0.25f, 0xffffffff.toInt())  // turquoise
//        colorStops.put(0.15f, 0xff00fff4.toInt()) // turquoise
        colorStops.put(0.25f, 0xff00fff4.toInt()) // turquoise

        colorStops.put(0.35f, 0xff00fff4.toInt()) // turquoise
        colorStops.put(0.4f, 0xff00fff4.toInt())  // turquoise
        colorStops.put(0.6f, 0xff00fff4.toInt())  // turquoise
        colorStops.put(0.65f, 0xff00fff4.toInt()) // turquoise

        colorStops.put(0.7f, 0xff00fff4.toInt())  // turquoise
        colorStops.put(0.75f, 0xff00fff4.toInt()) // turquoise
        colorStops.put(0.85f, 0xfff72d63.toInt()) // reddish
        colorStops.put(0.99f, 0xfff72d63.toInt())  // reddish

//        rightHeatMap.setOpacity(0)
//        leftHeatMap.setOpacity(0)
//
//        leftHeatMap.setMinimumOpacity(0)
//        leftHeatMap.setMaximumOpacity(255)
//
//        rightHeatMap.setMinimumOpacity(0)
//        rightHeatMap.setMaximumOpacity(255)

//        leftHeatMap.setRadius(pixels)
//        rightHeatMap.setRadius(pixels)

        leftHeatMap.setColorStops(colorStops)
        rightHeatMap.setColorStops(colorStops)

//        setBlur(0.78)
    }
}
