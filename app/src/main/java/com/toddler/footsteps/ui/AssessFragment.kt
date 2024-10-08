package com.toddler.footsteps.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.bitvale.switcher.SwitcherC
import com.toddler.footsteps.R
import com.toddler.footsteps.Screens
import com.toddler.footsteps.chat.ChatViewModel
import com.toddler.footsteps.databinding.FragmentAssessBinding
import java.sql.Timestamp
import kotlin.properties.Delegates

enum class ShowHide {
    HIDE,
    SHOW
}
class AssessFragment: Fragment() {



    private lateinit var assessViewModel: AssessViewModel
    private lateinit var statsViewModel: StatsViewModel
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var binding: FragmentAssessBinding

    private lateinit var reqTextArray: List<MutableLiveData<Boolean>>
    private lateinit var reqIndicatorFunArray: List<(Boolean)->Unit>
    private lateinit var reqIndicatorArray: List<MutableLiveData<Boolean>>
    private lateinit var reqSwitchArray: List<SwitcherC>

    private var startTime by Delegates.notNull<Long>()
    private var endTime by Delegates.notNull<Long>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAssessBinding.inflate(inflater, container, false)
        assessViewModel = ViewModelProvider(requireActivity())[AssessViewModel::class.java]
        statsViewModel = ViewModelProvider(requireActivity())[StatsViewModel::class.java]
        chatViewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]
        val fragmentManager = requireActivity().supportFragmentManager

        // Get the root view of the fragment
        val rootView = binding.root

        startTime = System.currentTimeMillis()
        endTime = startTime + 60000

        // Disable touch events on the root view to prevent passing through to the activity's views
        rootView.isClickable = true
        rootView.isFocusableInTouchMode = true
        rootView.setOnTouchListener { _, _ -> true }


        // Set the lifecycle owner to this Fragment
        // This is needed so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner // Set the lifecycle owner to this Fragment
        binding.viewModel = assessViewModel

        // go back to the previous fragment upon clicking the back button on the toolbar
        binding.toolbar.setNavigationOnClickListener {
//            requireActivity().onBackPressed()
            fragmentManager.popBackStack()
        }

        reqTextArray = listOf(
            assessViewModel.req1Text,
            assessViewModel.req2Text,
            assessViewModel.req3Text,
            assessViewModel.req4Text
            )

        reqSwitchArray = listOf(
            binding.requirement1Switch,
            binding.requirement2Switch,
            binding.requirement3Switch,
            binding.requirement4Switch
        )

        reqIndicatorFunArray = listOf(
            { value -> assessViewModel.setReq1(value) },
            { value -> assessViewModel.setReq2(value) },
            { value -> assessViewModel.setReq3(value) },
            { value -> assessViewModel.setReq4(value) }
        )

        reqIndicatorArray = listOf(
            assessViewModel.req1,
            assessViewModel.req2,
            assessViewModel.req3,
            assessViewModel.req4
        )

        for(req_text in reqTextArray) {
            req_text.observe(viewLifecycleOwner) {
                if(req_text.value == true) {
                    reqSwitchArray[reqTextArray.indexOf(req_text)].setChecked(true)
                } else {
                    reqSwitchArray[reqTextArray.indexOf(req_text)].setChecked(false)
                }
            }
        }

        for(switch in reqSwitchArray) {
            switch.setOnCheckedChangeListener { isChecked ->
                reqIndicatorFunArray[reqSwitchArray.indexOf(switch)](isChecked)
//                Log.i("switch", "switch ${req_indicator_array[req_switch_array.indexOf(switch)].value} is $isChecked")
//                req_indicator_array[req_switch_array.indexOf(switch)](isChecked)

                assessViewModel.checkAllRequirements()
            }
        }

        val timerView = binding.timerContainer
        val requirementsContainer = binding.requirementsContainer
        val instructionsContainer = binding.instructionsContainer
        assessViewModel.allRequirementsMet.observe(viewLifecycleOwner) {
            if(it == true) {
                handleVisibility(timerView, ShowHide.SHOW, 1000,0f, 0f, -2f, 0f)
                handleVisibility(requirementsContainer, ShowHide.HIDE, 500, 0f, -1f, 0f, 0f)
                handleVisibility(instructionsContainer, ShowHide.SHOW, 500,1f, 0f, 0f, 0f)
            } else {
                handleVisibility(timerView, ShowHide.HIDE, 1000, 0f, 0f, 0f, -2f)
                handleVisibility(requirementsContainer, ShowHide.SHOW, 500,-1f, 0f, 0f, 0f)
                handleVisibility(instructionsContainer, ShowHide.HIDE, 500,0f, 1f, 0f, 0f)
            }
        }

        binding.instructionsBack.setOnClickListener() {
            handleVisibility(timerView, ShowHide.HIDE, 1000,0f, 0f, 0f, -2f)
            handleVisibility(requirementsContainer, ShowHide.SHOW, 500,-1f, 0f, 0f, 0f)
            handleVisibility(instructionsContainer, ShowHide.HIDE, 500,0f, 1f, 0f, 0f)

            for(switch in reqSwitchArray) {
                switch.setChecked(false)
            }
        }

        binding.imageAddButton.setOnClickListener() {
            if(binding.timerTextField.text.toString() == "") {
                binding.timerTextField.setText("0")
            }
            assessViewModel.setCounter(binding.timerTextField.text.toString().toInt())
            assessViewModel.incrementCounter()
        }

        // if pressed for 2 seconds, incerement by 10
       // ToDo: make this work

        binding.imageRemoveButton.setOnClickListener() {
            assessViewModel.decrementCounter()
        }

        binding.startTimerButton.setOnClickListener() {
            // get the timestamp of this moment
            assessViewModel.setStoreData(true)
            startTime = Timestamp(System.currentTimeMillis()).time

            // get the time writen in the text field and set it as the timer
            if(binding.timerTextField.text.toString() == "") {
                binding.timerTextField.setText("0")
                Toast.makeText(requireContext(), "Please enter a time", Toast.LENGTH_SHORT).show()
            } else if(binding.timerTextField.text.toString().toInt() == 0) {
                Toast.makeText(requireContext(), "Please enter a time greater than 0", Toast.LENGTH_SHORT).show()
            } else if(binding.timerTextField.text.toString().toInt() < 10){
                Toast.makeText(requireContext(), "Please enter a time greater than 10", Toast.LENGTH_SHORT).show()
            }
            else if(binding.timerTextField.text.toString().toInt() > 60) {
                Toast.makeText(requireContext(), "Please enter a time less than 60", Toast.LENGTH_SHORT).show()
            } else {
                assessViewModel.setCounter(binding.timerTextField.text.toString().toInt())

                assessViewModel.startTimer()
                binding.stopTimerButton.visibility = View.VISIBLE
                binding.pauseTimerButton.visibility = View.VISIBLE
                it.isEnabled = false
            }
        }


        assessViewModel.counterTextInt.observe(viewLifecycleOwner) {
            val progressBar = binding.progressBar
            progressBar.max = assessViewModel.counter.value!!
            progressBar.progress = it
//            // set an animation for the progress bar for each tick
//            val animation = ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, it)
//            animation.duration = 500
//            animation.interpolator = LinearInterpolator()
//            animation.start()


//            animation.addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationEnd(animation: Animator?) {
//                    super.onAnimationEnd(animation)
//                    progressBar.progress = it
//
////                    if(assessViewModel.counterTextInt.value == assessViewModel.counter.value) {
////                        assessViewModel.timerFinished()
////                    }
//                }
//            })
        }

        binding.stopTimerButton.setOnClickListener() {
            assessViewModel.setStoreData(false)
            endTime = Timestamp(System.currentTimeMillis()).time
            assessViewModel.stopTimer()
            binding.stopTimerButton.visibility = View.GONE
            binding.pauseTimerButton.visibility = View.GONE
            binding.startTimerButton.isEnabled = true
        }

        binding.pauseTimerButton.setOnClickListener() {
            assessViewModel.pauseTimer()
        }

        binding.exportDataButton.setOnClickListener() {
            assessViewModel.exportData(startTime, endTime)
        }

        assessViewModel.timerFinished.observe(viewLifecycleOwner) {

            if(it) {
                endTime = Timestamp(System.currentTimeMillis()).time
                assessViewModel.stopTimer()
                binding.stopTimerButton.visibility = View.GONE
                binding.pauseTimerButton.visibility = View.GONE
                binding.startTimerButton.isEnabled = true

                handleVisibility(binding.exportDataButton, ShowHide.SHOW, 500, 0f, 0f, -1f, 0f)
                handleVisibility(binding.assessMovementButton, ShowHide.SHOW, 500, 0f, 0f, -1f, 0f)
            } else {
                handleVisibility(binding.exportDataButton, ShowHide.HIDE, 500, 0f, 0f, 0f, -1f)
                handleVisibility(binding.assessMovementButton, ShowHide.HIDE, 500, 0f, 0f, 0f, -1f)
            }
        }

        binding.assessMovementButton.setOnClickListener() {
            binding.textView2.visibility = View.VISIBLE

            // indicate the AI model concluded that the movement is normal

            binding.textView2.text = "Movement is normal"
        }


