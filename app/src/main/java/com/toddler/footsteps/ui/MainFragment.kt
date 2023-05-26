package com.toddler.footsteps.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Message
import android.os.Vibrator
import android.view.MenuItem
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ca.hss.heatmaplib.HeatMap
import com.christophesmet.android.views.maskableframelayout.MaskableFrameLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.toddler.footsteps.*
import com.toddler.footsteps.chat.ChatAdapter
import com.toddler.footsteps.chat.ChatViewModel
import com.toddler.footsteps.databinding.FragmentMainBinding
import com.toddler.footsteps.heatmap.HeatMapViewModel
import com.toddler.footsteps.heatmap.Insole
import com.toddler.footsteps.navbar.CustomBottomNavBar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class MainFragment: Fragment() {

    private lateinit var binding: FragmentMainBinding
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


    private lateinit var rightMask: MaskableFrameLayout
    private lateinit var leftMask: MaskableFrameLayout

    private lateinit var heatMapUtil: HeatMapUtil

    private lateinit var flActionBtn: FloatingActionButton
    private lateinit var bluetoothBtn: ImageButton
    private lateinit var analyticBtn: ImageButton

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

    //    val bluetoothKit = BluetoothKit()
    private var stateEnum: StateEnum = StateEnum.STATE_NONE

    private lateinit var buffer: ByteArray
    private lateinit var receivedMsg: String
    private var end by Delegates.notNull<Int>()

    private lateinit var dataPoints: List<HeatMap.DataPoint>

    private var foot: Insole = Insole()

    private var strHolder: String = ""

    private var startTime by Delegates.notNull<Long>()

    var onScreen = true
    private var pressedTime: Long = 0L

    private lateinit var messageCopy: Message

    private lateinit var vibrator: Vibrator

/*    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
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


//        setContentView(R.layout.activity_main)

        bluetoothUtils = BluetoothUtils(this, context, MainActivity.handler)
        rightHeatMap = binding.heatmapRight
        leftHeatMap = binding.heatmapLeft
        feetContainer = binding.feetContainer

//        rightMask = binding.rightMask
//        leftMask = binding.leftMask

        flActionBtn = binding.floatingActionButton
        bluetoothBtn = binding.bluetoothButton
        analyticBtn = binding.analyticBtn

        rightHeatMap.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        leftHeatMap.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        rightMask.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        leftMask.setLayerType(View.LAYER_TYPE_HARDWARE, null)

//            scheduleMemoryClearing()

        val lifecycleOwner = this

        analyticBtn.setOnClickListener {
            if (heatmapViewModel.isAnalytic()) {
                heatmapViewModel.turnOffAnalytic()
                binding.analyticBtn.setBackgroundColor(resources.getColor(R.color.kindaWhite))
                binding.analyticBtn.setShapeType(ShapeType.Companion.DEFAULT)
            } else {
                heatmapViewModel.setAnalytic()
                binding.analyticBtn.setBackgroundColor(resources.getColor(R.color.offWhite))
                binding.analyticBtn.setShapeType(ShapeType.BASIN)
            }
        }

        heatmapViewModel.heatMapMode.observe(viewLifecycleOwner) { mode ->
            when (mode) {
                HeatMapMode.ANALYTIC -> {
                    heatMapUtil.analyticTheme()
                    Toast.makeText(context, "Analytic Mode", Toast.LENGTH_SHORT).show()
                }
                HeatMapMode.USER_FRIENDLY -> {
                    heatMapUtil.userFriendlyTheme()
                    Toast.makeText(context, "Normal Mode", Toast.LENGTH_SHORT).show()
                }
            }
        }


        // get the VIBRATOR_SERVICE system service
        vibrator = getSystemService(context, AppCompatActivity.VIBRATOR_SERVICE::class.java) as Vibrator


        heatMapUtil = HeatMapUtil(rightHeatMap, leftHeatMap)

        bluetoothBtn.setOnClickListener {
            // check if the device supports bluetooth
            if (!this.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
                Toast.makeText(context, "Bluetooth not supported", Toast.LENGTH_SHORT).show()
            } else {
                enableBluetooth()
                requestBluetoothPermissions()
            }
        }


//        heatMapTestRight = binding.heatmapTestRight
//        heatMapTestLeft = binding.heatmapTestLeft

//        heatMapTestRight.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        heatMapTestLeft.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        leftHeatMap.setMinimum(0.0)
        leftHeatMap.setMaximum(60.0)

        rightHeatMap.setMinimum(0.0)
        rightHeatMap.setMaximum(60.0)


        testingHeatmap()
        binding.addData.setOnClickListener {
            addDataToDatabase()
        }
        flActionBtn.setOnClickListener {
            addUserToDatabase()
            // navigate to the reference activity
            val intent = Intent(this, ReferenceActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        return binding.root
    }*/
}