package com.toddler.bluecomm.bluetooth

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.toddler.bluecomm.R
import com.toddler.bluecomm.databinding.FragmentAvailableListBinding

class AvailableListFragment : Fragment() {

    private lateinit var viewModel: BluetoothViewModel
    private lateinit var binding: FragmentAvailableListBinding
    private lateinit var listAvailableDevices: RecyclerView
    private lateinit var adapterAvailableDevices: DeviceAdapter

    interface OnFetchAvailableAddressListener {
        fun connectDevice(address: String?, name: String?, device: BluetoothDevice?)
    }

    var fetchAddressListener: OnFetchAvailableAddressListener? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        fetchAddressListener = try {
            activity as OnFetchAvailableAddressListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onFetchAddressListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_available_list, container, false)
        viewModel = ViewModelProvider(requireActivity())[BluetoothViewModel::class.java]
//        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        binding.viewModel = viewModel
        viewModel.updateDevicesList()
        scanDevices()
        init()

        binding.swipeAvailableLayout.setOnRefreshListener {
            viewModel.updateDevicesList()
            scanDevices()
            init()
            binding.availableDeviceList.adapter = adapterAvailableDevices

            viewModel.pairedDevices.observe(viewLifecycleOwner) {
                it?.let {
                    adapterAvailableDevices.submitList(it as MutableList<BluetoothDevice>)
                }
            }
            binding.swipeAvailableLayout.isRefreshing = false
        }

        binding.availableDeviceList.adapter = adapterAvailableDevices

        viewModel.pairedDevices.observe(viewLifecycleOwner) {
            it?.let {
                adapterAvailableDevices.submitList(it as MutableList<BluetoothDevice>)
            }
        }
        viewModel.selectedDevice.observe(viewLifecycleOwner) {
            it?.let {
//                this.findNavController()
//                    .navigate(MainFragmentDirections.actionShowDetail(it))
//                viewModel.doneNavigating()
                // TODO: ACTION WHEN DEVICE IS PRESSED
            }
        }
        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun init() {
        listAvailableDevices = binding.availableDeviceList
//        adapterAvailableDevices = DeviceAdapter(viewModel.availableDevices)
        adapterAvailableDevices = DeviceAdapter(DeviceAdapter.DeviceListener {
            viewModel.deviceSelected(it)
        })
        listAvailableDevices.adapter = adapterAvailableDevices
        listAvailableDevices.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        /** Listen to Discover Devices*/
        val intentFilter: IntentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity?.registerReceiver(viewModel?.let { AvailableDevicesBroadcastReceiver(it) }, intentFilter)
        val intentFilter2: IntentFilter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        activity?.registerReceiver(viewModel?.let { AvailableDevicesBroadcastReceiver(it) }, intentFilter2)

    }

    private fun scanDevices() {
        viewModel.scanDevices()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.device_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_paired_devices -> {
                view?.findNavController()
                    ?.navigate(AvailableListFragmentDirections.actionAvailableListFragmentToPairedListFragment())
                Toast.makeText(context, "Paired Devices", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}