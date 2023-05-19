package com.toddler.footsteps

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.os.*
import android.util.Log
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ca.hss.heatmaplib.HeatMap
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.toddler.footsteps.bluetooth.DeviceListActivity
import com.toddler.footsteps.chat.ChatAdapter
import com.toddler.footsteps.chat.ChatViewModel
import com.toddler.footsteps.database.rawdata.LeftFootFrame
import com.toddler.footsteps.database.reference.User
import com.toddler.footsteps.databinding.ActivityMainBinding
import com.toddler.footsteps.heatmap.HeatMapViewModel
import com.toddler.footsteps.heatmap.Insole
import com.toddler.footsteps.navbar.CustomBottomNavBar
import com.toddler.footsteps.ui.ReferenceViewModel
import kotlinx.coroutines.*
import soup.neumorphism.ShapeType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

//if (bluetoothUtils != null) {
//    // Only if the state is STATE_NONE, do we know that we haven't started already
//    if (bluetoothUtils?.getState() == StateEnum.STATE_NONE.ordinal) {
//        // Start the Bluetooth chat services
//        bluetoothUtils?.start()
//    }
//}

enum class MessageEnum {
    MESSAGE_STATE_CHANGED,
    MESSAGE_READ,
    MESSAGE_WRITE,
    MESSAGE_DEVICE_NAME,
    MESSAGE_TOAST
}


class MainActivity : AppCompatActivity() {
    companion object {
        val toast: String = "toast"
        var deviceName: String = "DeviceName"
        const val TAG = "MAIN_ACTIVITY"
        const val VIBRATION_TIME = 500L
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var context: Context
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var heatmapViewModel: HeatMapViewModel
    private lateinit var framesViewModel: FramesViewModel
    private lateinit var referenceViewModel: ReferenceViewModel
    var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var messageList: RecyclerView
    private lateinit var searchDevices: MenuItem
    private lateinit var adapterMessages: ChatAdapter
    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>
    private val runningSOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S
    private lateinit var connectedDevice: String
    private var bluetoothUtils: BluetoothUtils? = null
    var FLAG_ON = 0
    private lateinit var device: BluetoothDevice
    var c: Calendar = Calendar.getInstance()
    private var df: SimpleDateFormat? = null
    private var formattedDate = ""

    private lateinit var leftHeatMap: HeatMap
    private lateinit var rightHeatMap: HeatMap

    private lateinit var heatMapTestRight: HeatMap
    private lateinit var heatMapTestLeft: HeatMap

    private lateinit var feetContainer: ConstraintLayout


    private lateinit var heatMapUtil: HeatMapUtil

    private lateinit var flActionBtn: FloatingActionButton
    private lateinit var bluetoothBtn: ImageButton
    private lateinit var scientificBtn: ImageButton

    private lateinit var leftRight: LeftRight

    private lateinit var leftLineChart: LineChart
    private lateinit var rightLineChart: LineChart
    var leftf0LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var leftf1LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")

    var rightf0LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var rightf1LineDataSet: LineDataSet = LineDataSet(listOf(Entry(0f, 0f)), "")
    var iLineDataSet: java.util.ArrayList<ILineDataSet> = java.util.ArrayList()
    private lateinit var lineData: LineData

    private lateinit var chartStyle: LineChartStyle

    private lateinit var customBottomBar: CustomBottomNavBar

    private lateinit var animator: ValueAnimator
    private lateinit var animatorScaleDown: AnimatorSet
    private lateinit var animatorScaleUp: AnimatorSet
    private var isTouching = false
    private var longPressRunnable: Runnable? = null
    private var referenceSet: Boolean = false


    //    val bluetoothKit = BluetoothKit()
    private var stateEnum: StateEnum = StateEnum.STATE_NONE

    private lateinit var buffer: ByteArray
    private lateinit var receivedMsg: String
    private var end by Delegates.notNull<Int>()

    private var id: Int = 0
    private var f0: Int = 0
    private var f1: Int = 0
    private var f2: Int = 0
    private var f3: Int = 0
    private var f4: Int = 0
    private var f5: Int = 0
    private var a0: Int = 0
    private var a1: Int = 0
    private var a2: Int = 0
    private var g0: Int = 0
    private var g1: Int = 0
    private var g2: Int = 0
    private var counter: Int = 0

    private lateinit var pointX0: HeatMap.DataPoint
    private lateinit var pointX1: HeatMap.DataPoint
    private lateinit var pointX2: HeatMap.DataPoint
    private lateinit var pointX3: HeatMap.DataPoint
    private lateinit var pointX4: HeatMap.DataPoint
    private lateinit var pointX5: HeatMap.DataPoint
    private lateinit var pointX6: HeatMap.DataPoint
    private lateinit var pointX7: HeatMap.DataPoint
    private lateinit var pointX8: HeatMap.DataPoint

    private lateinit var dataPoints: List<HeatMap.DataPoint>

    private var foot: Insole = Insole()

    private var strHolder: String = ""

    private var startTime by Delegates.notNull<Long>()

    var onScreen = true
    private var pressedTime: Long = 0L

    private lateinit var messageCopy: Message

    private lateinit var vibrator: Vibrator


    @Synchronized
    fun setState2(state: StateEnum) {
        stateEnum = state
        handler.obtainMessage(MessageEnum.MESSAGE_STATE_CHANGED.ordinal, state.ordinal, -1)
            .sendToTarget()
    }

    @SuppressLint("MissingPermission")
    private var activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        Log.w(TAG, "IM BACKKKKKKKKKKKKKKKKK with result? ${result.resultCode == RESULT_OK}")
        if (result.resultCode == RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val data2 = intent.getStringExtra("deviceAddress")
            Log.i(TAG, "Result: ${data?.getStringExtra("deviceAddress")}")
            Log.i(TAG, "Result2: $data2")
            Toast.makeText(
                context,
                "Address: ${data?.getStringExtra("deviceAddress")}",
                Toast.LENGTH_SHORT
            ).show()
            connectedDevice = data?.getStringExtra("deviceAddress")!!
            deviceName = data?.getStringExtra("deviceName")!!
            device = data?.getParcelableExtra<BluetoothDevice>("device")!!
            bluetoothUtils?.connect(bluetoothAdapter!!.getRemoteDevice(connectedDevice))
        }
    }

