package io.github.thwisse.disabletheego

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.materialswitch.MaterialSwitch
import io.github.thwisse.disabletheego.databinding.FragmentMainBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
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
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swEgo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                disableOtherSwitches()
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

    private fun preventSwitchActivationIfEgoIsOn(switch: MaterialSwitch) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            // sw ego checked iken ayni anda secilen switch de checked olursa:
            if (binding.swEgo.isChecked && isChecked) {
                switch.isChecked = false
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
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}