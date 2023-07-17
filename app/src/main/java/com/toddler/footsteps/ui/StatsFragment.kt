package com.toddler.footsteps.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.PieChart
import com.toddler.footsteps.R
import com.toddler.footsteps.Screens
import com.toddler.footsteps.chat.ChatViewModel
import com.toddler.footsteps.databinding.FragmentStatsBinding
import kotlin.math.atan2

class StatsFragment : Fragment() {


    private lateinit var statsViewModel: StatsViewModel
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var binding: FragmentStatsBinding

    private lateinit var leftPieChart: PieChart
    private lateinit var rightPieChart: PieChart
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        statsViewModel = ViewModelProvider(requireActivity())[StatsViewModel::class.java]
        chatViewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]

        /**
         *  7ot el satreen dol 7ala2a fe wednk w battal tensahom! w battal ya COPILOT EL KALB TN2L KLAMNA
         * Set the lifecycle owner to this Fragment
         * This is needed so that the binding can observe LiveData updates
         */
        // this is needed so that the binding can observe LiveData updates
        binding.viewModel = statsViewModel
        // I'm not sure why this is needed, but without it the mapping of stepCount to the TextView doesn't work
        binding.lifecycleOwner = viewLifecycleOwner // Set the lifecycle owner to this Fragment

        val chartsFragment = ChartsFragment()
        val statsFragment = StatsFragment()
        val assessFragment = AssessFragment()
        val fragmentManager = requireActivity().supportFragmentManager

        binding.assessMovementButton.setOnClickListener {
//            chatViewModel.setScreen(Screens.ASSESS_MOVEMENT_SCREEN)
            // Replace the current content of the FrameLayout with your Fragment
            val transaction = fragmentManager.beginTransaction()
            // Specify custom animations for enter and exit transitions
            transaction.setCustomAnimations(
                R.anim.slide_in_down,   // Animation resource for enter transition
                R.anim.slide_out_down,  // Animation resource for exit transition
                R.anim.slide_in_down,   // Animation resource for pop enter transition
                R.anim.slide_out_down
            )  // Animation resource for pop exit transition

            transaction.replace(R.id.frameLayout, assessFragment, "AssessFragment")
            transaction.addToBackStack(null) // Add to back stack if you want to navigate back
            transaction.setReorderingAllowed(true)
            transaction.commit()
        }

        rightPieChart = binding.rightPieChart
        leftPieChart = binding.leftPieChart

        statsViewModel.setPieChart(rightPieChart, leftPieChart)

        statsViewModel.rightFootAngle.observe(viewLifecycleOwner) {
            statsViewModel.updateRightPieCharts(rightPieChart)
        }

        statsViewModel.leftFootAngle.observe(viewLifecycleOwner) {
            statsViewModel.updateLeftPieCharts(leftPieChart)
        }

        statsViewModel.setStepCount(5235)

//        statsViewModel.stepCount.observe(viewLifecycleOwner) {
//            Log.i("StatsFragment", "Step count: $it")
//            binding.stepCountValue.text = it.toString()
//        }

        statsViewModel.setCadence(103) // in steps per minute
        statsViewModel.setStrideLength(54.2) // in cm


        statsViewModel.accelerometerRightData.observe(viewLifecycleOwner) {
            val yaw = atan2(-it[0]/10, it[1]/10) * 180 / Math.PI
            statsViewModel.setRightFootAngle(it[1])
//            Log.i("StatsFragment", "Yaw: $yaw")
        }

        statsViewModel.accelerometerLeftData.observe(viewLifecycleOwner) {
            val yaw = atan2(-it[0]/10, -it[1]/10) * 180 / Math.PI
            statsViewModel.setLeftFootAngle(it[1])
//            Log.i("StatsFragment", "Yaw: $yaw")
        }



        // go back to the previous fragment upon clicking the back button on the toolbar
        binding.toolbar.setNavigationOnClickListener {
//            requireActivity().onBackPressed()
            fragmentManager.popBackStack()
        }

        return binding.root
    }


    override fun onStop() {
        chatViewModel.setScreen(Screens.MAIN_SCREEN)
        super.onStop()

    }

    override fun onPause() {
        chatViewModel.setScreen(Screens.NO_SCREEN)
        super.onPause()
    }

    override fun onResume() {
        chatViewModel.setScreen(Screens.STATISTICS_SCREEN)
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }

}