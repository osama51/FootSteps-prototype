package com.toddler.bluecomm

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.hss.heatmaplib.HeatMap
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.toddler.bluecomm.bluetooth.DeviceListActivity
import com.toddler.bluecomm.chat.ChatAdapter
import com.toddler.bluecomm.chat.ChatBubble
import com.toddler.bluecomm.chat.ChatViewModel
import com.toddler.bluecomm.databinding.ActivityMainBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


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
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var context: Context
    private lateinit var viewModel: ChatViewModel
    var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var messageList: RecyclerView
    private lateinit var searchDevices: MenuItem
    private lateinit var adapterMessages: ChatAdapter
    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>
    private val runningSOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S
    private lateinit var connectedDevice: String
    private var chatUtils: ChatUtils? = null
    var FLAG_ON = 0
    private lateinit var device: BluetoothDevice
    var c: Calendar = Calendar.getInstance()
    private var df: SimpleDateFormat? = null
    private var formattedDate = ""

    private lateinit var leftHeatMap: HeatMap
    private lateinit var rightHeatMap: HeatMap
    private lateinit var heatMapUtil: HeatMapUtil

    private lateinit var btActionBar: FloatingActionButton

    private lateinit var leftRight: LeftRight

    //    val bluetoothKit = BluetoothKit()
    private var stateEnum: StateEnum = StateEnum.STATE_NONE

    private var idIndex by Delegates.notNull<Int>()
    private var f0Index by Delegates.notNull<Int>()
    private var f1Index by Delegates.notNull<Int>()
    private var a0Index by Delegates.notNull<Int>()
    private var a1Index by Delegates.notNull<Int>()
    private var a2Index by Delegates.notNull<Int>()
    private var g0Index by Delegates.notNull<Int>()
    private var g1Index by Delegates.notNull<Int>()
    private var g2Index by Delegates.notNull<Int>()


    private var id: Int = 0
    private var f0: Int = 0
    private var f1: Int = 0
    private var a0: Int = 0
    private var a1: Int = 0
    private var a2: Int = 0
    private var g0: Int = 0
    private var g1: Int = 0
    private var g2: Int = 0




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
            chatUtils?.connectAndStartThread(bluetoothAdapter!!.getRemoteDevice(connectedDevice))

        }
    }

    private var handler: Handler = Handler(Handler.Callback { message ->

        when (message.what) {
            MessageEnum.MESSAGE_STATE_CHANGED.ordinal -> {
                when (message.arg1) {
                    StateEnum.STATE_NONE.ordinal, StateEnum.STATE_LISTEN.ordinal -> {
                        setState("Not Connected")
                        heatMapUtil.leftFootPoints(0.0,0.0)
                        heatMapUtil.rightFootPoints(0.0,0.0)
                    }
                    StateEnum.STATE_CONNECTING.ordinal -> {
                        setState("Connecting...")
                        heatMapUtil.leftFootPoints(0.0,0.0)
                        heatMapUtil.rightFootPoints(0.0,0.0)
                    }
                    StateEnum.STATE_CONNECTED.ordinal -> {
                        setState("Connected to: $connectedDevice")
                    }
                }
            }
            MessageEnum.MESSAGE_READ.ordinal -> {
                var buffer = message.obj as ByteArray
//                val begin: Int = message.arg1
                val end: Int = message.arg2

                var receivedMsg = String(buffer, 0, end)
//                receivedMsg = receivedMsg.substring(begin, end)

//                Toast.makeText(context, receivedMsg, Toast.LENGTH_SHORT).show()
//                try{
                idIndex = receivedMsg.indexOf('i', 0)
                f0Index = receivedMsg.indexOf("f0", 0)
                f1Index = receivedMsg.indexOf("f1", 0)
                a0Index = receivedMsg.indexOf("a0", 0)
                a1Index = receivedMsg.indexOf("a1", 0)
                a2Index = receivedMsg.indexOf("a2", 0)
                g0Index = receivedMsg.indexOf("g0", 0)
                g1Index = receivedMsg.indexOf("g1", 0)
                g2Index = receivedMsg.indexOf("g2", 0)

                when (-1) {
                    idIndex, f0Index, f1Index, a0Index, a1Index, a2Index, g0Index, g1Index, g2Index -> {

                    }
                    else -> {
//                        for (c in receivedMsg.indices) {

                            id = receivedMsg.slice(idIndex - 1 until idIndex).toInt()
                            f0 = receivedMsg.slice(idIndex + 2 until f0Index).toInt()
                            f1 = receivedMsg.slice(f0Index + 2 until f1Index).toInt()
                            a0 = receivedMsg.slice(f1Index + 2 until a0Index).toInt()
                            a1 = receivedMsg.slice(a0Index + 2 until a1Index).toInt()
                            a2 = receivedMsg.slice(a1Index + 2 until a2Index).toInt()
                            g0 = receivedMsg.slice(a2Index + 2 until g0Index).toInt()
                            g1 = receivedMsg.slice(g0Index + 2 until g1Index).toInt()
                            g2 = receivedMsg.slice(g1Index + 2 until g2Index).toInt()

//                            if (receivedMsg[c] == 'i') {
//                                id = Integer.parseInt(receivedMsg.slice(c - 1 until c))
//                                idIndex = c
//                            }
//                            if (receivedMsg[c] == 'f') {
//                                f = Integer.parseInt(receivedMsg.slice(idIndex + 2 until c)) ?: 0
//                                fIndex = c
//                            }
//                            if (receivedMsg[c] == 'g') {
//                                g = Integer.parseInt(receivedMsg.slice(fIndex + 2 until c))
//                                gIndex = c
//                            }
//                            if (receivedMsg[c] == 'h') {
//                                h = Integer.parseInt(receivedMsg.slice(gIndex + 2 until c))
//                                hIndex = c
//                            }
//                            if (receivedMsg[c] == 'j') {
//                                j = Integer.parseInt(receivedMsg.slice(hIndex + 2 until c))
//                            }
//                        }
                    }
                }

                when(id){
                    LeftRight.RIGHT.ordinal -> {
                        f0 = (0..4095).random()
                        f1 = (0..4095).random()
                        heatMapUtil.leftFootPoints(f0.toDouble(), f1.toDouble())
                    }
                    LeftRight.LEFT.ordinal -> {
                        f0 = (0..4095).random()
                        f1 = (0..4095).random()
                        heatMapUtil.rightFootPoints(f0.toDouble(), f1.toDouble())
                    }
                }

                c = Calendar.getInstance()
                df = SimpleDateFormat("E HH:mm a", Locale.ENGLISH)
                formattedDate = df!!.format(c.time)
                var bubble = ChatBubble(
                    id = viewModel.id.value!!,
                    chatMessage = "i: $id f0: $f0 f1: $f1 a0: $a0 a1: $a1 a2: $a2 g0: $g0 g1: $g1 g2: $g2",
                    messageDate = formattedDate,
                    sender = deviceName
                )
                viewModel.updateChatList(bubble)
                viewModel.chatList.value?.size?.let {
                    binding.messagesList.scrollToPosition(
                        it.minus(
                            1
                        )
                    )
                }
            }
            MessageEnum.MESSAGE_WRITE.ordinal -> return@Callback true
            MessageEnum.MESSAGE_DEVICE_NAME.ordinal -> {
                connectedDevice = message.data.getString(deviceName).toString()
                Toast.makeText(context, connectedDevice, Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, message.data.getString(toast), Toast.LENGTH_SHORT).show()
                heatMapUtil.leftFootPoints(0.0,0.0)
                heatMapUtil.rightFootPoints(0.0,0.0)
            }
        }
        return@Callback false
    })

    private fun setState(subTitle: CharSequence) {
        supportActionBar?.subtitle = subTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        supportActionBar?.hide()

//        setContentView(R.layout.activity_main)
        context = this

        chatUtils = ChatUtils(context, handler)
        rightHeatMap = binding.heatmapRight
        leftHeatMap = binding.heatmapLeft
        btActionBar = binding.floatingActionButton

        heatMapUtil = HeatMapUtil(rightHeatMap, leftHeatMap)


        btActionBar.setOnClickListener {
            initBluetooth()
            requestBluetoothPermissions()
        }
//        testingStringSlicing()

        requestMultiplePermissionsLauncher()
        chatRecyclerviewInit()

        viewModel.chatList.observe(this) {
            it?.let {
                adapterMessages.submitList(it)
            }
        }

        viewModel.selectedMessage.observe(this) {
            it?.let {
            }
        }

        binding.messagesList.apply {
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true
                reverseLayout = false
            }
        }
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
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search_devices -> {
//                Toast.makeText(context, "Searching", Toast.LENGTH_SHORT).show()
                initBluetooth()
                requestBluetoothPermissions()
            }
        }

        if (FLAG_ON == 1) {}
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("MissingPermission")
    fun initBluetooth() {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter


        // tells if the device supports bluetooth or not
        // actually useless if I set bluetooth to be required in manifest
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "The device does not support bluetooth", Toast.LENGTH_LONG).show()
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

    private fun requestBluetoothPermissions() {
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
    private fun chatRecyclerviewInit() {
        messageList = binding.messagesList
//        adapterPairedDevices = DeviceAdapter(viewModel.pairedDevices)
        adapterMessages = ChatAdapter(this, ChatAdapter.ChatBubbleListener {
            viewModel.messageSelected(it)
        })
        messageList.adapter = adapterMessages
        messageList.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.btnSendMessage.setOnClickListener {
            if (binding.edEnterMessage.text != null) {
                var message: String = binding.edEnterMessage.text.toString()
                c = Calendar.getInstance()

                /**
                 * https://developer.android.com/reference/kotlin/java/text/SimpleDateFormat
                 * */
                df = SimpleDateFormat("E HH:mm a", Locale.ENGLISH)
                formattedDate = df!!.format(c.time)
                var bubble = ChatBubble(
                    id = viewModel.id.value!!,
                    chatMessage = message,
                    messageDate = formattedDate,
                    sender = deviceName
                )

                viewModel.updateChatList(bubble)
                binding.edEnterMessage.text.clear()
                viewModel.chatList.value?.size?.let {
                    binding.messagesList.scrollToPosition(
                        it.minus(
                            1
                        )
                    )
                }

                chatUtils?.write(message.toByteArray())
                chatUtils?.write("\n".toByteArray())

//                    bluetoothKit.write(message.toByteArray())
//                    bluetoothKit.write("\n".toByteArray())


                hideKeyboard(this, binding.root)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (chatUtils != null) {
            chatUtils?.stop()
        }
    }

}

private const val REQUEST_ANDROID_S_PERMISSION_RESULT_CODE = 33
private const val REQUEST_LEGACY_PERMISSIONS_REQUEST_CODE = 31
private const val SELECT_DEVICE = 20
