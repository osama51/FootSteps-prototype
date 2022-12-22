package com.toddler.bluecomm.bluetooth


import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


/**
 *                 You can't share a ViewModel across Activities.
 *      That's specifically one of the downsides of using multiple activities
 *
 * */

@SuppressLint("MissingPermission")
class BluetoothViewModel(application: Application) : AndroidViewModel(application) {

    private val context by lazy { getApplication<Application>().applicationContext }
    private var bluetoothAdapter: BluetoothAdapter

    private val bluetoothManager = context.getSystemService(Application.BLUETOOTH_SERVICE) as BluetoothManager


    // apparently having these as LiveData is an awful idea! ╯︿╰
    val pairedDevices = MutableLiveData<MutableList<BluetoothDevice?>>()
    val availableDevices = MutableLiveData<MutableList<BluetoothDevice?>>()

    val noDevices: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val finished: MutableLiveData<Boolean> = MutableLiveData<Boolean>()


//    var pairedDevices: MutableSet<BluetoothDevice>? = bluetoothAdapter.bondedDevices
//    var availableDevices: MutableSet<BluetoothDevice>? = bluetoothAdapter.bondedDevices

    private val _selectedDevice = MutableLiveData<BluetoothDevice?>()
    val selectedDevice: LiveData<BluetoothDevice?>
        get() = _selectedDevice

    var connectFlag = false

//    private val _selectedDeviceAddress = MutableLiveData<BluetoothDevice?>()
//    val selectedDeviceAddress: LiveData<BluetoothDevice?>
//        get() = _selectedDeviceAddress

    fun doneNavigating() {
        _selectedDevice.value = null
    }
    init {
        Log.i("BluetoothViewModel", "BluetoothViewModel Created")
        noDevices.value = false
        finished.value = true
        bluetoothAdapter = bluetoothManager.adapter
        updateDevicesList()

        _selectedDevice.value = null
    }

    fun updateDevicesList() {
        Log.i("updateDevicesList", "BluetoothViewModel UPDATING")
        pairedDevices.value = bluetoothAdapter.bondedDevices.toMutableList()
        availableDevices.value = mutableSetOf<BluetoothDevice>().toMutableList()
    }

    fun scanDevices() {
        if(bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }
        bluetoothAdapter.startDiscovery()
//        finished.value =false
    }

    fun deviceSelected(device: BluetoothDevice) {
        _selectedDevice.value = device
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("BluetoothViewModel", "BluetoothViewModel destroyed")
    }
}