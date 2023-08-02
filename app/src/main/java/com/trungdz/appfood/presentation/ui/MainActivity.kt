package com.trungdz.appfood.presentation.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.trungdz.appfood.R
import com.trungdz.appfood.data.util.Utils
import com.trungdz.appfood.data.util.cache.ISharedPreference
import com.trungdz.appfood.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //    private val loggin = true // test
    private var currentMenuPosition = R.id.homeFragment

    @Inject
    lateinit var sharedPreferences: ISharedPreference
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Login", "${sharedPreferences.getLoggedInStatus()}")
        sharedPreferences.saveLoggedInStatus(false)
        sharedPreferences.removeToken()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setConnectToolbarWithNavController()
        setItemMenuEventListener()
    }

    override fun onStop() {
        super.onStop()

    }

    override fun onDestroy() {
        super.onDestroy()
//        Log.d("DangXuat", "${sharedPreferences.getLoggedInStatus()}")
//        sharedPreferences.saveLoggedInStatus(false)
    }

    private fun setConnectToolbarWithNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_of_main_activity) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val toolbar = binding.toolbar
            val menuNav = binding.bottomNavigationView
            when (destination.id) { // hide toolbar + bot nav
                R.id.homeFragment, R.id.cartFragment, R.id.orderHistoryFragment, R.id.wishlistFragment -> {
                    toolbar.visibility = View.VISIBLE
                    menuNav.visibility = View.VISIBLE
                }
                else -> { // fullscreen
                    toolbar.visibility = View.GONE
                    menuNav.visibility = View.GONE
                }
            }

        }
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.cartFragment,
                R.id.orderHistoryFragment,
                R.id.wishlistFragment
            )
        )
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.toolbar
            .setupWithNavController(navController, appBarConfiguration)
    }

    private fun setItemMenuEventListener() {
        binding.bottomNavigationView.menu.findItem(R.id.cartFragment).setOnMenuItemClickListener {
            if (!sharedPreferences.getLoggedInStatus()) {
                Utils.alertDialogLogin(
                    this,
                    findNavController(R.id.nav_host_fragment_of_main_activity)
                )
                true
            } else {
                false
            }
        }
        binding.bottomNavigationView.menu.findItem(R.id.orderHistoryFragment)
            .setOnMenuItemClickListener {
                if (!sharedPreferences.getLoggedInStatus()) {
                    Utils.alertDialogLogin(
                        this,
                        findNavController(R.id.nav_host_fragment_of_main_activity)
                    )
                    true
                } else {
                    false
                }
            }
        binding.bottomNavigationView.menu.findItem(R.id.wishlistFragment)
            .setOnMenuItemClickListener {
                if (!sharedPreferences.getLoggedInStatus()) {
                    Utils.alertDialogLogin(
                        this,
                        findNavController(R.id.nav_host_fragment_of_main_activity)
                    )
                    true
                } else {
                    false
                }
            }
    }

    fun setItemMenuBadge(count: Int) {
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).getOrCreateBadge(R.id.cartFragment).number =
            count
    }

    fun getItemMenuBadge(): Int {
        return findViewById<BottomNavigationView>(R.id.bottomNavigationView).getOrCreateBadge(R.id.cartFragment).number
    }
}