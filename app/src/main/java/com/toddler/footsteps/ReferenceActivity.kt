package com.toddler.footsteps

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.toddler.footsteps.database.reference.User
import com.toddler.footsteps.database.reference.UserDao
import com.toddler.footsteps.database.reference.UserDatabase
import com.toddler.footsteps.databinding.ActivityDeviceListBinding
import com.toddler.footsteps.databinding.ActivityReferenceBinding
import com.toddler.footsteps.ui.ReferenceViewModel
import kotlinx.coroutines.launch


class ReferenceActivity : AppCompatActivity(), GridAdapter.GridItemClickListener {

    private lateinit var viewModel: ReferenceViewModel
    private lateinit var binding: ActivityReferenceBinding
    private lateinit var context: Context

    private lateinit var gridView: GridView
    private var gridViewScrollPosition: Int = 0

    private lateinit var addFAB: FloatingActionButton
    private lateinit var deleteFAB: FloatingActionButton
    private lateinit var dao: UserDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_reference)
        binding = ActivityReferenceBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[ReferenceViewModel::class.java]

        dao = UserDatabase.getInstance(application).userDao


        supportActionBar?.hide()

//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
//        val navController: NavController = navHostFragment.navController
//        setContentView(R.layout.activity_main)


        binding.lifecycleOwner = this
        context = this


        Log.i("ReferenceActivity", "::::::::ReferenceActivity Created::::::::")
        gridView = binding.usersGridView


        lifecycleScope.launch {
            dao.getAllUsers().observe(this@ReferenceActivity) { userList ->
                gridView.adapter = GridAdapter(context, gridView, userList, this@ReferenceActivity)
                gridView.setSelection(gridViewScrollPosition)
                Log.i("ReferenceActivity", ":::::ReferenceActivity users updated: ${userList}:::")
            }
        }
        addFAB = binding.addUserFab
        deleteFAB = binding.deleteUsersFab

        addFAB.setOnClickListener {
            // add a new user to the database
            val user = User()
            user.apply {
                title = "User ${System.currentTimeMillis()}"
                timestamp = System.currentTimeMillis()
                selected = true
//                Add sensors data here
            }
            viewModel.insertUser(user)
        }

        deleteFAB.setOnClickListener {
            viewModel.deleteAllUsers()
        }

        setContentView(binding.root)
    }

    private fun updateGridView() {
        lifecycleScope.launch {
            dao.getAllUsers().observe(this@ReferenceActivity) { userList ->
                gridView.adapter = GridAdapter(context, gridView, userList, this@ReferenceActivity)
                gridView.setSelection(gridViewScrollPosition)
                Log.i("ReferenceActivity", ":::::ReferenceActivity users updated: ${userList}:::")
            }
        }
    }

    override fun onItemLongClicked(item: User, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClicked(item: User, position: Int) {

        if(item.selected){ return@onItemClicked }

        item.selected = true
        viewModel.setOtherUsersNotSelected(item.id)
        viewModel.updateUser(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        gridViewScrollPosition = gridView.firstVisiblePosition
        outState.putInt("GRIDVIEW_SCROLL_POSITION", gridViewScrollPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        gridViewScrollPosition = savedInstanceState.getInt("GRIDVIEW_SCROLL_POSITION", 0)
    }
}

