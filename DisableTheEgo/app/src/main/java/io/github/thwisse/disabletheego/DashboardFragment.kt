package io.github.thwisse.disabletheego

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.materialswitch.MaterialSwitch
import io.github.thwisse.disabletheego.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    // Aktif menü öğelerini ve switch durumlarını saklayan listeler
    private lateinit var activeMenuItems: MutableList<Int>
    private lateinit var switchStates: MutableMap<Int, Boolean>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = requireActivity() as MainActivity
        val bottomNavigationView = mainActivity.getBottomNavigationView()

        // Switch durumları ve aktif menü öğeleri başlatılır
        activeMenuItems = mutableListOf(R.id.dashboardFragment)
        switchStates = mutableMapOf(
            R.id.happinessFragment to false,
            R.id.optimismFragment to false,
            R.id.kindnessFragment to false,
            R.id.givingFragment to false,
            R.id.respectFragment to false
        )

        // Dashboard'a geri dönüldüğünde switch'leri ve menü öğelerini güncelle
        updateSwitchesAndMenuItems(bottomNavigationView)

        // Ego switch'i dinleyicisi
        binding.swEgo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                disableOtherSwitches()
                bottomNavigationView.visibility = View.INVISIBLE
                // Menüde Dashboard dışındaki tüm öğeleri kaldır
                removeAllNonDashboardItems(bottomNavigationView)
            }
        }

        // Diğer switch'ler için dinleyiciler eklenir
        setSwitchListener(binding.swHappiness, R.id.happinessFragment, bottomNavigationView)
        setSwitchListener(binding.swOptimism, R.id.optimismFragment, bottomNavigationView)
        setSwitchListener(binding.swKindness, R.id.kindnessFragment, bottomNavigationView)
        setSwitchListener(binding.swGiving, R.id.givingFragment, bottomNavigationView)
        setSwitchListener(binding.swRespect, R.id.respectFragment, bottomNavigationView)
    }

    private fun updateSwitchesAndMenuItems(bottomNavigationView: BottomNavigationView) {
        // Dashboard dışındaki mevcut menü öğelerine göre switch'leri güncelle
        val menu = bottomNavigationView.menu

        menu.findItem(R.id.happinessFragment)?.let {
            switchStates[R.id.happinessFragment] = true
            if (!activeMenuItems.contains(R.id.happinessFragment)) {
                activeMenuItems.add(R.id.happinessFragment)
            }
        }

        menu.findItem(R.id.optimismFragment)?.let {
            switchStates[R.id.optimismFragment] = true
            if (!activeMenuItems.contains(R.id.optimismFragment)) {
                activeMenuItems.add(R.id.optimismFragment)
            }
        }

        menu.findItem(R.id.kindnessFragment)?.let {
            switchStates[R.id.kindnessFragment] = true
            if (!activeMenuItems.contains(R.id.kindnessFragment)) {
                activeMenuItems.add(R.id.kindnessFragment)
            }
        }

        menu.findItem(R.id.givingFragment)?.let {
            switchStates[R.id.givingFragment] = true
            if (!activeMenuItems.contains(R.id.givingFragment)) {
                activeMenuItems.add(R.id.givingFragment)
            }
        }

        menu.findItem(R.id.respectFragment)?.let {
            switchStates[R.id.respectFragment] = true
            if (!activeMenuItems.contains(R.id.respectFragment)) {
                activeMenuItems.add(R.id.respectFragment)
            }
        }

        // Switch'lerin durumunu güncelle
        updateSwitches()

        // Ego switch'in durumunu kontrol et ve güncelle
        binding.swEgo.isChecked = menu.size() <= 1
    }

    private fun updateSwitches() {
        binding.swHappiness.isChecked = switchStates[R.id.happinessFragment] == true
        binding.swOptimism.isChecked = switchStates[R.id.optimismFragment] == true
        binding.swKindness.isChecked = switchStates[R.id.kindnessFragment] == true
        binding.swGiving.isChecked = switchStates[R.id.givingFragment] == true
        binding.swRespect.isChecked = switchStates[R.id.respectFragment] == true
    }

    private fun disableOtherSwitches() {
        binding.swGiving.isChecked = false
        binding.swRespect.isChecked = false
        binding.swKindness.isChecked = false
        binding.swHappiness.isChecked = false
        binding.swOptimism.isChecked = false
    }

    private fun setSwitchListener(
        switch: MaterialSwitch, menuItemId: Int, bottomNavigationView: BottomNavigationView) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (binding.swEgo.isChecked) {
                switch.isChecked = false
                return@setOnCheckedChangeListener
            } else {
                bottomNavigationView.visibility = View.VISIBLE
            }

            if (isChecked) {
                if (activeMenuItems.size >= 5) {
                    switch.isChecked = false
                } else {
                    addMenuItem(menuItemId, bottomNavigationView)
                }
            } else {
                removeMenuItem(menuItemId, bottomNavigationView)
            }

            // Switch durumunu güncelle
            switchStates[menuItemId] = isChecked
        }
    }

    private fun removeMenuItem(menuItemId: Int, bottomNavigationView: BottomNavigationView) {
        if (activeMenuItems.contains(menuItemId)) {
            activeMenuItems.remove(menuItemId)
            rearrangeMenuItems(bottomNavigationView)
        }
    }

    private fun rearrangeMenuItems(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.menu.clear()

        bottomNavigationView.menu.add(Menu.NONE, R.id.dashboardFragment, Menu.NONE, "Dashboard")
            .setIcon(R.drawable.dashboard_icon)

        activeMenuItems.forEach { id ->
            if (id != R.id.dashboardFragment) {
                bottomNavigationView.menu.add(Menu.NONE, id, Menu.NONE, getMenuItemTitle(id))
                    .setIcon(getMenuItemIcon(id))
            }
        }
    }

    private fun removeAllNonDashboardItems(bottomNavigationView: BottomNavigationView) {
        activeMenuItems.removeAll { it != R.id.dashboardFragment }
        rearrangeMenuItems(bottomNavigationView)
    }

    private fun getMenuItemTitle(menuItemId: Int): String {
        return when (menuItemId) {
            R.id.happinessFragment -> "Happiness"
            R.id.optimismFragment -> "Optimism"
            R.id.kindnessFragment -> "Kindness"
            R.id.givingFragment -> "Giving"
            R.id.respectFragment -> "Respect"
            else -> ""
        }
    }

    private fun getMenuItemIcon(menuItemId: Int): Int {
        return when (menuItemId) {
            R.id.happinessFragment -> R.drawable.happiness_icon
            R.id.optimismFragment -> R.drawable.optimism_icon
            R.id.kindnessFragment -> R.drawable.kindness_icon
            R.id.givingFragment -> R.drawable.giving_icon
            R.id.respectFragment -> R.drawable.respect_icon
            else -> 0
        }
    }

    private fun addMenuItem(menuItemId: Int, bottomNavigationView: BottomNavigationView) {
        if (!activeMenuItems.contains(menuItemId)) {
            activeMenuItems.add(menuItemId)
            rearrangeMenuItems(bottomNavigationView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
