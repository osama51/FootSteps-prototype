package com.toddler.footsteps.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.toddler.footsteps.R
import com.toddler.footsteps.Screens
import com.toddler.footsteps.chat.ChatViewModel
import com.toddler.footsteps.databinding.FragmentAssessBinding

enum class ShowHide {
    HIDE,
    SHOW
}
class AssessFragment: Fragment() {



    private lateinit var statsViewModel: StatsViewModel
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var binding: FragmentAssessBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAssessBinding.inflate(inflater, container, false)
        statsViewModel = ViewModelProvider(requireActivity())[StatsViewModel::class.java]
        chatViewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]
        val fragmentManager = requireActivity().supportFragmentManager

        // Get the root view of the fragment
        val rootView = binding.root

        // Disable touch events on the root view to prevent passing through to the activity's views
        rootView.isClickable = true
        rootView.isFocusableInTouchMode = true
        rootView.setOnTouchListener { _, _ -> true }

        // go back to the previous fragment upon clicking the back button on the toolbar
        binding.toolbar.setNavigationOnClickListener {
//            requireActivity().onBackPressed()
            fragmentManager.popBackStack()
        }

        return binding.root
    }


    override fun onStop() {
        showHideBottomNavBar(ShowHide.SHOW)
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        chatViewModel.setScreen(Screens.STATISTICS_SCREEN)
        showHideBottomNavBar(ShowHide.HIDE)
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }



    private fun showHideBottomNavBar(showHide: ShowHide) {

        var fromY = 0f
        var toY = 1f
        var viewVisibility = View.GONE

        if(showHide == ShowHide.SHOW) {
            fromY = 1f
            toY = 0f
            viewVisibility = View.VISIBLE
        }

        // get a view from the main activity
        val view = requireActivity().findViewById<View>(R.id.customBottomNavBar)


        // Create a TranslateAnimation to animate the view's position
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, fromY,
            Animation.RELATIVE_TO_SELF, toY
        )

        // Set the duration and interpolator for the animation
        animation.duration = 500
        animation.interpolator = AccelerateInterpolator()

        // Set the AnimationListener to handle the view's visibility change
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Set the visibility of the view to "gone" after the animation ends
                view.visibility = viewVisibility
            }
        })
        view.startAnimation(animation)
    }
}