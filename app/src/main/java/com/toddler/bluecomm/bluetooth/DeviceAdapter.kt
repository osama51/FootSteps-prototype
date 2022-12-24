package com.toddler.bluecomm.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.toddler.bluecomm.R
import com.toddler.bluecomm.databinding.ItDeviceBinding
import kotlinx.android.synthetic.main.it_device.view.*

class DeviceAdapter(private val clickListener: DeviceListener) :
    ListAdapter<BluetoothDevice, DeviceAdapter.ViewHolder>(DiffCallback()) {

    inner class DeviceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.it_device, parent, false)
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: ItDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: DeviceListener, device: BluetoothDevice) {
            binding.device = device
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItDeviceBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class DiffCallback() : DiffUtil.ItemCallback<BluetoothDevice>() {
        override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
            return oldItem == newItem
        }
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deviceItem = getItem(position)
        holder.bind(clickListener, deviceItem)
        holder.itemView.apply {
            title.text = deviceItem.name
            description.text = deviceItem.address
        }
    }

//    override fun getItemCount(): Int {
//        return devices.value!!.size
//    }

    class DeviceListener(val clickListener: (device: BluetoothDevice) -> Unit) {
        fun onClick(device: BluetoothDevice) = clickListener(device)
    }
}