    private var handler: Handler = Handler(Handler.Callback { message ->
//        startTime = System.currentTimeMillis()
//        Log.d("___CALL___", "Current time: $startTime milliseconds")

        when (message.what) {
            MessageEnum.MESSAGE_STATE_CHANGED.ordinal -> {
//                Log.i(TAG, "______________STATE CHANGED_____________")
                when (message.arg1) {
                    StateEnum.STATE_NONE.ordinal, StateEnum.STATE_LISTEN.ordinal -> {
                        setState("Not Connected")
                        foot = Insole()
                        heatMapUtil.leftFootPoints(foot)
                        heatMapUtil.rightFootPoints(foot)
                    }
                    StateEnum.STATE_CONNECTING.ordinal -> {
                        setState("Connecting...")
                        foot = Insole()
                        heatMapUtil.leftFootPoints(foot)
                        heatMapUtil.rightFootPoints(foot)
                    }
                    StateEnum.STATE_CONNECTED.ordinal -> {
                        setState("Connected to: $connectedDevice")
                    }
                }
            }

            MessageEnum.MESSAGE_READ.ordinal -> {
//                startTime = System.currentTimeMillis()
//                Log.d("___REENDERR___", "Current time: $startTime milliseconds")
                extractData(message)

            }
            MessageEnum.MESSAGE_WRITE.ordinal -> return@Callback true
            MessageEnum.MESSAGE_DEVICE_NAME.ordinal -> {
                connectedDevice = message.data.getString(deviceName).toString()
                Toast.makeText(context, connectedDevice, Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, message.data.getString(toast), Toast.LENGTH_SHORT)
                    .show()
                foot = Insole()
                heatMapUtil.leftFootPoints(foot)
                heatMapUtil.rightFootPoints(foot)
            }
        }
        return@Callback false
    })

