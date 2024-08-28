package io.github.thwisse.disabletheego

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.thwisse.disabletheego.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        enableEdgeToEdge()

        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navController = getNavController()

        // Geri tuşuna basıldığında NavController'ı kullanarak geri gidin
        onBackPressedDispatcher.addCallback(this) {
            if (!navController.popBackStack()) {
                finish()
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val currentFragmentId = navController.currentDestination?.id

            when (item.itemId) {

                R.id.happinessFragment -> {
                    if (currentFragmentId != R.id.happinessFragment) {
                        navController.navigate(R.id.happinessFragment)
                    }
                    true
                }
                R.id.optimismFragment -> {
                    if (currentFragmentId != R.id.optimismFragment) {
                        navController.navigate(R.id.optimismFragment)
                    }
                    true
                }
                R.id.kindnessFragment -> {
                    if (currentFragmentId != R.id.kindnessFragment) {
                        navController.navigate(R.id.kindnessFragment)
                    }
                    true
                }
                R.id.givingFragment -> {
                    if (currentFragmentId != R.id.givingFragment) {
                        navController.navigate(R.id.givingFragment)
                    }
                    true
                }
                R.id.respectFragment -> {
                    if (currentFragmentId != R.id.respectFragment) {
                        navController.navigate(R.id.respectFragment)
                    }
                    true
                }
                R.id.dashboardFragment -> {
                    // Eğer dashboard'a geri dönmek istiyorsak, popBackStack ile stack'teki mevcut dashboard'u geri getirin
                    navController.popBackStack(R.id.dashboardFragment, false)
                    true
                }
                else -> false
            }
        }


    }

    fun getBottomNavigationView(): BottomNavigationView {
        return binding.bottomNavigationView
    }

    fun getNavController(): NavController {
        val navHostFragmentView =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentView) as NavHostFragment

        return navHostFragmentView.navController
    }
}