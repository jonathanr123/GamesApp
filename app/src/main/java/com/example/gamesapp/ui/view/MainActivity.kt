package com.example.gamesapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.example.gamesapp.R
import com.example.gamesapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Properties
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = supportActionBar!!
        toolbar.hide()
        bottomNavView = binding.navigationView

        // Set up the bottom navigation view
        bottomNavView.setOnItemSelectedListener{ itemMenu ->
            navigationFragments(itemMenu)
            true
        }

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

}