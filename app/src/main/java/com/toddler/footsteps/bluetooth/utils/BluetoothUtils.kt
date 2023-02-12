package com.toddler.footsteps

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import kotlin.properties.Delegates


enum class StateEnum {
    STATE_NONE,
    STATE_LISTEN,
    STATE_CONNECTING,
    STATE_CONNECTED
}

class BluetoothUtils(activity: Activity, context: Context, handler: Handler) {

    private var activity: Activity = activity
    private var context: Context = context
    private var handler: Handler = handler
    private var connectThread: ConnectThread? = null
    private var acceptThread: AcceptThread? = null
    private var connectedThread: ConnectedThread? = null
    private var bluetoothAdapter: BluetoothAdapter
    private var appUuid: UUID = UUID.fromString(UUID_STRING)
    private var appName: String = "FootStepsApp"
    private var stateEnum: StateEnum = StateEnum.STATE_NONE
    private var startTime by Delegates.notNull<Long>()


    init {
        stateEnum = StateEnum.STATE_NONE
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    fun getState(): StateEnum {
        return stateEnum
    }

    @Synchronized
    fun setState(state: StateEnum) {
        this.stateEnum = state
        handler.obtainMessage(MessageEnum.MESSAGE_STATE_CHANGED.ordinal, state.ordinal, -1)
            .sendToTarget()
    }

    @Synchronized
    private fun start() {
        if (connectThread != null) {
            connectThread?.cancel()
            connectThread = null
        }

        if (acceptThread == null) {
            acceptThread = AcceptThread()
            acceptThread!!.start()
        }

        if (connectedThread != null) {
            connectedThread?.cancel()
            connectedThread = null
        }

        setState(StateEnum.STATE_LISTEN)
    }

    @Synchronized
    fun stop() {
        if (connectThread != null) {
            connectThread?.cancel()
            connectThread = null
        }

        if (acceptThread != null) {
            acceptThread?.cancel()
            acceptThread = null
        }

        if (connectedThread != null) {
            connectedThread?.cancel()
            connectedThread = null
        }

        setState(StateEnum.STATE_NONE)
    }

    fun connect(device: BluetoothDevice) {
        if (stateEnum == StateEnum.STATE_CONNECTING) {
            connectThread?.cancel()
            connectThread = null
        }

        connectThread = ConnectThread(device)
        connectThread!!.start()

        if (connectedThread != null) {
            connectedThread?.cancel()
            connectedThread = null
        }

        setState(StateEnum.STATE_CONNECTING)
    }

    @SuppressLint("MissingPermission")
    private inner class AcceptThread : Thread() {
        var serverSocket: BluetoothServerSocket?
        private val mSocketType: String


        init {
            var tmp: BluetoothServerSocket? = null

            mSocketType = "Insecure"

            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(appName, UUID.fromString(UUID_STRING))
            } catch (e: IOException) {
                Log.e("Accept.Constructor", e.toString())
            }

            serverSocket = tmp
//            setState(StateEnum.STATE_LISTEN)
        }

        override fun run() {

            var socket: BluetoothSocket? = null
//
//            Log.d("AcceptThread", "Socket Type: " + mSocketType +
//                    "BEGIN mAcceptThread" + this)
//            name = "AcceptThread" + mSocketType


//            // Listen to the server socket if we're not connected
//            while ( stateEnum != StateEnum.STATE_CONNECTED) {
//                try {
//                    // This is a blocking call and will only return on a
//                    // successful connection or an exception
//                    socket = serverSocket?.accept()
//                } catch (e: IOException) {
//                    Log.e("AcceptThread", "Socket Type: " + mSocketType + "accept() failed", e)
//                    break
//                }
//
//                // If a connection was accepted
//                if (socket != null) {
//                    synchronized(this@BluetoothUtils) {
//                        when (stateEnum) {
//                            StateEnum.STATE_LISTEN, StateEnum.STATE_CONNECTING ->
//                                // Situation normal. Start the connected thread.
//                                connected(socket, socket.remoteDevice)
//
//                            StateEnum.STATE_NONE, StateEnum.STATE_CONNECTED ->
//                                // Either not ready or already connected. Terminate new socket.
//                                try {
//                                    socket?.close()
//                                } catch (e: IOException) {
//                                    Log.e("AcceptThread", "Could not close unwanted socket", e)
//                                }
//
//                            else -> {
//                            }
//                        }
//                    }
//                }
//            }
//            Log.i("AcceptThread", "END mAcceptThread, socket Type: " + mSocketType)

            try {
                socket = serverSocket!!.accept()
            } catch (e: IOException) {
                Log.e("Accept.run", e.toString())
                try {
                    serverSocket?.close()
                } catch (e2: IOException) {
                    Log.e("Accept.Close", e2.toString())
                }
            }

            if (socket != null) {
                when (stateEnum) {
                    StateEnum.STATE_LISTEN, StateEnum.STATE_CONNECTING -> {
                        connected(socket, socket.remoteDevice)
                    }
                    else -> {
                        try {
                            socket.close()
                        } catch (e: IOException) {
                            Log.e("Accept.CloseSocket", e.toString())
                        }
                    }
                }
            }
        }

        fun cancel() {
            try {
                serverSocket?.close()
            } catch (e: IOException) {
                Log.e("Accept.CloseServer", e.toString())
            }
        }
    }