//        binding.assessMovementButton.set
        return rootView
    }


    private fun handleVisibility(view: View, showHide: ShowHide, duration: Long, fromX: Float, toX: Float, fromY: Float, toY: Float) {
        var _fromY = fromY
        var _toY = toY
        var _fromX = fromX
        var _toX = toX
        var viewVisibility = View.GONE

        if(showHide == ShowHide.SHOW) {
            viewVisibility = View.VISIBLE
            view.visibility = viewVisibility

        } else if(showHide == ShowHide.HIDE) {
            viewVisibility = View.GONE
        }

        // Create a TranslateAnimation to animate the view's position
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, _fromX,
            Animation.RELATIVE_TO_SELF, _toX,
            Animation.RELATIVE_TO_SELF, _fromY,
            Animation.RELATIVE_TO_SELF, _toY)

        // Set the duration and interpolator for the animation
        animation.duration = duration
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


    // another function to handle visibility with fading instead of translating
    private fun handleVisibilityWithFade(view: View, showHide: ShowHide, duration: Long, fromAlpha: Float, toAlpha: Float) {
        var _fromAlpha = fromAlpha
        var _toAlpha = toAlpha
        var viewVisibility = View.GONE

        if(showHide == ShowHide.SHOW) {
            viewVisibility = View.VISIBLE
            view.visibility = viewVisibility

        } else if(showHide == ShowHide.HIDE) {
            viewVisibility = View.GONE
        }

        // Create a TranslateAnimation to animate the view's position
        val animation = AlphaAnimation(_fromAlpha, _toAlpha)

        // Set the duration and interpolator for the animation
        animation.duration = duration
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


    override fun onStop() {
        showHideBottomNavBar(ShowHide.SHOW)
        super.onStop()
    }

    override fun onPause() {
        assessViewModel.setStoreData(false)
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