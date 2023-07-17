package com.toddler.footsteps.ui

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.toddler.footsteps.MainActivity
import com.toddler.footsteps.StateEnum
import com.toddler.footsteps.databinding.FragmentRefinstructionsBinding


class RefInstructionsFragment: Fragment() {

    private lateinit var binding: FragmentRefinstructionsBinding


    private lateinit var animator: ValueAnimator
    private lateinit var animatorScaleDown: AnimatorSet
    private lateinit var animatorScaleUp: AnimatorSet
    private var isTouching = false
    private var longPressRunnable: Runnable? = null
    private var referenceSet: Boolean = false

    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRefinstructionsBinding.inflate(inflater, container, false)

        // get reference to the main activity
        mainActivity = activity as MainActivity

        binding.floatingActionButton.setOnClickListener{
            true
        }

        binding.floatingActionButton.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    isTouching = true
                    startLongPressRunnable()
                }
                MotionEvent.ACTION_UP -> {
                    isTouching = false
                    stopLongPressRunnable()
                    if (!referenceSet) {
                       Toast.makeText(
                            requireContext(),
                            "Hold for 3 sec to add a reference",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
            false
        }


        return binding.root

    }


    private fun startPulsatingAnimation() {

    }

    private fun cancelPulsatingAnimation() {

    }

    private fun startLongPressRunnable() {
        stopLongPressRunnable() // Stop the previous runnable if any
        longPressRunnable = kotlinx.coroutines.Runnable {
            if (isTouching) {
                referenceSet = true
                cancelPulsatingAnimation()
                mainActivity.addUserToDatabase(mainActivity.leftFoot, mainActivity.rightFoot, mainActivity.leftAcc, mainActivity.rightAcc)
                Toast.makeText(requireContext(), "New Reference Added", Toast.LENGTH_LONG).show()
                mainActivity.fragmentManager.popBackStack()
            }
        }
        mainActivity.handler.postDelayed(longPressRunnable!!, 3000) // 3 seconds
    }

    private fun stopLongPressRunnable() {
        longPressRunnable?.let {
            mainActivity.handler.removeCallbacks(it)
            longPressRunnable = null
        }
    }


}