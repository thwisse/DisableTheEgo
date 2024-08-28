package io.github.thwisse.disabletheego

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.materialswitch.MaterialSwitch
import io.github.thwisse.disabletheego.databinding.FragmentDashboardBinding

// DashboardFragment sınıfı, bir Fragment'tir. Bu fragment içinde bir BottomNavigationView
// ve birkaç Switch kontrolü bulunur.
class DashboardFragment : Fragment() {

    // Binding referansı, XML dosyasında tanımlanan görsel bileşenlere erişim sağlar.
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    // Aktif menü öğelerinin ID'lerini saklayan bir liste.
    // Dashboard menü öğesi başlangıçta bu listede yer alır.
    private lateinit var activeMenuItems: MutableList<Int>

    // onCreateView, Fragment'ın arayüzünü oluşturur ve binding'i başlatır.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // FragmentDashboardBinding sınıfından binding nesnesi oluşturulur.
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root // Arayüzün kök view'i döndürülür.
    }

    // onViewCreated, View oluşturulduktan sonra çağrılır. Bu metot, view'larla ilgili işlemleri yapar.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            binding.swHappiness.isChecked = it.getBoolean("swHappiness", false)
            binding.swOptimism.isChecked = it.getBoolean("swOptimism", false)
            binding.swKindness.isChecked = it.getBoolean("swKindness", false)
            binding.swGiving.isChecked = it.getBoolean("swGiving", false)
            binding.swRespect.isChecked = it.getBoolean("swRespect", false)
            binding.swEgo.isChecked = it.getBoolean("swEgo", true) // Varsayılan olarak açık
        }

        // MainActivity referansı alınır.
        val mainActivity = requireActivity() as MainActivity

        // BottomNavigationView, MainActivity'den alınır.
        val bottomNavigationView = mainActivity.getBottomNavigationView()
        // Dashboard menü öğesi başlangıçta aktif menü öğeleri listesine eklenir.
        activeMenuItems = mutableListOf(R.id.dashboardFragment)

        // Ego Switch'in durumuna göre BottomNavigationView'in görünürlüğünü ayarlayın.
        if (binding.swEgo.isChecked) {
            bottomNavigationView.visibility = View.INVISIBLE
        } else {
            bottomNavigationView.visibility = View.VISIBLE
        }

        // Eğer BottomNavigationView daha önce görünür yapıldıysa, tekrar invisible yapma.
        if (!binding.swEgo.isChecked && bottomNavigationView.visibility != View.VISIBLE) {
            bottomNavigationView.visibility = View.VISIBLE
        }

        // Diğer Switch'lerin durumlarını kontrol ederek BottomNavigationView'e ekleyin.
        updateBottomNavigationItems(bottomNavigationView)

        // Ego switch'i dinleyicisi. Ego açıldığında diğer switch'leri kapatır ve menüyü temizler.
        binding.swEgo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Diğer switch'leri kapat
                disableOtherSwitches()
                // BottomNavigationView'i görünmez yap
                //mainActivity.getBottomNavigationView().visibility = View.INVISIBLE

                // Menü öğelerini temizle ama Dashboard'u menüde tut
                bottomNavigationView.menu.clear() // Menü öğeleri temizlenir.
                activeMenuItems.clear() // Aktif menü öğeleri listesi temizlenir.
                activeMenuItems.add(R.id.dashboardFragment) // Dashboard menü öğesi tekrar eklenir.
                bottomNavigationView.menu.add(Menu.NONE, R.id.dashboardFragment, Menu.NONE, "Dashboard")
                    .setIcon(R.drawable.dashboard_icon) // Dashboard menü öğesi menüye eklenir.
            } else {
                //mainActivity.getBottomNavigationView().visibility = View.INVISIBLE
                // Ego switch'i kapatıldığında Dashboard'un menüde olduğundan emin olun
                if (!activeMenuItems.contains(R.id.dashboardFragment)) {
                    activeMenuItems.add(R.id.dashboardFragment) // Dashboard tekrar aktif menü öğelerine eklenir.
                    bottomNavigationView.menu.add(Menu.NONE, R.id.dashboardFragment, Menu.NONE, "Dashboard")
                        .setIcon(R.drawable.dashboard_icon) // Dashboard menü öğesi menüye eklenir.
                }
            }
        }

        // Diğer switch'ler için dinleyiciler eklenir.
        setSwitchListener(binding.swHappiness, R.id.happinessFragment, bottomNavigationView)
        setSwitchListener(binding.swOptimism, R.id.optimismFragment, bottomNavigationView)
        setSwitchListener(binding.swKindness, R.id.kindnessFragment, bottomNavigationView)
        setSwitchListener(binding.swGiving, R.id.givingFragment, bottomNavigationView)
        setSwitchListener(binding.swRespect, R.id.respectFragment, bottomNavigationView)
    }

    private fun updateBottomNavigationItems(bottomNavigationView: BottomNavigationView) {
        // Mevcut Switch durumlarını kontrol edin ve BottomNavigationView'e gerekli item'ları ekleyin.
        if (binding.swHappiness.isChecked) {
            addMenuItem(R.id.happinessFragment, bottomNavigationView)
        }
        if (binding.swOptimism.isChecked) {
            addMenuItem(R.id.optimismFragment, bottomNavigationView)
        }
        if (binding.swKindness.isChecked) {
            addMenuItem(R.id.kindnessFragment, bottomNavigationView)
        }
        if (binding.swGiving.isChecked) {
            addMenuItem(R.id.givingFragment, bottomNavigationView)
        }
        if (binding.swRespect.isChecked) {
            addMenuItem(R.id.respectFragment, bottomNavigationView)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("swHappiness", binding.swHappiness.isChecked)
        outState.putBoolean("swOptimism", binding.swOptimism.isChecked)
        outState.putBoolean("swKindness", binding.swKindness.isChecked)
        outState.putBoolean("swGiving", binding.swGiving.isChecked)
        outState.putBoolean("swRespect", binding.swRespect.isChecked)
        outState.putBoolean("swEgo", binding.swEgo.isChecked)
    }

    // disableOtherSwitches fonksiyonu, Ego switch'i açıldığında diğer switch'leri kapatır.
    private fun disableOtherSwitches() {
        binding.swGiving.isChecked = false
        binding.swRespect.isChecked = false
        binding.swKindness.isChecked = false
        binding.swHappiness.isChecked = false
        binding.swOptimism.isChecked = false
    }

    // setSwitchListener fonksiyonu, her bir switch için bir dinleyici ekler.
    private fun setSwitchListener(
        switch: MaterialSwitch, menuItemId: Int, bottomNavigationView: BottomNavigationView) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (binding.swEgo.isChecked) {
                // Eğer Ego switch'i açıksa, diğer switch'lerin açılmasına izin verme
                switch.isChecked = false
            } else if (isChecked) {
                // Switch açıldığında menü öğesi eklenir
                bottomNavigationView.visibility = View.VISIBLE
                addMenuItem(menuItemId, bottomNavigationView)
            } else {
                // Switch kapatıldığında menü öğesi kaldırılır
                removeMenuItem(menuItemId, bottomNavigationView)
            }
        }
    }

    // removeMenuItem fonksiyonu, menü öğesini listeden ve BottomNavigationView'den kaldırır.
    private fun removeMenuItem(menuItemId: Int, bottomNavigationView: BottomNavigationView) {
        activeMenuItems.remove(menuItemId) // Menü öğesi aktif menü listesinden kaldırılır.
        rearrangeMenuItems(bottomNavigationView) // Menü öğeleri yeniden sıralanır.
    }

    // rearrangeMenuItems fonksiyonu, menü öğelerini aktif menü öğelerinin sırasına göre yeniden düzenler.
    private fun rearrangeMenuItems(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.menu.clear() // Mevcut menü temizlenir.

        // Dashboard her zaman en başta olur
        bottomNavigationView.menu.add(Menu.NONE, R.id.dashboardFragment, Menu.NONE, "Dashboard")
            .setIcon(R.drawable.dashboard_icon) // Dashboard menü öğesi menüye eklenir.

        // Diğer aktif öğeleri ekle (basılma sırasına göre)
        activeMenuItems.forEachIndexed { index, id ->
            if (index != 0) { // 0 indexi Dashboard olduğu için atlıyoruz
                bottomNavigationView.menu.add(Menu.NONE, id, Menu.NONE, getMenuItemTitle(id))
                    .setIcon(getMenuItemIcon(id))
            }
        }
    }

    // getMenuItemTitle fonksiyonu, menü öğesi için başlık döner.
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

    // getMenuItemIcon fonksiyonu, menü öğesi için ikon döner.
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

    // addMenuItem fonksiyonu, menü öğesini aktif menü listesine ve BottomNavigationView'e ekler.
    private fun addMenuItem(menuItemId: Int, bottomNavigationView: BottomNavigationView) {
        if (!activeMenuItems.contains(menuItemId) && activeMenuItems.size < 5) {
            activeMenuItems.add(menuItemId) // Menü öğesi aktif menü listesine eklenir.
            rearrangeMenuItems(bottomNavigationView) // Menü öğeleri yeniden düzenlenir.
        }
    }

    // onDestroyView fonksiyonu, view yok edilmeden önce binding'i temizler.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
