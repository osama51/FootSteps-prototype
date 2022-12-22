package com.toddler.bluecomm.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.toddler.bluecomm.MainActivity
import com.toddler.bluecomm.R
import com.toddler.bluecomm.databinding.ActivityDeviceListBinding

class DeviceListActivity : AppCompatActivity(),
    AvailableListFragment.OnFetchAvailableAddressListener, PairedListFragment.OnFetchPairedAddressListener {

    companion object {
        const val TAG = "DEVICE_LIST_ACTIVITY"
    }

    private lateinit var binding: ActivityDeviceListBinding
    private lateinit var context: Context
    private lateinit var viewModel: BluetoothViewModel

    private lateinit var listPairedDevices: RecyclerView
    private lateinit var listAvailableDevices: RecyclerView
    private lateinit var adapterPairedDevices: DeviceAdapter
    private lateinit var adapterAvailableDevices: DeviceAdapter

    private lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_device_list)
        binding = ActivityDeviceListBinding.inflate(layoutInflater)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController


        viewModel = ViewModelProvider(this)[BluetoothViewModel::class.java]
        binding.lifecycleOwner = this
        context = this


        setContentView(binding.root)

//        val intent = Intent(baseContext, MainActivity::class.java)
//        intent.putExtra("deviceAddress", address)
//        startActivity(intent)
//        setResult(SELECT_DEVICE, )
    }

    override fun connectDevice(address: String?, name: String?, device: BluetoothDevice?) {
//        val intent = Intent(baseContext,MainActivity::class.java)
        intent.putExtra("deviceAddress", address)
        intent.putExtra("deviceName", name)
        intent.putExtra("device", device)
//        startActivity(intent)
        setResult(RESULT_OK, intent)
    }

}
    private const val SELECT_DEVICE = 20

