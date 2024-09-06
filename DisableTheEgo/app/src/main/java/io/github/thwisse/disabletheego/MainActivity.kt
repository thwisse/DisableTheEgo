package io.github.thwisse.disabletheego

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.thwisse.disabletheego.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

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

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val currentFragmentId = navController.currentDestination?.id

            val navOptions = navOptions {
                anim {
                    enter = R.anim.slide_in_left
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
            }

            when (item.itemId) {
                R.id.dashboardFragment -> {
                    if (currentFragmentId != R.id.dashboardFragment) {
                        navController.navigate(R.id.dashboardFragment, null, navOptions)
                    }
                    true
                }
                R.id.happinessFragment -> {
                    if (currentFragmentId != R.id.happinessFragment) {
                        navController.navigate(R.id.happinessFragment, null, navOptions)
                    }
                    true
                }
                R.id.optimismFragment -> {
                    if (currentFragmentId != R.id.optimismFragment) {
                        navController.navigate(R.id.optimismFragment, null, navOptions)
                    }
                    true
                }
                R.id.kindnessFragment -> {
                    if (currentFragmentId != R.id.kindnessFragment) {
                        navController.navigate(R.id.kindnessFragment, null, navOptions)
                    }
                    true
                }
                R.id.givingFragment -> {
                    if (currentFragmentId != R.id.givingFragment) {
                        navController.navigate(R.id.givingFragment, null, navOptions)
                    }
                    true
                }
                R.id.respectFragment -> {
                    if (currentFragmentId != R.id.respectFragment) {
                        navController.navigate(R.id.respectFragment, null, navOptions)
                    }
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
