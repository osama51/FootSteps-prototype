package com.toddler.footsteps

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.toddler.footsteps.database.reference.User
import com.toddler.footsteps.databinding.ActivityReferenceBinding
import com.toddler.footsteps.ui.ReferenceViewModel


class ReferenceActivity : AppCompatActivity() {

private lateinit var viewModel: ReferenceViewModel
private lateinit var binding: ActivityReferenceBinding
private lateinit var gridView : GridView
private lateinit var addFAB : FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        viewModel = ViewModelProvider(this)[ReferenceViewModel::class.java]

        binding = DataBindingUtil.setContentView(this, R.layout.activity_reference)

        gridView = binding.usersGridView
        addFAB = binding.addUserFab

        addFAB.setOnClickListener {
            // add a new user to the database
            val user = User()
            user.apply {
                title = "User ${System.currentTimeMillis()}"
                timestamp = System.currentTimeMillis()
                selected = true
                TODO("Add sensors data here")
            }
            viewModel.insertUser(user)
        }

        // get the users list from the room database
        val users = ArrayList<User>()



    }
}
