package com.toddler.footsteps

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.toddler.footsteps.database.reference.User
import com.toddler.footsteps.database.reference.UserDatabase
import com.toddler.footsteps.ui.ReferenceViewModel
import com.toddler.footsteps.utils.ConvertDate

class GridAdapter(
    private val context: Context,
    private val gridView: GridView,
    private val items: List<User>,
    private val itemClickListener: GridItemClickListener
    ) : BaseAdapter() {

    private var selectedItemPosition = -1

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder



        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.reference_grid_item, parent, false)
            // Initialize the view holder
            viewHolder = ViewHolder(view)
            // Set the view holder as tag of the view for easy access
            view.tag = viewHolder
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            // what this means is that if the view is not null, then we are reusing the view
            // and we are getting the viewholder from the tag
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val item = items[position]
        // Convert the timestamp to a readable format


        // Populate the data from the data object via the viewHolder object
        viewHolder.titleTextView.text = item.title
        viewHolder.dateTextView.text = ConvertDate().getDateFromTimestamp(item.timestamp).toString()
//        viewHolder.timeTextView.text = ConvertDate().getTimeFromTimestamp(item.timestamp)

//        if (position == selectedItemPosition) {
        if (item.selected) {
            // Customize the appearance or behavior of the selected item
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        } else {
            // Reset the appearance or behavior of non-selected items
            view.setBackgroundColor(context.resources.getColor(R.color.gray_opaque))
        }

        view.setOnLongClickListener {
            itemClickListener.onItemLongClicked(item, position)
            true
        }

        view.setOnClickListener {
            itemClickListener.onItemClicked(item, position)
        }

        return view
    }

    interface GridItemClickListener {
        fun onItemLongClicked(item: User, position: Int)

        fun onItemClicked(item: User, position: Int)
    }

    private class ViewHolder(view: View) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val timeTextView: TextView = view.findViewById(R.id.titleTextView)
    }
}