    @SuppressLint("MissingPermission")
    inner class ConnectThread(var device: BluetoothDevice) : Thread() {

        private var socket: BluetoothSocket?

        init {
            var tmp: BluetoothSocket? = null

            try {
                tmp = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(UUID_STRING))
//                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(UUID_STRING));

//                Log.i("Connect.Constructor", "Connected to: $tmp")
                /**
                 * https://stackoverflow.com/questions/36785985/buetooth-connection-failed-read-failed-socket-might-closed-or-timeout-read-re#:~:text=I%20got%20success%20when%20I%20changed
                 * */

//                tmp = device.javaClass.getMethod(
//                    "createRfcommSocket", Int::class.javaPrimitiveType
//                ).invoke(device, 1) as BluetoothSocket? // IT WORKS!!!!!!!!! IT FUCKING WORKSSSSSS
//
//                tmp = device.createRfcommSocketToServiceRecord(
//                    UUID.fromString(UUID_STRING))


                Log.i("Connect.Constructor", "Connected to: $tmp")

            } catch (e: IOException) {
                Log.e("Connect.Constructor", e.toString())
            }
            socket = tmp
        }

        override fun run() {

//            // Cancel discovery because it otherwise slows down the connection.
//            // Cancel discovery because it otherwise slows down the connection.
//            bluetoothAdapter.cancelDiscovery()
//
//            try {
//                socket?.connect()
//                Log.d("TESTING", "Connected to shit")
//            } catch (connectException: IOException) {
//                try {
//                    socket?.close()
//                } catch (closeException: IOException) {
//                    Log.e("TEST", "Can't close socket")
//                }
//                connectionFailed(connectException.toString())
//                return
//            }

            bluetoothAdapter.cancelDiscovery()
            try {
                socket?.connect()
            } catch (e: IOException) {
                Log.e("Connect.run", e.toString())
                try {
                    socket?.close()
                } catch (e2: IOException) {
                    Log.e("Connect.CloseSocket", e2.toString())
                }
                connectionFailed(e.toString())
                return
            }

            synchronized(this) {
                connectThread = null
            }

            connected(socket, device)
        }

        fun cancel() {
            try {
                socket?.close()
            } catch (e3: IOException) {
                Log.e("Connect.Cancel", e3.toString())
            }
        }
    }


    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream
