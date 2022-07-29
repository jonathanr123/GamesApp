package com.example.gamesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamesapp.adapters.CreatorListAdapter
import com.example.gamesapp.adapters.GameListAdapter
import com.example.gamesapp.adapters.GenreListAdapter
import com.example.gamesapp.databinding.FragmentHomeBinding
import com.example.gamesapp.model.Creators
import com.example.gamesapp.model.Games
import com.example.gamesapp.model.Genres
import com.example.gamesapp.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var rvGames: RecyclerView
    private val adapter by lazy { GameListAdapter() }
    private lateinit var rvRecomendados: RecyclerView
    private val adapter2 by lazy { GameListAdapter() }
    private lateinit var rvCreators: RecyclerView
    private val adapter3 by lazy { CreatorListAdapter() }
    private lateinit var rvGenres: RecyclerView
    private val adapter4 by lazy { GenreListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        setUpRecyclerViewCreators()

        setUpRecyclerViewGenres()

        setUpRecyclerViewGames()

        setUpRecyclerViewRecomendados()

        return binding.root
    }

    private fun setUpRecyclerViewGenres() {
        val data = listOf(
            Genres(1, "GTA","https://media.rawg.io/media/games/960/960b601d9541cec776c5fa42a00bf6c4.jpg"),
            Genres(2, "GTA","https://media.rawg.io/media/games/f99/f9979698c43fd84c3ab69280576dd3af.jpg"),
            Genres(3, "GTA","https://media.rawg.io/media/games/d1a/d1a2e99ade53494c6330a0ed945fe823.jpg"),
            Genres(4, "GTA","https://media.rawg.io/media/games/d1e/d1e70ce3762efcfc170c6bd067d7e9e3.jpg"))
        rvGenres = binding.incSectionGenres.rvGenres
        rvGenres.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvGenres.adapter = adapter4
        adapter4.submitList(data)
    }

    private fun setUpRecyclerViewCreators() {
        val data = listOf(
            Creators(1, "Juan Perez","https://media.rawg.io/media/persons/5e5/5e5e064d3475fc3fe49d1d2debb4e36c.jpg"),
            Creators(2, "Pablo Ruiz","https://media.rawg.io/media/persons/da3/da3fc907a3af9e494dc671b0c6348f5c.jpg"),
            Creators(3, "Rodrigo Sevenz","https://media.rawg.io/media/persons/b76/b76df211424e553218ce800f9b1d38f0.png"),
            Creators(4, "Santiago Dulio","https://media.rawg.io/media/persons/513/51388ad8c1db829a2fcb0353560f0f2a.png"),
            Creators(5, "Diego Juarez","https://media.rawg.io/media/persons/6ea/6ea06e2ddd6c0190e5134f61d826f30f.jpg"))
        rvCreators = binding.incSectionCreators.rvCreator
        rvCreators.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvCreators.adapter = adapter3
        adapter3.submitList(data)
    }

    private fun setUpRecyclerViewRecomendados() {
        val data = listOf(
            Games(1, "GTA","https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg"),
            Games(1, "GTA","https://media.rawg.io/media/games/021/021c4e21a1824d2526f925eff6324653.jpg"))
        binding.incSectionRecomendados.tvTitleSectionGames.text = "Categories"
        rvRecomendados = binding.incSectionRecomendados.rvGames
        rvRecomendados.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvRecomendados.adapter = adapter2
        adapter2.submitList(data)
    }

    private fun setUpRecyclerViewGames() {
        val data = listOf(
            Games(1, "GTA","https://media.rawg.io/media/games/456/456dea5e1c7e3cd07060c14e96612001.jpg"),
            Games(1, "GTA","https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"),
            Games(1, "GTA","https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg"),
            Games(1, "GTA","https://media.rawg.io/media/games/021/021c4e21a1824d2526f925eff6324653.jpg"))
        binding.incSectionGames.tvTitleSectionGames.text = "Games Trending"
        rvGames = binding.incSectionGames.rvGames
        rvGames.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvGames.adapter = adapter
        adapter.submitList(data)
    }

}