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
    private lateinit var activeMenuItems: MutableList<Int>

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
        activeMenuItems = mutableListOf(R.id.dashboardFragment) // Dashboard öğesi sabit olarak eklenecek

        binding.swEgo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                disableOtherSwitches()
                mainActivity.getBottomNavigationView().visibility = View.INVISIBLE

                // Menü öğelerini temizle ama Dashboard'u menüde tut
                bottomNavigationView.menu.clear()
                activeMenuItems.clear()
                activeMenuItems.add(R.id.dashboardFragment)
                bottomNavigationView.menu.add(Menu.NONE, R.id.dashboardFragment, Menu.NONE, "Dashboard")
                    .setIcon(R.drawable.dashboard_icon)
            } else {
                // Ego kapatıldığında Dashboard öğesi her zaman menüde kalsın
                if (!activeMenuItems.contains(R.id.dashboardFragment)) {
                    activeMenuItems.add(R.id.dashboardFragment)
                    bottomNavigationView.menu.add(Menu.NONE, R.id.dashboardFragment, Menu.NONE, "Dashboard")
                        .setIcon(R.drawable.dashboard_icon)
                }
            }
        }

        // Diğer switch'ler için dinleyiciler ekleyin
        setSwitchListener(binding.swHappiness, R.id.happinessFragment, bottomNavigationView)
        setSwitchListener(binding.swOptimism, R.id.optimismFragment, bottomNavigationView)
        setSwitchListener(binding.swKindness, R.id.kindnessFragment, bottomNavigationView)
        setSwitchListener(binding.swGiving, R.id.givingFragment, bottomNavigationView)
        setSwitchListener(binding.swRespect, R.id.respectFragment, bottomNavigationView)
    }

    private fun disableOtherSwitches() {
        binding.swGiving.isChecked = false
        binding.swRespect.isChecked = false
        binding.swKindness.isChecked = false
        binding.swHappiness.isChecked = false
        binding.swOptimism.isChecked = false
    }

    private fun setSwitchListener(switch: MaterialSwitch, menuItemId: Int, bottomNavigationView: BottomNavigationView) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (binding.swEgo.isChecked) {
                // Eğer Ego switch'i açık ise, diğer switch'lerin açılmasına izin vermeyin
                switch.isChecked = false
            } else if (isChecked) {
                bottomNavigationView.visibility = View.VISIBLE
                addMenuItem(menuItemId, bottomNavigationView)
            } else {
                removeMenuItem(menuItemId, bottomNavigationView)
            }
        }
    }

    private fun removeMenuItem(menuItemId: Int, bottomNavigationView: BottomNavigationView) {
        activeMenuItems.remove(menuItemId)
        rearrangeMenuItems(bottomNavigationView) // Menü öğelerini yeniden düzenle
    }

    private fun rearrangeMenuItems(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.menu.clear()

        // Dashboard her zaman en başta olacak
        bottomNavigationView.menu.add(Menu.NONE, R.id.dashboardFragment, Menu.NONE, "Dashboard")
            .setIcon(R.drawable.dashboard_icon)

        // Diğer öğeleri sabit sıraya göre ekle
        val allItemsInOrder = listOf(
            R.id.happinessFragment,
            R.id.optimismFragment,
            R.id.kindnessFragment,
            R.id.givingFragment,
            R.id.respectFragment
        )

        allItemsInOrder.forEach { id ->
            if (activeMenuItems.contains(id)) {
                bottomNavigationView.menu.add(Menu.NONE, id, Menu.NONE, getMenuItemTitle(id))
                    .setIcon(getMenuItemIcon(id))
            }
        }
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
        if (!activeMenuItems.contains(menuItemId) && activeMenuItems.size < 5) {
            activeMenuItems.add(menuItemId)
            rearrangeMenuItems(bottomNavigationView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