    private fun extractData(message: Message) {
//        messageCopy = Message.obtain(message)
//        launch {
//            withContext(Dispatchers.Default) {

//        GlobalScope.launch(Dispatchers.Default) {


        //            Log.i("O7ADESKOM MN EXTRACTDATA", message.obj.toString())
        buffer = message.obj as ByteArray
//                val begin: Int = message.arg1
        end = message.arg2

        receivedMsg = String(buffer, 0, end)
//        strHolder = "0"
//                receivedMsg = receivedMsg.substring(begin, end)

//                Toast.makeText(context, receivedMsg, Toast.LENGTH_SHORT).show()
//                try{

//        Log.i(TAG, "_________THIS THREAD_________" + Thread.currentThread().name)
//                Log.i(TAG, "_________STRING HOLDER_________" + strHolder.toString())

        // sensors: s t u v w x y z
        // IMUs:
        //      accelerometer: a b c
        //      gyroscope:     d e f
        if (receivedMsg.isEmpty()) {
        } else {
            if (receivedMsg.last() != 'f') {
            } else {
                receivedMsg.forEachIndexed { _, value ->
                    if ((value != '#') and (!value.isWhitespace()) and (value != ' ') and (value.toString()
                            .isNotEmpty()) and (!value.toString().isNullOrBlank())
                    ) {
                        when (value) {
                            'i' -> {
                                id = strHolder.toInt()
                                strHolder = "0"
                            }
                            's' -> {
                                foot.sensor1 = strHolder.toDouble()
                                strHolder = "0"
                            }
                            't' -> {
                                foot.sensor2 = strHolder.toDouble()
                                strHolder = "0"
                            }
                            'u' -> {
                                foot.sensor3 = strHolder.toDouble()
                                strHolder = "0"
                            }
                            'v' -> {
                                foot.sensor4 = strHolder.toDouble()
                                strHolder = "0"
                            }
                            'w' -> {
                                foot.sensor5 = strHolder.toDouble()
                                strHolder = "0"
                            }
                            'x' -> {
                                foot.sensor6 = strHolder.toDouble()
                                strHolder = "0"
                            }
                            'a' -> {
                                a0 = strHolder.toInt()
                                strHolder = "0"
                            }
                            'b' -> {
                                a1 = strHolder.toInt()
                                strHolder = "0"
                            }
                            'c' -> {
                                a2 = strHolder.toInt()
                                strHolder = "0"
                            }
                            'd' -> {
                                g0 = strHolder.toInt()
                                strHolder = "0"
                            }
                            'e' -> {
                                g1 = strHolder.toInt()
                                strHolder = "0"
                            }
                            'f' -> {
                                // sometimes this stores "" into g2 if I set strHolder to default ""
                                // I still have no idea why it only happens to g2
                                g2 = strHolder.toInt()
                                strHolder = "0"
                            }
                            else -> {
                                if (value.isDigit()) {
                                    strHolder += value
                                }
                            }
                        }
                    }
                }
            }
        }
        /**
         * for(i in setOf<Int>(f0, f1, a0, a1, a2, g0, g1, g2)){
        // 3685 equals 90% of the peak pressure
        // uncomment the vibrateWhenHigh() function to vibrate when the pressure is high
        if(i > 3685){
        //                vibrateWhenHigh()
        }
        }*/
//            withContext(Dispatchers.Main) {
        when (id) {
            LeftRight.RIGHT.ordinal -> {
//                        f0 = (0..4095).random()
//                        f1 = (0..4095).random()
//                        f0 = (f0 + 1) % 60
//                        f1 = (f1 + 1) % 60
                if (onScreen) {
                    heatMapUtil.rightFootPoints(foot)
//                    rightHeatMap.forceRefresh()
                }
            }

            LeftRight.LEFT.ordinal -> {
//                        f0 = (0..4095).random()
//                        f1 = (0..4095).random()
//                        f0 = (f0 + 1) % 60
//                        f1 = (f1 + 1) % 60
                if (onScreen) {
                    heatMapUtil.leftFootPoints(foot)
//                    leftHeatMap.forceRefresh()
                }

            }
        }
//          }
//      }
//  }
//            feetContainer.invalidate()
    }

