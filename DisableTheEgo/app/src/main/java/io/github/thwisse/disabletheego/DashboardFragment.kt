package io.github.thwisse.disabletheego

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.materialswitch.MaterialSwitch
import io.github.thwisse.disabletheego.databinding.FragmentDashboardBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = requireActivity() as MainActivity

        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        binding.swEgo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                disableOtherSwitches()
                mainActivity.getBottomNavigationView().visibility = View.GONE
                bottomNavigationView.menu.findItem(R.id.happinessFragment).isVisible = false
                bottomNavigationView.menu.findItem(R.id.optimismFragment).isVisible = false
                bottomNavigationView.menu.findItem(R.id.givingFragment).isVisible = false
                bottomNavigationView.menu.findItem(R.id.kindnessFragment).isVisible = false
                bottomNavigationView.menu.findItem(R.id.respectFragment).isVisible = false
            }
        }

        preventSwitchActivationIfEgoIsOn(binding.swHappiness)
        preventSwitchActivationIfEgoIsOn(binding.swGiving)
        preventSwitchActivationIfEgoIsOn(binding.swRespect)
        preventSwitchActivationIfEgoIsOn(binding.swKindness)
        preventSwitchActivationIfEgoIsOn(binding.swOptimism)
    }

    private fun disableOtherSwitches() {
        binding.swGiving.isChecked = false
        binding.swRespect.isChecked = false
        binding.swKindness.isChecked = false
        binding.swHappiness.isChecked = false
        binding.swOptimism.isChecked = false
    }

//    private fun invisibleItems(switch: MaterialSwitch) {
//
//        val bottomNavigationView =
//            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//
//        switch.setOnCheckedChangeListener { _, isChecked ->
//            if (switch != binding.swEgo) {
//                bottomNavigationView.menu.findItem(R.id.happinessFragment).isVisible = false
//                bottomNavigationView.menu.findItem(R.id.optimismFragment).isVisible = false
//                bottomNavigationView.menu.findItem(R.id.givingFragment).isVisible = false
//                bottomNavigationView.menu.findItem(R.id.kindnessFragment).isVisible = false
//                bottomNavigationView.menu.findItem(R.id.respectFragment).isVisible = false
//            }
//        }
//    }

    private fun preventSwitchActivationIfEgoIsOn(switch: MaterialSwitch) {

        val mainActivity = requireActivity() as MainActivity

        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        //val menu = bottomNavigationView.menu

        switch.setOnCheckedChangeListener { _, isChecked ->
            // sw ego checked iken ayni anda secilen switch de checked olursa:
            if (binding.swEgo.isChecked && isChecked) {
                switch.isChecked = false
            }
            if (!binding.swEgo.isChecked && isChecked) {
                mainActivity.getBottomNavigationView().visibility = View.VISIBLE
                var fragmentName: Int? = null
                when {
                    switch.id == R.id.swHappiness -> { fragmentName = R.id.happinessFragment }
                    switch.id == R.id.swGiving -> { fragmentName = R.id.givingFragment }
                    switch.id == R.id.swRespect -> { fragmentName = R.id.respectFragment }
                    switch.id == R.id.swKindness -> { fragmentName = R.id.kindnessFragment }
                    switch.id == R.id.swOptimism -> { fragmentName = R.id.optimismFragment }
                    else -> { }
                }
                val menuItem = fragmentName?.let { bottomNavigationView.menu.findItem(it) }
                if (menuItem != null) {
                    menuItem.isVisible = true
                }
            }
            if (!binding.swEgo.isChecked && !isChecked) {
                mainActivity.getBottomNavigationView().visibility = View.VISIBLE
                var fragmentName: Int? = null
                when {
                    switch.id == R.id.swHappiness -> { fragmentName = R.id.happinessFragment }
                    switch.id == R.id.swGiving -> { fragmentName = R.id.givingFragment }
                    switch.id == R.id.swRespect -> { fragmentName = R.id.respectFragment }
                    switch.id == R.id.swKindness -> { fragmentName = R.id.kindnessFragment }
                    switch.id == R.id.swOptimism -> { fragmentName = R.id.optimismFragment }
                    else -> { }
                }
                val menuItem = fragmentName?.let { bottomNavigationView.menu.findItem(it) }
                if (menuItem != null) {
                    menuItem.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}