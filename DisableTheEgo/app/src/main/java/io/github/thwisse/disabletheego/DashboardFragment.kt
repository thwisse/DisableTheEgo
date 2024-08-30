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

    // Binding nesnesi, XML layout'unun bileşenlerine erişimi sağlar
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    // Aktif menü öğelerinin ID'lerini tutmak için bir liste. Bu liste, menüdeki öğelerin sırasını da belirler.
    private lateinit var activeMenuItems: MutableList<Int>

    // Her bir switch'in durumunu (açık veya kapalı) tutan bir harita (Map).
    // Bu haritada switch'lerin ID'leri anahtar, durumları ise değer olarak tutulur.
    private lateinit var switchStates: MutableMap<Int, Boolean>

    // 5. switch menüde yer bulamadığında, bu öğeyi hafızada tutarız.
    private var pendingMenuItem: Int? = null

    // onCreateView, Fragment'ın arayüzünü oluşturur ve binding nesnesini başlatır.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // FragmentDashboardBinding sınıfından bir binding nesnesi oluşturulur ve XML layout'una bağlanır.
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root // Fragment'ın kök View'ı döndürülür.
    }

    // onViewCreated, View oluşturulduktan sonra çağrılır ve view ile ilgili işlemleri yapmak için kullanılır.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // MainActivity referansı alınır.
        val mainActivity = requireActivity() as MainActivity

        // BottomNavigationView nesnesi, MainActivity'den alınır.
        val bottomNavigationView = mainActivity.getBottomNavigationView()

        // Aktif menü öğelerini tutacak liste başlatılır ve Dashboard ID'siyle başlatılır.
        activeMenuItems = mutableListOf(R.id.dashboardFragment)

        // Switch durumlarını tutacak harita başlatılır ve tüm switch'ler için başlangıç durumu false olarak ayarlanır.
        switchStates = mutableMapOf(
            R.id.happinessFragment to false,
            R.id.optimismFragment to false,
            R.id.kindnessFragment to false,
            R.id.givingFragment to false,
            R.id.respectFragment to false
        )

        // Switch'leri ve menü öğelerini güncelleyen fonksiyon çağrılır.
        updateSwitchesAndMenuItems(bottomNavigationView)

        // Ego switch'i için bir dinleyici eklenir. Ego switch'i açıldığında, diğer switch'ler kapatılır
        // ve menüde yalnızca Dashboard kalır.
        binding.swEgo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Ego açıldığında diğer switch'ler kapatılır.
                disableOtherSwitches()
                // BottomNavigationView görünmez yapılır.
                bottomNavigationView.visibility = View.INVISIBLE
                // Dashboard dışındaki tüm menü öğeleri kaldırılır.
                removeAllNonDashboardItems(bottomNavigationView)
            }
        }

        // Diğer switch'ler için dinleyiciler eklenir.
        setSwitchListener(binding.swHappiness, R.id.happinessFragment, bottomNavigationView)
        setSwitchListener(binding.swOptimism, R.id.optimismFragment, bottomNavigationView)
        setSwitchListener(binding.swKindness, R.id.kindnessFragment, bottomNavigationView)
        setSwitchListener(binding.swGiving, R.id.givingFragment, bottomNavigationView)
        setSwitchListener(binding.swRespect, R.id.respectFragment, bottomNavigationView)
    }

    // updateSwitchesAndMenuItems, switch durumlarını ve menü öğelerini günceller.
    // Menünün o anki durumu, switch'lerin durumlarını belirler ve varsa pendingMenuItem'ı menüye ekler.
    private fun updateSwitchesAndMenuItems(bottomNavigationView: BottomNavigationView) {
        val menu = bottomNavigationView.menu

        // Her bir menü öğesini kontrol ederek, aktif olup olmadığını ve menüde yer alıp almadığını belirleriz.
        // Eğer aktifse, switch durumu true yapılır ve activeMenuItems listesine eklenir.
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

        // Eğer pendingMenuItem varsa ve bu switch açık ise onun da menüye eklenmesi gerekiyor.
        pendingMenuItem?.let { itemId ->
            if (switchStates[itemId] == true && !activeMenuItems.contains(itemId)) {
                if (activeMenuItems.size < 5) {
                    // Eğer 5. switch açık ve aktif menüde değilse, onu menüye ekliyoruz.
                    addMenuItem(itemId, bottomNavigationView)
                    pendingMenuItem = null
                }
            }
        }

        // Switch'lerin durumunu günceller.
        updateSwitches()

        // Eğer sadece Dashboard varsa, Ego switch'i açık olmalı.
        binding.swEgo.isChecked = activeMenuItems.size <= 1
    }

    // updateSwitches, switch'lerin durumunu günceller. Her bir switch'in durumunu
    // switchStates haritasına göre ayarlar.
    private fun updateSwitches() {
        binding.swHappiness.isChecked = switchStates[R.id.happinessFragment] == true
        binding.swOptimism.isChecked = switchStates[R.id.optimismFragment] == true
        binding.swKindness.isChecked = switchStates[R.id.kindnessFragment] == true
        binding.swGiving.isChecked = switchStates[R.id.givingFragment] == true
        binding.swRespect.isChecked = switchStates[R.id.respectFragment] == true
    }

    // disableOtherSwitches, Ego switch'i açıldığında diğer switch'leri kapatır.
    private fun disableOtherSwitches() {
        // Tüm switch durumlarını kapalı hale getirir.
        switchStates.forEach { (id, _) ->
            switchStates[id] = false
        }
        // Bekleyen switch sıfırlanır.
        pendingMenuItem = null
        // Switch'lerin durumu güncellenir.
        updateSwitches()
    }

    // setSwitchListener, her bir switch için bir dinleyici ekler. Switch açıldığında menüye eklenir,
    // kapatıldığında menüden kaldırılır.
    private fun setSwitchListener(
        switch: MaterialSwitch, menuItemId: Int, bottomNavigationView: BottomNavigationView
    ) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            // Eğer Ego switch'i açıksa, diğer switch'leri açamayız.
            if (binding.swEgo.isChecked) {
                switch.isChecked = false
                return@setOnCheckedChangeListener
            } else {
                // BottomNavigationView görünür hale getirilir.
                bottomNavigationView.visibility = View.VISIBLE
            }

            // Eğer switch açıldıysa ve aktif menü öğeleri 5'ten azsa menüye eklenir,
            // aksi halde pendingMenuItem olarak tutulur.
            if (isChecked) {
                if (activeMenuItems.size >= 5) {
                    pendingMenuItem = menuItemId
                } else {
                    addMenuItem(menuItemId, bottomNavigationView)
                }
            } else {
                // Switch kapandığında, menüden kaldırılır.
                removeMenuItem(menuItemId, bottomNavigationView)
            }

            // Switch durumunu günceller.
            switchStates[menuItemId] = isChecked
        }
    }

    // removeMenuItem, menüden bir öğeyi kaldırır ve varsa pendingMenuItem'ı menüye ekler.
    private fun removeMenuItem(menuItemId: Int, bottomNavigationView: BottomNavigationView) {
        // Menü öğesi activeMenuItems listesinden kaldırılır.
        if (activeMenuItems.contains(menuItemId)) {
            activeMenuItems.remove(menuItemId)
            // Menü yeniden düzenlenir.
            rearrangeMenuItems(bottomNavigationView)

            // Eğer pendingMenuItem varsa, onu menüye ekler.
            pendingMenuItem?.let {
                if (activeMenuItems.size < 5) {
                    addMenuItem(it, bottomNavigationView)
                    pendingMenuItem = null
                }
            }
        }
    }

    // rearrangeMenuItems, aktif menü öğelerini yeniden düzenler. Menü tamamen temizlenir ve ardından
    // Dashboard her zaman ilk sırada olacak şekilde yeniden oluşturulur.
    private fun rearrangeMenuItems(bottomNavigationView: BottomNavigationView) {
        // Menü tamamen temizlenir.
        bottomNavigationView.menu.clear()

        // Dashboard her zaman ilk sırada olacak şekilde menüye eklenir.
        bottomNavigationView.menu.add(Menu.NONE, R.id.dashboardFragment, Menu.NONE, "Dashboard")
            .setIcon(R.drawable.dashboard_icon)

        // Diğer aktif menü öğeleri sırasıyla eklenir.
        activeMenuItems.forEach { id ->
            if (id != R.id.dashboardFragment) {
                bottomNavigationView.menu.add(Menu.NONE, id, Menu.NONE, getMenuItemTitle(id))
                    .setIcon(getMenuItemIcon(id))
            }
        }
    }

    // removeAllNonDashboardItems, Dashboard dışındaki tüm menü öğelerini kaldırır ve pendingMenuItem'ı sıfırlar.
    private fun removeAllNonDashboardItems(bottomNavigationView: BottomNavigationView) {
        // Dashboard dışındaki tüm menü öğeleri kaldırılır.
        activeMenuItems.removeAll { it != R.id.dashboardFragment }
        rearrangeMenuItems(bottomNavigationView)
        // Bekleyen switch sıfırlanır.
        pendingMenuItem = null
    }

    // getMenuItemTitle, menü öğesinin başlığını döner.
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

    // getMenuItemIcon, menü öğesinin ikonunu döner.
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

    // addMenuItem, menüye bir öğe ekler ve menü öğelerini yeniden düzenler.
    private fun addMenuItem(menuItemId: Int, bottomNavigationView: BottomNavigationView) {
        // Menü öğesi activeMenuItems listesine eklenir ve ardından menü yeniden düzenlenir.
        if (!activeMenuItems.contains(menuItemId)) {
            activeMenuItems.add(menuItemId)
            rearrangeMenuItems(bottomNavigationView)
        }
    }

    // Fragment view'ı yok edilmeden önce binding'i temizler.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