    private fun setState(subTitle: CharSequence) {
        supportActionBar?.subtitle = subTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        customBottomBar.inflateMenu(R.menu.bottom_menu)

//        supportFragmentManager.beginTransaction()
//            .replace(R.id.main_container, DashboardFragment())
//            .commit()

        val drawable = GradientDrawable().apply {
            colors = intArrayOf(
                R.color.lightBlue,
                R.color.white,
                R.color.white,
            )
            orientation = GradientDrawable.Orientation.TOP_BOTTOM
            gradientType = GradientDrawable.LINEAR_GRADIENT
            shape = GradientDrawable.RECTANGLE
        }

        customBottomBar = binding.customBottomNavBar
//        customBottomBar.inflateMenu(R.menu.bottom_menu)
//        customBottomBar.setShadow(R.color.black, R.dimen.shadow_normal, R.dimen.elevation, Gravity.TOP)
//        customBottomBar.setBackgroundDrawable(drawable)
        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        heatmapViewModel = ViewModelProvider(this)[HeatMapViewModel::class.java]
        framesViewModel = ViewModelProvider(this)[FramesViewModel::class.java]
        referenceViewModel = ViewModelProvider(this)[ReferenceViewModel::class.java]

        supportActionBar?.hide()

//        setContentView(R.layout.activity_main)
        context = this

        bluetoothUtils = BluetoothUtils(this, context, handler)
        rightHeatMap = binding.heatmapRight
        leftHeatMap = binding.heatmapLeft
        feetContainer = binding.feetContainer

//        rightMask = binding.rightMask
//        leftMask = binding.leftMask

        flActionBtn = binding.floatingActionButton
        bluetoothBtn = binding.bluetoothButton
        scientificBtn = binding.scientificBtn

        rightHeatMap.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        leftHeatMap.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        rightMask.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        leftMask.setLayerType(View.LAYER_TYPE_HARDWARE, null)

//            scheduleMemoryClearing()

        val lifecycleOwner = this

        scientificBtn.setOnClickListener {
            if (heatmapViewModel.isScientific()) {
                heatmapViewModel.turnOffScientific()
                binding.scientificBtn.setBackgroundColor(resources.getColor(R.color.kindaWhite))
                binding.scientificBtn.setShapeType(ShapeType.Companion.DEFAULT)
            } else {
                heatmapViewModel.setScientific()
                binding.scientificBtn.setBackgroundColor(resources.getColor(R.color.offWhite))
                binding.scientificBtn.setShapeType(ShapeType.BASIN)
            }
        }

        heatmapViewModel.heatMapMode.observe(lifecycleOwner) { mode ->
            when (mode) {
                HeatMapMode.SCIENTIFIC -> {
                    heatMapUtil.scientificTheme()
                    Toast.makeText(context, "Scientific Mode", Toast.LENGTH_SHORT).show()
                }
                HeatMapMode.USER_FRIENDLY -> {
                    heatMapUtil.userFriendlyTheme()
                    Toast.makeText(context, "Normal Mode", Toast.LENGTH_SHORT).show()
                }
            }
        }


        // get the VIBRATOR_SERVICE system service
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator


//        leftLineChart = binding.leftLineChart
//
////        leftf0LineDataSet.lineWidth = 3F
////        leftf0LineDataSet.formLineWidth = 3F
////        leftf1LineDataSet.lineWidth = 3F
////        leftf1LineDataSet.formLineWidth = 3F
//
//        rightLineChart = binding.rightLineChart
////        rightf0LineDataSet.lineWidth = 3F
////        rightf0LineDataSet.formLineWidth = 3F
////        rightf1LineDataSet.lineWidth = 3F
////        rightf1LineDataSet.formLineWidth = 3F
//
//        chartStyle = LineChartStyle(this)
//        chartStyle.styleChart(binding.leftLineChart)
//        chartStyle.styleChart(binding.rightLineChart)
//
////        chartStyle.drawLineGraph(binding.tempChart)
////        chartStyle.drawLineGraph(binding.ecgChart)
//
//        chartStyle.styleLineDataSet(leftf0LineDataSet)
//        chartStyle.styleLineDataSet(leftf1LineDataSet)
//        chartStyle.styleLineDataSet(rightf0LineDataSet)
//        chartStyle.styleLineDataSet(rightf1LineDataSet)

        heatMapUtil = HeatMapUtil(this, rightHeatMap, leftHeatMap)

        bluetoothBtn.setOnClickListener {
            // check if the device supports bluetooth
            if (!this.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
                Toast.makeText(context, "Bluetooth not supported", Toast.LENGTH_SHORT).show()
            } else {
                enableBluetooth()
                requestBluetoothPermissions()
            }
        }
//        testingStringSlicing()


        requestMultiplePermissionsLauncher()
//        chatRecyclerviewInit()

        initBluetooth()
        if (bluetoothAdapter?.isEnabled == true) {
//            requestBluetoothPermissions()
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
            val defaultAddress = resources.getString(R.string.default_address)
            val deviceAddress =
                sharedPref.getString(getString(R.string.device_address_key), defaultAddress)
            if (deviceAddress != "NONE") {
                bluetoothUtils?.connect(bluetoothAdapter!!.getRemoteDevice(deviceAddress))
            }
        }

//        viewModel.chatList.observe(this) {
//            it?.let {
//                adapterMessages.submitList(it)
//            }
//        }
//            viewModel.leftf0List.observe(this) {
//                it?.let {
//                    Log.i("leftF0List", "$f0")
////                updateLeftGraphs(it)
//                }
//            }
//            viewModel.rightf0List.observe(this) {
//                it?.let {
//                    Log.i("rightF0List", "$f0")
////                updateRightGraphs(it)
//                }
//            }

//            viewModel.selectedMessage.observe(this) {
//                it?.let {
//                }
//            }
//        , viewModel.rightf1List.value!!

//        heatMapTestRight = binding.heatmapTestRight
//        heatMapTestLeft = binding.heatmapTestLeft

//        heatMapTestRight.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        heatMapTestLeft.setLayerType(View.LAYER_TYPE_HARDWARE, null)

//        leftHeatMap.setMinimum(0.0)
//        leftHeatMap.setMaximum(60.0)
//
//        rightHeatMap.setMinimum(0.0)
//        rightHeatMap.setMaximum(60.0)

//        rightHeatMap.setRadius(750.0)
//        rightHeatMap.setRadius(750.0)

//        testingHeatmap()
        binding.addData.setOnClickListener {
            addDataToDatabase()
        }
//        flActionBtn.setOnClickListener {
////            addUserToDatabase()
////            // navigate to the reference activity
////            val intent = Intent(this, ReferenceActivity::class.java)
////            activityResultLauncher.launch(intent)
//
//        }


        // Create the pulsating animation
        animator = ValueAnimator.ofFloat(1.0f, 1.3f).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            duration = 2000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                val scale = animation.animatedValue as Float
                flActionBtn.scaleX = scale
                flActionBtn.scaleY = scale
            }
        }


        val scaleXAnimatorDown = ObjectAnimator.ofFloat(flActionBtn, "scaleX", 1.0f)
        val scaleYAnimatorDown = ObjectAnimator.ofFloat(flActionBtn, "scaleY", 1.0f)
        animatorScaleDown = AnimatorSet().apply {

            playTogether(scaleXAnimatorDown, scaleYAnimatorDown)
            duration = 200
            interpolator = AccelerateDecelerateInterpolator()
        }

        val scaleXAnimatorUp = ObjectAnimator.ofFloat(flActionBtn, "scaleX", 1.3f)
        val scaleYAnimatorUp = ObjectAnimator.ofFloat(flActionBtn, "scaleY", 1.3f)
        animatorScaleUp = AnimatorSet().apply {

            playTogether(scaleXAnimatorUp, scaleYAnimatorUp)
            duration = 200
            interpolator = AccelerateDecelerateInterpolator()
        }

        startPulsatingAnimation()

