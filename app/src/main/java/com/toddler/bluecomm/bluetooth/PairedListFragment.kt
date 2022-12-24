package com.toddler.bluecomm.bluetooth

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.toddler.bluecomm.R
import com.toddler.bluecomm.databinding.FragmentPairedListBinding

class PairedListFragment : Fragment() {

    private lateinit var viewModel: BluetoothViewModel
    private lateinit var binding: FragmentPairedListBinding
    private lateinit var listPairedDevices: RecyclerView
    private lateinit var adapterPairedDevices: DeviceAdapter

    interface OnFetchPairedAddressListener {
        fun connectDevice(address: String?, name: String?, device: BluetoothDevice?)
    }

    var fetchAddressListener: OnFetchPairedAddressListener? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        fetchAddressListener = try {
            activity as OnFetchPairedAddressListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onFetchAddressListener")
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
//        binding =
//            DataBindingUtil.inflate(inflater, R.layout.fragment_paired_list, container, false)
        viewModel = ViewModelProvider(requireActivity())[BluetoothViewModel::class.java]
//        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        binding = FragmentPairedListBinding.inflate(inflater)


        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        init()

//       binding.pairedDeviceList.adapter = adapterPairedDevices
        viewModel.pairedDevices.observe(viewLifecycleOwner) {
            it?.let {
//                Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
                init()
                adapterPairedDevices.submitList(it)
            }
        }
        /** When refreshing you update the paired device list..
         * which triggers pairedDevices.observe   */
        binding.swipePairedLayout.setOnRefreshListener {
            viewModel.updateDevicesList()
            binding.swipePairedLayout.isRefreshing = false
        }

        viewModel.selectedDevice.observe(viewLifecycleOwner) {
            it?.let {
//                this.findNavController()
//                    .navigate(MainFragmentDirections.actionShowDetail(it))
//                viewModel.doneNavigating()
                // TODO: ACTION WHEN DEVICE IS PRESSED

//                var address: String = it.address
//                val intent = Intent(context, MainActivity::class.java)
//                intent.putExtra("deviceAddress", address)
//                startActivity(intent)
                fetchAddressListener?.connectDevice(it.address, it.name, it)
                activity?.finish()
            }
        }

        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun init() {
        listPairedDevices = binding.pairedDeviceList
//        adapterPairedDevices = DeviceAdapter(viewModel.pairedDevices)
        adapterPairedDevices = DeviceAdapter(DeviceAdapter.DeviceListener {
            viewModel.deviceSelected(it)
        })
        listPairedDevices.adapter = adapterPairedDevices
        listPairedDevices.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.device_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_available_devices -> {
                view?.findNavController()
                    ?.navigate(PairedListFragmentDirections.actionPairedListFragmentToAvailableListFragment())
                Toast.makeText(context, "Available Devices", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "PAIRED_DEVICES"
    }

}