//        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream


        override fun run() {
            var numBytes: Int // bytes returned from read()
            var begin: Int = 0
            var bytes: Int = 0

//            // Keep listening to the InputStream until an exception occurs.
//            while (true) {
//                // Read from the InputStream.
//                numBytes = try {
//                    mmInStream.read(mmBuffer)
//                } catch (e: IOException) {
//                    Log.d("ConnectedThread", "Input stream was disconnected", e)
//                    break
//                }
//
//                // Send the obtained bytes to the UI activity.
//                val readMsg: Message = handler.obtainMessage(
//                    MessageEnum.MESSAGE_READ.ordinal, numBytes, -1,
//                    mmBuffer)
//                readMsg.sendToTarget()
//            }

            while (true) {
                try {
                    bytes += mmInStream.read(mmBuffer, bytes, mmBuffer.size - bytes)
                    for (i in begin until bytes) {
                        if (mmBuffer[i] == "#".toByteArray()[0]) {
                            handler.obtainMessage(MessageEnum.MESSAGE_READ.ordinal, begin, i, mmBuffer).sendToTarget()
//                            begin = i + 1
//                            if (i == bytes - 1) {
//                                bytes = 0
//                                begin = 0
//                            }
                            bytes = 0
//                            startTime = System.currentTimeMillis()
//                            Log.d("____REEEEAAADDATA____", "Current time: $startTime milliseconds")

                            break
                        }
                    }
                } catch (e: IOException) {
                    Log.e("InStream", "${e.message}")
                    break
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        fun write(bytes: ByteArray) {
            try {
                mmOutStream.write(bytes)
            } catch (e: IOException) {
                Log.e("write", "Error occurred when sending data", e)

                // Send a failure message back to the activity.
                val writeErrorMsg = handler.obtainMessage(MessageEnum.MESSAGE_TOAST.ordinal)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }
                writeErrorMsg.data = bundle
                handler.sendMessage(writeErrorMsg)
                return
            }

            // Share the sent message with the UI activity.
            val writtenMsg = handler.obtainMessage(
                MessageEnum.MESSAGE_WRITE.ordinal, -1, -1, mmBuffer)
            writtenMsg.sendToTarget()
        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                Log.e("ConnectedThread->Cancel", "Could not close the connect socket", e)
            }
        }
    }

    @Synchronized
    private fun connectionFailed(e: String) {
        var message: Message = handler.obtainMessage(MessageEnum.MESSAGE_TOAST.ordinal)
        var bundle: Bundle = Bundle()
        bundle.putString(MainActivity.toast, "Can't connect to device: $e")
        message.data = bundle
        handler.sendMessage(message)

        this.start()
    }

    @SuppressLint("MissingPermission")
    @Synchronized
    private fun connected(socket: BluetoothSocket?, device: BluetoothDevice) {
        if (connectThread != null) {
            connectThread?.cancel()
            connectThread = null
        }

        if (connectedThread != null) {
            connectedThread?.cancel()
            connectedThread = null
        }

//        val sharedPref = activity.getSharedPreferences(
//            activity.getString(R.string.device_key), Context.MODE_PRIVATE)

        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(activity.getString(R.string.device_address_key), device.address)
            apply()
        }
        connectedThread = ConnectedThread(socket!!)
        connectedThread!!.start()

        var message: Message = handler.obtainMessage(MessageEnum.MESSAGE_DEVICE_NAME.ordinal)
        var bundle: Bundle = Bundle()
        bundle.putString(MainActivity.deviceName, device.name)
        message.data = bundle
        handler.sendMessage(message)

        setState(StateEnum.STATE_CONNECTED)
    }

    fun write(buffer: ByteArray){
        val connThread: ConnectedThread?
        synchronized(this){
            if(stateEnum != StateEnum.STATE_CONNECTED){
                return
            }

            connThread = connectedThread
        }

        connThread?.write(buffer)
    }

    companion object {
//        private const val UUID_STRING = "af7c1fe6-d669-414e-b066-e9733f0de7a8"
//        private const val UUID_STRING = "a3edfc21-44d9-408d-b9c1-390e0f0890c2"
        private const val UUID_STRING = "00001101-0000-1000-8000-00805F9B34FB"
    }
}