//        animator = ObjectAnimator.ofPropertyValuesHolder(
//            flActionBtn,
//            PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.3f, 1.0f),
//            PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.3f, 1.0f)
//        ).apply {
//            repeatCount = ValueAnimator.INFINITE // Repeat the animation indefinitely
//            duration = 2000 // Animation duration in milliseconds
//            interpolator = AccelerateDecelerateInterpolator() // Smoothly accelerate and decelerate the animation
//            repeatMode = ValueAnimator.RESTART // Restart the animation when it reaches the end
//        }
//        animator.start()


        flActionBtn.setOnLongClickListener {
//            animatorScaleUp.start()
            // Action to perform after the long press duration (5 seconds or more)
            // Add your code here
            f0 = (f0 + 10) % 60
            f1 = (f0 + 10) % 60
            f2 = (f1 + 10) % 60
            f3 = (f2 + 10) % 60
            f4 = (f3 + 10) % 60
            f5 = (f4 + 10) % 60
            foot = Insole(
                f0.toDouble(),
                f1.toDouble(),
                f2.toDouble(),
                f3.toDouble(),
                f4.toDouble(),
                f5.toDouble()
            )
            Log.i("fooooooot", "$foot")

            heatMapUtil.leftFootPoints(foot)
            heatMapUtil.rightFootPoints(foot)

            true
        }

        customBottomBar.setOnItemSelectedListener { it ->
            when (it.itemId) {
//                R.id.action_home -> {
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                }
                R.id.action_profile -> {
                    val intent = Intent(this, ReferenceActivity::class.java)
                    startActivity(intent)
                }
//                R.id.action_charts -> {
////                    val intent = Intent(this, ChartsActivity::class.java)
////                    startActivity(intent)
//            }
                else -> false
            }
            true
        }


//        flActionBtn.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                when (event?.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        animatorScaleUp.start()
//                    }
//                }
//                return v?.onTouchEvent(event) ?: true
//            }
//        }
//        )

