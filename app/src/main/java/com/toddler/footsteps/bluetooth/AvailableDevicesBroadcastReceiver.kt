package com.toddler.footsteps.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AvailableDevicesBroadcastReceiver(private val viewModel: BluetoothViewModel) : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?)

    {
        val action = intent?.action
        /**
         *
         *          LIVEDATA
         *             IS
         *         NOT WORKING
         *
         * */
        when(action){
            BluetoothDevice.ACTION_FOUND -> {
                viewModel.finished.value = false
                var device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                if (device != null) {
                    /** If not paired, then add to available devices*/
                    if (device.bondState != BluetoothDevice.BOND_BONDED) {
                        viewModel.noDevices.value = false
                        viewModel.availableDevices.value?.add(device)
                    }
                }
            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                if (viewModel.availableDevices.value!!.isEmpty()) {
                    Toast.makeText(context, "No new devices found", Toast.LENGTH_LONG).show()
                    viewModel.finished.value = true
                    viewModel.noDevices.value = true
                }
            }
        }
    }


}