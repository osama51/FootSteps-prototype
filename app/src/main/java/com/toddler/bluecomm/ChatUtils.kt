package com.toddler.bluecomm

import android.annotation.SuppressLint
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


enum class StateEnum {
    STATE_NONE,
    STATE_LISTEN,
    STATE_CONNECTING,
    STATE_CONNECTED
}

class ChatUtils(context: Context, handler: Handler) {

    private var context: Context = context
    private var handler: Handler = handler
    private var connectThread: ConnectThread? = null
    private var acceptThread: AcceptThread? = null
    private var connectedThread: ConnectedThread? = null
    private var bluetoothAdapter: BluetoothAdapter
    private var appUuid: UUID = UUID.fromString(UUID_STRING)
    private var appName: String = "FootStepsApp"
    private var stateEnum: StateEnum = StateEnum.STATE_NONE

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

    fun connectAndStartThread(device: BluetoothDevice) {
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

        init {
            var tmp: BluetoothServerSocket? = null

            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(appName, appUuid)
            } catch (e: IOException) {
                Log.e("Accept.Constructor", e.toString())
            }

            serverSocket = tmp
        }

        override fun run() {
            var socket: BluetoothSocket? = null

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
                        connect(socket, socket.remoteDevice)
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
//                tmp = device.createInsecureRfcommSocketToServiceRecord(appUuid)
                /**
                 * https://stackoverflow.com/questions/36785985/buetooth-connection-failed-read-failed-socket-might-closed-or-timeout-read-re#:~:text=I%20got%20success%20when%20I%20changed
                 * */
                tmp = device.javaClass.getMethod(
                    "createRfcommSocket", Int::class.javaPrimitiveType
                ).invoke(device, 1) as BluetoothSocket? // IT WORKS!!!!!!!!! IT FUCKING WORKSSSSSS
            } catch (e: IOException) {
                Log.e("Connect.Constructor", e.toString())
            }
            socket = tmp
        }

        override fun run() {
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

            connect(socket, device)
        }

        fun cancel() {
            try {
                socket?.close()
            } catch (e3: IOException) {
                Log.e("Connect.Cancel", e3.toString())

            }
        }
    }

//    inner class ConnectedThread(var socket: BluetoothSocket) : Thread() {
//        private var inputStream: InputStream?
//        private var outputStream: OutputStream?
//
//        var tempInput: InputStream? = null
//        var tempOutput: OutputStream? = null
//
//        init {
//            try {
//                tempInput = socket.inputStream
//                tempOutput = socket.outputStream
//            } catch (e: IOException) {
//
//            }
//
//            inputStream = tempInput
//            outputStream = tempOutput
//        }
//
//        override fun run() {
//            super.run()
//            var buffer: ByteArray = ByteArray(1024)
//            var bytes: Int
//
//            try {
//                bytes = inputStream!!.read(buffer)
//
//                handler.obtainMessage(MessageEnum.MESSAGE_READ.ordinal, bytes, -1, buffer)
//                    .sendToTarget()
//            } catch (e: IOException) {
//                Toast.makeText(context, "Connection lost", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        fun cancel() {
//            try {
//                socket?.close()
//            } catch (e3: IOException) {
//                Log.e("Connect.Cancel", e3.toString())
//
//            }
//        }
//    }

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

//    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
//        private val mmInStream: InputStream?
//        private val mmOutStream: OutputStream?
//        override fun run() {
//            val buffer = ByteArray(1024)
//            var begin = 0
//            var bytes = 0
//            while (true) {
//                try {
//                    bytes += mmInStream!!.read(buffer, bytes, buffer.size - bytes)
//                    for (i in begin until bytes) {
//                        if (buffer[i] == "#".toByteArray()[0]) {
//                            handler.obtainMessage(1, begin, i, buffer).sendToTarget()
//                            begin = i + 1
//                            if (i == bytes - 1) {
//                                bytes = 0
//                                begin = 0
//                            }
//                        }
//                    }
//                } catch (e: IOException) {
//                    break
//                }
//            }
//        }
//
//        fun write(bytes: ByteArray?) {
//            try {
//                mmOutStream!!.write(bytes)
//            } catch (e: IOException) {
//            }
//        }
//
//        fun cancel() {
//            try {
//                mmSocket.close()
//            } catch (e: IOException) {
//            }
//        }
//
//        init {
//            var tmpIn: InputStream? = null
//            var tmpOut: OutputStream? = null
//            try {
//                tmpIn = mmSocket.inputStream
//                tmpOut = mmSocket.outputStream
//            } catch (e: IOException) {
//            }
//            mmInStream = tmpIn
//            mmOutStream = tmpOut
//        }
//    }


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
    private fun connect(socket: BluetoothSocket?, device: BluetoothDevice) {
        if (connectThread != null) {
            connectThread?.cancel()
            connectThread = null
        }

        if (connectedThread != null) {
            connectedThread?.cancel()
            connectedThread = null
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
        private const val UUID_STRING = "af7c1fe6-d669-414e-b066-e9733f0de7a8"
    }
}