//        flActionBtn.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
//            if (hasFocus) {
//                animatorScaleUp.start()
//            } else {
//                animatorScaleDown.start()
//            }
//        }
        flActionBtn.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    isTouching = true
                    animator.cancel()
                    animatorScaleUp.start()
                    startLongPressRunnable()
                }
                MotionEvent.ACTION_UP -> {
                    isTouching = false
                    animatorScaleDown.start()
                    stopLongPressRunnable()
                    if(!referenceSet){
                        Toast.makeText(this, "No Reference has added", Toast.LENGTH_SHORT).show()
                        startPulsatingAnimation()
                    }
                }
            }
            false
        }

    }

    private fun startPulsatingAnimation() {
        if (!animator.isRunning) {
            animator.start()
        }
    }

    private fun cancelPulsatingAnimation() {
        if (animator.isRunning) {
            animator.cancel()
            animatorScaleDown.start()
        }
    }

    private fun startLongPressRunnable() {
        stopLongPressRunnable() // Stop the previous runnable if any
        longPressRunnable = Runnable {
            if (isTouching) {
                referenceSet = true
                cancelPulsatingAnimation()
                addUserToDatabase()
                Toast.makeText(this, "New Reference has been added", Toast.LENGTH_LONG).show()
            }
        }
        handler.postDelayed(longPressRunnable!!, 3000) // 3 seconds
    }

    private fun stopLongPressRunnable() {
        longPressRunnable?.let {
            handler.removeCallbacks(it)
            longPressRunnable = null
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun testingHeatmap() {
        var index = 0
        var ten = 0
        // create a background thread using async.task execute
        AsyncTask.execute {
            while (true) {
                // update the heatmap in the background thread
                index += 1
                if (index > 60) {
                    index = 0
                }
//                ten += 1
//                if (ten > 10) {
//                    ten = 0
//                }

                leftHeatMap.clearData()
                rightHeatMap.clearData()

                pointX0 = HeatMap.DataPoint(0.5F, 0.1F, index.toDouble())
                pointX1 = HeatMap.DataPoint(0.4F, 0.2F, index.toDouble())
                pointX2 = HeatMap.DataPoint(0.3F, 0.3F, index.toDouble())
                pointX3 = HeatMap.DataPoint(0.4F, 0.4F, index.toDouble())
                pointX4 = HeatMap.DataPoint(0.5F, 0.5F, index.toDouble())
                pointX5 = HeatMap.DataPoint(0.4F, 0.6F, index.toDouble())
                pointX6 = HeatMap.DataPoint(0.3F, 0.7F, index.toDouble())
                pointX7 = HeatMap.DataPoint(0.4F, 0.8F, index.toDouble())
                pointX8 = HeatMap.DataPoint(0.5F, 0.9F, index.toDouble())
                // create a list of all dataPoints
                dataPoints = listOf(
                    pointX0,
                    pointX1,
                    pointX2,
                    pointX3,
                    pointX4,
                    pointX5,
                    pointX6,
                    pointX7,
                    pointX8
                )

                for (point in dataPoints) {
                    leftHeatMap.addData(point)
                    rightHeatMap.addData(point)
                }

                leftHeatMap.postInvalidate()
                rightHeatMap.postInvalidate()

                // update the heatmap in the background thread
                runOnUiThread {
                    try {
                        leftHeatMap.forceRefreshOnWorkerThread()
                        rightHeatMap.forceRefreshOnWorkerThread()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // clear the arrays
                        leftHeatMap.clearData()
                        rightHeatMap.clearData()
                    }
                }

                try {
                    Thread.sleep(17)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }


/*        GlobalScope.launch(Dispatchers.Default) {
            while (true) {
//                if (index >= dataPoints.size){
//                    index = 0
//                }
                // update the heatmap in the background thread
                counter += 1
                if (counter > 60) {
                    counter = 0
                }

                pointX0 = HeatMap.DataPoint(0.5F, 0.1F, counter.toDouble())
                pointX1 = HeatMap.DataPoint(0.4F, 0.2F, counter.toDouble())
                pointX2 = HeatMap.DataPoint(0.3F, 0.3F, counter.toDouble())
                pointX3 = HeatMap.DataPoint(0.4F, 0.4F, counter.toDouble())
                pointX4 = HeatMap.DataPoint(0.5F, 0.5F, counter.toDouble())
                pointX5 = HeatMap.DataPoint(0.4F, 0.6F, counter.toDouble())
                pointX6 = HeatMap.DataPoint(0.3F, 0.7F, counter.toDouble())
                pointX7 = HeatMap.DataPoint(0.4F, 0.8F, counter.toDouble())
                pointX8 = HeatMap.DataPoint(0.5F, 0.9F, counter.toDouble())

                pointX10 = HeatMap.DataPoint(0.7F, 0.1F, counter.toDouble())
                pointX11 = HeatMap.DataPoint(0.7F, 0.2F, counter.toDouble())
                pointX12 = HeatMap.DataPoint(0.7F, 0.3F, counter.toDouble())
                pointX13 = HeatMap.DataPoint(0.7F, 0.4F, counter.toDouble())
                pointX14 = HeatMap.DataPoint(0.7F, 0.5F, counter.toDouble())
                pointX15 = HeatMap.DataPoint(0.7F, 0.6F, counter.toDouble())
                pointX16 = HeatMap.DataPoint(0.7F, 0.7F, counter.toDouble())
                pointX17 = HeatMap.DataPoint(0.7F, 0.8F, counter.toDouble())
                pointX18 = HeatMap.DataPoint(0.7F, 0.9F, counter.toDouble())

                val dataPoints = listOf<HeatMap.DataPoint>(
                    pointX0,
                    pointX1,
                    pointX2,
                    pointX3,
                    pointX4,
                    pointX5,
                    pointX6,
                    pointX7,
                    pointX8,

//                    pointX10,
//                    pointX11,
//                    pointX12,
//                    pointX13,
//                    pointX14,
//                    pointX15,
//                    pointX16,
//                    pointX17,
//                    pointX18
                )
//                rightHeatMap.clearData()
                leftHeatMap.clearData()

                for (element in dataPoints){
//                    rightHeatMap.apply {
//                        addData(element)
////                        forceRefresh()
////                        postInvalidate()
//                    }

                    leftHeatMap.apply {
                        addData(element)
////                        postInvalidate()
                    }
                }
//                rightHeatMap.postInvalidate()
                leftHeatMap.postInvalidate()

//                heatMapTestRight.addData(dataPoints[index])
//                heatMapTestRight.forceRefreshOnWorkerThread()
//                index += 1

                ten += 1
                withContext(Dispatchers.Main) {
//                    heatMapTestRight.forceRefreshOnWorkerThread()
//                    heatMapTestRight.apply{
//                        // stop the heatmap from clearing the previous data
//                        heatMapTestRight.forceRefreshOnWorkerThread()
//                        postInvalidate()
//                    }
                    if (ten == 10){
                        ten = 0
                        leftHeatMap.forceRefreshOnWorkerThread()

                        // refresh the other foot after a delay
//                        delay(2)
//                        rightHeatMap.forceRefreshOnWorkerThread()

                    }
//                    Log.i("heatmap", "poinnt: $counter")
//                    heatMapTestLeft.apply{
//                        addData(pointX0)
//                        addData(pointX1)
//                        addData(pointX2)
//                        addData(pointX3)
//                        addData(pointX4)
//                        addData(pointX5)
//                        addData(pointX6)
//                        addData(pointX7)
//                        addData(pointX8)
//                        forceRefreshOnWorkerThread()
//                        invalidate()
//                    }
                }
                delay(3) // wait for 20 milliseconds before the next update
            }
        }*/
    }

    fun addDataToDatabase() {
        // add new LeftFootFrame data to room database
        val leftFootFrame = LeftFootFrame(
            sensor1 = 1.0,
            sensor2 = 2.0,
            sensor3 = 3.0,
            sensor4 = 4.0,
            sensor5 = 5.0,
            sensor6 = 6.0,
            acc0 = 0.0,
            acc1 = 11.0,
            acc2 = 22.0,
            gyro0 = 0.0,
            gyro1 = 111.0,
            gyro2 = 222.0
        )
        framesViewModel.insertLeftFootFrame(leftFootFrame)
    }

    fun addUserToDatabase() {
        val user = User(
            title = "Zeft",
            timestamp = System.currentTimeMillis(),
            selected = true
        )
        referenceViewModel.insertUser(user)
    }

    override fun onStart() {
        heatMapUtil.leftFootPoints(Insole())
        heatMapUtil.rightFootPoints(Insole())
        customBottomBar.selectedItemId = R.id.action_home
        super.onStart()
    }

    override fun onBackPressed() {
        if (pressedTime + 1500 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        pressedTime = System.currentTimeMillis()
    }

    private fun vibrateWhenHigh() {
        if (vibrator != null && vibrator.hasVibrator()) {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Vibrate for 500 milliseconds
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        VIBRATION_TIME,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                //deprecated in API 26
                vibrator.vibrate(VIBRATION_TIME)
            }
        } else {
            Toast.makeText(this, "Device does not support vibration", Toast.LENGTH_SHORT).show();
        }
    }

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

        rightLineChart.clear()
        rightLineChart.data = lineData
        rightLineChart.invalidate()
    }

//    fun testingStringSlicing() {
//        val msg: String = "1i231f1233g1543h1314j231k1233p2543m1314n#"
//        idIndex = msg.indexOf('i', 0)
//
//        try {
//            for (c in msg.indices) {
//                if (msg[c] == 'i') {
//                    id = Integer.parseInt(msg.slice(0 until c))
//                    idIndex = c
//                }
//                if (msg[c] == 'f') {
//                    f = Integer.parseInt(msg.slice(idIndex + 1 until c))
//                    fIndex = c
//                }
//                if (msg[c] == 'g') {
//                    g = Integer.parseInt(msg.slice(fIndex + 1 until c))
//                    gIndex = c
//                }
//                if (msg[c] == 'h') {
//                    h = Integer.parseInt(msg.slice(gIndex + 1 until c))
//                    hIndex = c
//                }
//                if (msg[c] == 'j') {
//                    j = Integer.parseInt(msg.slice(hIndex + 1 until c))
//                }
//            }
//
//            Toast.makeText(context, "i: $id f: $f g: $g h: $h j: $j", Toast.LENGTH_SHORT).show()
//
//        } catch (e: IOException) {
//            Log.e("GetIndices", "${e.message}")
//            Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG)
//        }
//    }

    private fun clearMemory() {
        System.gc()
    }

    private fun scheduleMemoryClearing() {
        val handler = Handler()
        val clearMemoryRunnable = object : Runnable {
            override fun run() {
                clearMemory()
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(clearMemoryRunnable, 3000)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        hideKeyboard(this, window.decorView);
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
        return super.onTouchEvent(event)
    }

    private fun hideKeyboard(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        searchDevices = menu.findItem(R.id.menu_search_devices)
//        var switchOnBT = menu.findItem(R.id.menu_bluetooth_on)
//        var switchOffBT = menu.findItem(R.id.menu_bluetooth_off)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        onScreen = true
        super.onResume()
    }

    override fun onPause() {
        onScreen = false
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search_devices -> {
//                Toast.makeText(context, "Searching", Toast.LENGTH_SHORT).show()
                enableBluetooth()
                requestBluetoothPermissions()
            }
        }

        if (FLAG_ON == 1) {
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initBluetooth() {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
    }

    @SuppressLint("MissingPermission")
    fun enableBluetooth() {
        initBluetooth()

        // tells if the device supports bluetooth or not
        // actually useless if I set bluetooth to be required in manifest
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "The device does not support bluetooth", Toast.LENGTH_LONG)
                .show()
        } else {
            if (isPermissionGranted()) {

                if (bluetoothAdapter!!.isEnabled) {
                    Toast.makeText(this, "Bluetooth already enabled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Turning on Bluetooth", Toast.LENGTH_SHORT).show()
                    bluetoothAdapter?.enable()
                }
            } else {
                requestBluetoothPermissions()
            }
        }
    }

    fun requestBluetoothPermissions() {
        if (isPermissionGranted()) {
            val intent = Intent(context, DeviceListActivity::class.java)
//            startActivityForResult(intent, SELECT_DEVICE)
            activityResultLauncher.launch(intent)
        } else {
            var permissionsArray =
                arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN)

            if (runningSOrLater) {
                permissionsArray += arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            }
            Log.d(TAG, "Request legacy bluetooth permissions")
            requestMultiplePermissions.launch(permissionsArray)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /**     Replaced with registerForActivityResult() and ActivityResultLauncher
         *                 since startActivityForResult is deprecated
         *
         *                  ctr + f ("var activityResultLauncher:")
         *
         * */
        if (resultCode == SELECT_DEVICE && resultCode == RESULT_OK) {
            Log.i(TAG, "Im in onActivityResult OOOOOOOOOOOOO")

            var address = data?.getStringExtra("deviceAddress")
//            Toast.makeText(context, "Address: $address", Toast.LENGTH_LONG).show()
            Log.i(TAG, "Address: $address")
        }
    }

    private fun requestMultiplePermissionsLauncher() {
        requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val notGranted = permissions.values.contains(false)
                if (notGranted) {
                    Log.i("DEBUG", "permission not granted")
                    Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()

                    AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setMessage("Bluetooth permission is required. \n Please grant")
                        .setPositiveButton(R.string.grant) { _, _ ->
                            requestBluetoothPermissions()
                        }
                        .setNegativeButton(R.string.deny) { _, _ ->
                            finish()
                        }
                        .show()

                    /**
                     * Show a Snackbar instead of an AlertDialog if you wish,
                     * but here, we have a chat app with an input field at
                     * the bottom.. too risky to have a snackbar there
                     * (might lead to misclicks)
                     * */

//                    Snackbar.make(
//                        window.decorView.findViewById(android.R.id.content),
//                        R.string.permissions_needed_snackbar_msg,
//                        Snackbar.LENGTH_INDEFINITE
//                    )
//                        .setAction(R.string.settings) {
//                            startActivity(Intent().apply {
//                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                                data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
//                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                            })
//                        }.show()

                } else {
                    Log.i("DEBUG", "permission granted")
                    val selectDeviceIntent = Intent(context, DeviceListActivity::class.java)
//                    startActivityForResult(selectDeviceIntent, SELECT_DEVICE)
                    activityResultLauncher.launch(selectDeviceIntent)
                }
            }
    }

    private fun isPermissionGranted(): Boolean {
        val bluetoothApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                )
        val bluetoothAdminApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_ADMIN
                        )
                )
        val bluetoothScanApproved = (
                if (runningSOrLater) {
                    PackageManager.PERMISSION_GRANTED ==
                            ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.BLUETOOTH_SCAN
                            )
                } else {
                    true
                }
                )
        val bluetoothConnectApproved =
            if (runningSOrLater) {
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            this, Manifest.permission.BLUETOOTH_CONNECT
                        )
            } else {
                true
            }
        return bluetoothApproved && bluetoothAdminApproved && bluetoothScanApproved && bluetoothConnectApproved
    }

    @SuppressLint("MissingPermission")
