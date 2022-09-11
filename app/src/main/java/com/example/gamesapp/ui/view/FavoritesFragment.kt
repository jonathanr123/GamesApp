package com.example.gamesapp.ui.view

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamesapp.R
import com.example.gamesapp.databinding.FragmentFavoritesBinding
import com.example.gamesapp.ui.view.adapters.FavoriteGameListAdapter
import com.example.gamesapp.ui.viewmodel.FavoritesViewModel
import com.example.gamesapp.utils.gone
import com.example.gamesapp.utils.visible
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentFavoritesBinding
    private val favoritesViewModel: FavoritesViewModel by activityViewModels()

    private lateinit var rvFavorites: RecyclerView
    private val adapterFavorites = FavoriteGameListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)

        // Show bottom navigation view
        val activity = requireActivity() as MainActivity
        activity.findViewById<BottomNavigationView>(R.id.navigationView).visible()

        // Set Up Toolbar
        activity.supportActionBar?.show()
        activity.supportActionBar?.title = "Favorites"
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.color.black))
        activity.findViewById<Toolbar>(R.id.toolbar).setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.gold))

        setUpRecyclerViewFavoriteGames()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setUpRecyclerViewFavoriteGames() {

        favoritesViewModel.fetchFavoriteGames()
        binding.incSectionFavorites.tvTitleSectionGames.gone()
        rvFavorites = binding.incSectionFavorites.rvGames
        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(context, 3)
            } else {
                GridLayoutManager(context, 2)
            }

        favoritesViewModel.favoriteGames.observe(viewLifecycleOwner) { response ->
            rvFavorites.layoutManager = layoutManager
            rvFavorites.adapter = adapterFavorites
            adapterFavorites.manager = parentFragmentManager
            adapterFavorites.submitList(response)
        }
    }

}