//
//package com.toddler.footsteps.bluetooth.utils
//
//import android.annotation.SuppressLint
//import android.bluetooth.BluetoothAdapter
//import android.content.Context
//import android.util.Log
//import android.bluetooth.BluetoothSocket
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothServerSocket
//import java.io.IOException
//import java.io.InputStream
//import java.io.OutputStream
//import android.os.Bundle
//import android.os.Handler
//import com.webianks.bluechat.Constants
//import com.webianks.bluechat.Constants.Companion.MESSAGE_READ
//import java.util.*
//
//
//enum class StateEnum {
//    STATE_NONE,
//    STATE_LISTEN,
//    STATE_CONNECTING,
//    STATE_CONNECTED
//}
//
///**
// * Created by ramankit on 20/7/17.
// */
//
//class BluetoothUtils(context: Context, handler: Handler){
//
//    // Member fields
//    private var mAdapter: BluetoothAdapter? = null
//    private var mHandler: Handler? = null
//    private var mSecureAcceptThread: AcceptThread? = null
//    private var mInsecureAcceptThread: AcceptThread? = null
//    private var mConnectThread: ConnectThread? = null
//    private var mConnectedThread: ConnectedThread? = null
//    private var mState: Int = 0
//    private var mNewState: Int = 0
//
//    private val  TAG: String = javaClass.simpleName
//
//    // Unique UUID for this application
//    private val MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
//    private val MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
//
//
//    // Name for the SDP record when creating server socket
//    private val NAME_SECURE = "BluetoothChatSecure"
//    private val NAME_INSECURE = "BluetoothChatInsecure"
//
//    // Constants that indicate the current connection state
//    companion object {
//        val STATE_NONE = 0       // we're doing nothing
//        val STATE_LISTEN = 1     // now listening for incoming connections
//        val STATE_CONNECTING = 2 // now initiating an outgoing connection
//        val STATE_CONNECTED = 3  // now connected to a remote device
//    }
//
//    init {
//
//        mAdapter = BluetoothAdapter.getDefaultAdapter()
//        mState = STATE_NONE
//        mNewState = mState
//        mHandler = handler
//    }
//
//    /**
//     * Return the current connection state.
//     */
//    @Synchronized fun getState(): Int {
//        return mState
//    }
//
//    /**
//     * Start the chat service. Specifically start AcceptThread to begin a
//     * session in listening (server) mode. Called by the Activity onResume()
//     */
//    @Synchronized fun start() {
//        Log.d(TAG, "start")
//
//        // Cancel any thread attempting to make a connection
//        if (mConnectThread != null) {
//            mConnectThread?.cancel()
//            mConnectThread = null
//        }
//
//        // Cancel any thread currently running a connection
//        if (mConnectedThread != null) {
//            mConnectedThread?.cancel()
//            mConnectedThread = null
//        }
//
//        // Start the thread to listen on a BluetoothServerSocket
//        if (mSecureAcceptThread == null) {
//            mSecureAcceptThread = AcceptThread(true)
//            mSecureAcceptThread?.start()
//        }
//        if (mInsecureAcceptThread == null) {
//            mInsecureAcceptThread = AcceptThread(false)
//            mInsecureAcceptThread?.start()
//        }
//        // Update UI title
//        //updateUserInterfaceTitle()
//    }
//
//
//    /**
//     * Start the ConnectThread to initiate a connection to a remote device.
//
//     * @param device The BluetoothDevice to connect
//     * *
//     * @param secure Socket Security type - Secure (true) , Insecure (false)
//     */
//    @Synchronized fun connect(device: BluetoothDevice?, secure: Boolean) {
//
//        Log.d(TAG, "connect to: " + device)
//
//        // Cancel any thread attempting to make a connection
//        if (mState == STATE_CONNECTING) {
//            if (mConnectThread != null) {
//                mConnectThread?.cancel()
//                mConnectThread = null
//            }
//        }
//
//        // Start the thread to connect with the given device
//        mConnectThread = ConnectThread(device, secure)
//        mConnectThread?.start()
//
//        // Cancel any thread currently running a connection
//        if (mConnectedThread != null) {
//            mConnectedThread?.cancel()
//            mConnectedThread = null
//        }
//
//
//        mState = STATE_CONNECTING
//        // Update UI title
//        //updateUserInterfaceTitle()
//    }
//
//
//    /**
//     * Start the ConnectedThread to begin managing a Bluetooth connection
//
//     * @param socket The BluetoothSocket on which the connection was made
//     * *
//     * @param device The BluetoothDevice that has been connected
//     */
//    @SuppressLint("MissingPermission")
//    @Synchronized fun connected(socket: BluetoothSocket?, device: BluetoothDevice?, socketType: String) {
//        Log.d(TAG, "connected, Socket Type:" + socketType)
//
//        // Cancel the thread that completed the connection
//        if (mConnectThread != null) {
//            mConnectThread?.cancel()
//            mConnectThread = null
//        }
//
//        // Cancel any thread currently running a connection
//        if (mConnectedThread != null) {
//            mConnectedThread?.cancel()
//            mConnectedThread = null
//        }
//
//        // Cancel the accept thread because we only want to connect to one device
//        if (mSecureAcceptThread != null) {
//            mSecureAcceptThread?.cancel()
//            mSecureAcceptThread = null
//        }
//        if (mInsecureAcceptThread != null) {
//            mInsecureAcceptThread?.cancel()
//            mInsecureAcceptThread = null
//        }
//
//        // Start the thread to manage the connection and perform transmissions
//        mConnectedThread = ConnectedThread(socket, socketType)
//        mConnectedThread?.start()
//
//        // Send the name of the connected device back to the UI Activity
//        val msg = mHandler?.obtainMessage(Constants.MESSAGE_DEVICE_NAME)
//        val bundle = Bundle()
//        bundle.putString(Constants.DEVICE_NAME, device?.name)
//        msg?.data = bundle
//        if (msg != null) {
//            mHandler?.sendMessage(msg)
//        }
//        // Update UI title
//        //updateUserInterfaceTitle()
//    }
//
//    /**
//     * Stop all threads
//     */
//    @Synchronized fun stop() {
//        Log.d(TAG, "stop")
//
//        if (mConnectThread != null) {
//            mConnectThread?.cancel()
//            mConnectThread = null
//        }
//
//        if (mConnectedThread != null) {
//            mConnectedThread?.cancel()
//            mConnectedThread = null
//        }
//
//        if (mSecureAcceptThread != null) {
//            mSecureAcceptThread?.cancel()
//            mSecureAcceptThread = null
//        }
//
//        if (mInsecureAcceptThread != null) {
//            mInsecureAcceptThread?.cancel()
//            mInsecureAcceptThread = null
//        }
//        mState = STATE_NONE
//        // Update UI title
//        //updateUserInterfaceTitle()
//    }
//
//    /**
//     * Write to the ConnectedThread in an unsynchronized manner
//
//     * @param out The bytes to write
//     * *
//     * @see ConnectedThread.write
//     */
//    fun write(out: ByteArray) {
//        // Create temporary object
//        var r: ConnectedThread?  = null
//        // Synchronize a copy of the ConnectedThread
//        synchronized(this) {
//            if (mState != STATE_CONNECTED) return
//            r = mConnectedThread
//        }
//        // Perform the write unsynchronized
//        r?.write(out)
//    }
//
//
//    /**
//     * Indicate that the connection attempt failed and notify the UI Activity.
//     */
//    private fun connectionFailed() {
//        // Send a failure message back to the Activity
//        val msg = mHandler?.obtainMessage(Constants.MESSAGE_TOAST)
//        val bundle = Bundle()
//        bundle.putString(Constants.TOAST, "Unable to connect device")
//        msg?.data = bundle
//        if (msg != null) {
//            mHandler?.sendMessage(msg)
//        }
//
//        mState = STATE_NONE
//        // Update UI title
//        //updateUserInterfaceTitle()
//
//        // Start the service over to restart listening mode
//        this@BluetoothUtils.start()
//    }
//
//    /**
//     * Indicate that the connection was lost and notify the UI Activity.
//     */
//    private fun connectionLost() {
//        // Send a failure message back to the Activity
//        val msg = mHandler?.obtainMessage(Constants.MESSAGE_TOAST)
//        val bundle = Bundle()
//        bundle.putString(Constants.TOAST, "Device connection was lost")
//        msg?.data = bundle
//        mHandler?.sendMessage(msg!!)
//
//        mState = STATE_NONE
//        // Update UI title
//        // updateUserInterfaceTitle()
//
//        // Start the service over to restart listening mode
//        this@BluetoothUtils.start()
//    }
//
//
//    /**
//     * This thread runs while listening for incoming connections. It behaves
//     * like a server-side client. It runs until a connection is accepted
//     * (or until cancelled).
//     */
//    @SuppressLint("MissingPermission")
//    private inner class AcceptThread(secure: Boolean) : Thread() {
//        // The local server socket
//        private val mmServerSocket: BluetoothServerSocket?
//        private val mSocketType: String
//
//        init {
//            var tmp: BluetoothServerSocket? = null
//            mSocketType = if (secure) "Secure" else "Insecure"
//
//            // Create a new listening server socket
//            try {
//                if (secure) {
//                    tmp = mAdapter?.listenUsingRfcommWithServiceRecord(NAME_SECURE,
//                        MY_UUID_SECURE)
//                } else {
//                    tmp = mAdapter?.listenUsingInsecureRfcommWithServiceRecord(
//                        NAME_INSECURE, MY_UUID_INSECURE)
//                }
//            } catch (e: IOException) {
//                Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e)
//            }
//
//            mmServerSocket = tmp
//            mState = STATE_LISTEN
//        }
//
//        override fun run() {
//
//            Log.d(TAG, "Socket Type: " + mSocketType +
//                    "BEGIN mAcceptThread" + this)
//            name = "AcceptThread" + mSocketType
//
//            var socket: BluetoothSocket?
//
//            // Listen to the server socket if we're not connected
//            while (mState != STATE_CONNECTED) {
//                try {
//                    // This is a blocking call and will only return on a
//                    // successful connection or an exception
//                    socket = mmServerSocket?.accept()
//                } catch (e: IOException) {
//                    Log.e(TAG, "Socket Type: " + mSocketType + "accept() failed", e)
//                    break
//                }
//
//                // If a connection was accepted
//                if (socket != null) {
//                    synchronized(this@BluetoothUtils) {
//                        when (mState) {
//                            STATE_LISTEN, STATE_CONNECTING ->
//                                // Situation normal. Start the connected thread.
//                                connected(socket, socket?.remoteDevice,
//                                    mSocketType)
//                            STATE_NONE, STATE_CONNECTED ->
//                                // Either not ready or already connected. Terminate new socket.
//                                try {
//                                    socket?.close()
//                                } catch (e: IOException) {
//                                    Log.e(TAG, "Could not close unwanted socket", e)
//                                }
//
//                            else -> {
//                            }
//                        }
//                    }
//                }
//            }
//            Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType)
//
//        }
//
//        fun cancel() {
//            Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this)
//            try {
//                mmServerSocket?.close()
//            } catch (e: IOException) {
//                Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e)
//            }
//
//        }
//    }
//
//
//    /**
//     * This thread runs while attempting to make an outgoing connection
//     * with a device. It runs straight through; the connection either
//     * succeeds or fails.
//     */
//    @SuppressLint("MissingPermission")
//    private inner class ConnectThread(private val mmDevice: BluetoothDevice?, secure: Boolean) : Thread() {
//        private val mmSocket: BluetoothSocket?
//        private val mSocketType: String
//
//        init {
//            var tmp: BluetoothSocket? = null
//            mSocketType = if (secure) "Secure" else "Insecure"
//
//            // Get a BluetoothSocket for a connection with the
//            // given BluetoothDevice
//            try {
//                if (secure) {
//                    tmp = mmDevice?.createRfcommSocketToServiceRecord(
//                        MY_UUID_SECURE)
//                } else {
//                    tmp = mmDevice?.createInsecureRfcommSocketToServiceRecord(
//                        MY_UUID_INSECURE)
//                }
//            } catch (e: IOException) {
//                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e)
//            }
//
//            mmSocket = tmp
//            mState = STATE_CONNECTING
//        }
//
//        override fun run() {
//
//            Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType)
//            name = "ConnectThread" + mSocketType
//
//            // Always cancel discovery because it will slow down a connection
//            mAdapter?.cancelDiscovery()
//
//            // Make a connection to the BluetoothSocket
//            try {
//                // This is a blocking call and will only return on a
//                // successful connection or an exception
//                mmSocket?.connect()
//
//            } catch (e: IOException) {
//                // Close the socket
//                try {
//                    mmSocket?.close()
//                } catch (e2: IOException) {
//                    Log.e(TAG, "unable to close() " + mSocketType +
//                            " socket during connection failure", e2)
//                }
//
//                connectionFailed()
//                return
//            }
//
//            // Reset the ConnectThread because we're done
//            synchronized(this@BluetoothUtils) {
//                mConnectThread = null
//            }
//
//            // Start the connected thread
//            connected(mmSocket, mmDevice, mSocketType)
//        }
//
//        fun cancel() {
//            try {
//                mmSocket?.close()
//            } catch (e: IOException) {
//                Log.e(TAG, "close() of connect $mSocketType socket failed", e)
//            }
//
//        }
//    }
//
//    /**
//     * This thread runs during a connection with a remote device.
//     * It handles all incoming and outgoing transmissions.
//     */
//    private inner class ConnectedThread(private val mmSocket: BluetoothSocket?, socketType: String) : Thread() {
//
//        private val mmInStream: InputStream?
//        private val mmOutStream: OutputStream?
//        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream
//
//
//        init {
//            Log.d(TAG, "create ConnectedThread: " + socketType)
//            var tmpIn: InputStream? = null
//            var tmpOut: OutputStream? = null
//
//            // Get the BluetoothSocket input and output streams
//            try {
//                tmpIn = mmSocket?.inputStream
//                tmpOut = mmSocket?.outputStream
//            } catch (e: IOException) {
//                Log.e(TAG, "temp sockets not created", e)
//            }
//
//            mmInStream = tmpIn
//            mmOutStream = tmpOut
//            mState = STATE_CONNECTED
//        }
//
//        /*override fun run() {
//            Log.i(TAG, "BEGIN mConnectedThread")
//            val buffer = ByteArray(1024)
//            var bytes: Int
//
//            // Keep listening to the InputStream while connected
//            while (mState == STATE_CONNECTED) {
//                try {
//                    // Read from the InputStream
//                    bytes = mmInStream?.read(buffer) ?: 0
//
//                    // Send the obtained bytes to the UI Activity
//                    mHandler?.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer)
//                            ?.sendToTarget()
//                } catch (e: IOException) {
//                    Log.e(TAG, "disconnected", e)
//                    connectionLost()
//                    break
//                }
//
//            }
//        }*/
//
//
//        override fun run() {
//            var numBytes: Int // bytes returned from read()
//            var begin: Int = 0
//            var bytes: Int = 0
//
//            while (true) {
//                try {
//                    bytes += mmInStream!!.read(mmBuffer, bytes, mmBuffer.size - bytes)
//                    for (i in begin until bytes) {
//                        if (mmBuffer[i] == "#".toByteArray()[0]) {
//                            mHandler?.obtainMessage(MESSAGE_READ, begin, i, mmBuffer)?.sendToTarget()
////                            begin = i + 1
////                            if (i == bytes - 1) {
////                                bytes = 0
////                                begin = 0
////                            }
//                            bytes = 0
////                            startTime = System.currentTimeMillis()
////                            Log.d("____REEEEAAADDATA____", "Current time: $startTime milliseconds")
//
//                            break
//                        }
//                    }
//                } catch (e: IOException) {
//                    Log.e("InStream", "${e.message}")
//                    connectionLost()
//                    break
//                }
//            }
//        }
//
//
//        /**
//         * Write to the connected OutStream.
//
//         * @param buffer The bytes to write
//         */
//        fun write(buffer: ByteArray) {
//            try {
//                mmOutStream?.write(buffer)
//
//                // Share the sent message back to the UI Activity
//                mHandler?.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer)
//                    ?.sendToTarget()
//            } catch (e: IOException) {
//                Log.e(TAG, "Exception during write", e)
//            }
//
//        }
//
//        fun cancel() {
//            try {
//                mmSocket?.close()
//            } catch (e: IOException) {
//                Log.e(TAG, "close() of connect socket failed", e)
//            }
//
//        }
//    }
//
//
//}