//    private fun chatRecyclerviewInit() {
//        messageList = binding.messagesList
////        adapterPairedDevices = DeviceAdapter(viewModel.pairedDevices)
//        adapterMessages = ChatAdapter(this, ChatAdapter.ChatBubbleListener {
//            viewModel.messageSelected(it)
//        })
//        messageList.adapter = adapterMessages
//        messageList.layoutManager =
//            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
//
//        binding.btnSendMessage.setOnClickListener {
//            if (binding.edEnterMessage.text != null) {
//                var message: String = binding.edEnterMessage.text.toString()
//                c = Calendar.getInstance()
//
//                /*
//                 * https://developer.android.com/reference/kotlin/java/text/SimpleDateFormat
//                 * /
//                df = SimpleDateFormat("E HH:mm a", Locale.ENGLISH)
//                formattedDate = df!!.format(c.time)
//                var bubble = ChatBubble(
//                    id = viewModel.id.value!!,
//                    chatMessage = message,
//                    messageDate = formattedDate,
//                    sender = deviceName
//                )
//
//                viewModel.updateChatList(bubble)
//                binding.edEnterMessage.text.clear()
//                viewModel.chatList.value?.size?.let {
//                    binding.messagesList.scrollToPosition(
//                        it.minus(
//                            1
//                        )
//                    )
//                }
//
//                bluetoothUtils?.write(message.toByteArray())
//                bluetoothUtils?.write("\n".toByteArray())
//
////                    bluetoothKit.write(message.toByteArray())
////                    bluetoothKit.write("\n".toByteArray())
//
//
//                hideKeyboard(this, binding.root)
//            }
//        }
//    }


    override fun onDestroy() {
        super.onDestroy()
        animator.cancel()
        if (bluetoothUtils != null) {
            bluetoothUtils?.stop()
        }
    }

}

private const val REQUEST_ANDROID_S_PERMISSION_RESULT_CODE = 33
private const val REQUEST_LEGACY_PERMISSIONS_REQUEST_CODE = 31
private const val SELECT_DEVICE = 20
