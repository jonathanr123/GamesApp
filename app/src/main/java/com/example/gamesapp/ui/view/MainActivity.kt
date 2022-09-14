package com.example.gamesapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gamesapp.R
import com.example.gamesapp.databinding.ActivityMainBinding
import com.example.gamesapp.utils.CheckNetworkConnection
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Properties
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView

    private val snackbar: Snackbar by lazy {
        Snackbar.make(binding.navHostFragment, R.string.snackbar_internet, Snackbar.LENGTH_INDEFINITE)
        .setBackgroundTint(ContextCompat.getColor(this, R.color.red)) }
    private lateinit var checkNetworkConnection: CheckNetworkConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set Up Toolbar
        setSupportActionBar(binding.toolbar.toolbar)

        bottomNavView = binding.navigationView

        // Set up navigation
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
        if (navController != null) {
            bottomNavView.setupWithNavController(navController)
        }

        callNetworkConnection()

    }

    // Override the function when clicking the back arrow of the toolbar
    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return false
    }

    //Check the network connection
    private fun callNetworkConnection() {
        checkNetworkConnection = CheckNetworkConnection(application)
        checkNetworkConnection.observe(this) { isConnected ->
            if (isConnected) {
                if (snackbar.isShown) {
                    snackbar.dismiss()
                }
            } else {
                snackbar.show()
            }
        }
    }

}