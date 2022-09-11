package com.example.gamesapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
        Snackbar.make(binding.fragmentContainerView, R.string.snackbar_internet, Snackbar.LENGTH_INDEFINITE)
        .setBackgroundTint(ContextCompat.getColor(this, R.color.red)) }
    private lateinit var checkNetworkConnection: CheckNetworkConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set Up Toolbar
        setSupportActionBar(binding.toolbar.toolbar)

        bottomNavView = binding.navigationView

        // Set up the bottom navigation view
        bottomNavView.setOnItemSelectedListener{ itemMenu ->
            navigationFragments(itemMenu)
            true
        }

        callNetworkConnection()

    }

    // Set up the fragments
    private fun navigationFragments(item: MenuItem) {
        when (item.itemId) {
            // Navigation to Home fragment
            R.id.menu_home -> {
                changeFragment(HomeFragment())
            }

            // Navigation to Search fragment
            R.id.menu_search -> {
                changeFragment(SearchFragment())
            }

            // Navigation to Videos fragment
            R.id.menu_videos -> {
                changeFragment(VideosFragment())
            }

            // Navigation to Favorites fragment
            R.id.menu_favorites -> {
                changeFragment(FavoritesFragment())
            }
        }
    }

    // Replace the fragment in the ContainerView
    private fun changeFragment(frag: Fragment){
        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragmentContainerView,frag).commit()
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