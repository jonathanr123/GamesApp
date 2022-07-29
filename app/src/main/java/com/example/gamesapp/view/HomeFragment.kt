package com.example.gamesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamesapp.adapters.GameListAdapter
import com.example.gamesapp.databinding.FragmentHomeBinding
import com.example.gamesapp.model.Games
import com.example.gamesapp.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var rvGames: RecyclerView
    private val adapter by lazy { GameListAdapter() }
    private lateinit var rvRecomendados: RecyclerView
    private val adapter2 by lazy { GameListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        setUpRecyclerViewGames()

        setUpRecyclerViewRecomendados()

        return binding.root
    }

    private fun setUpRecyclerViewRecomendados() {
        val data = listOf(
            Games(1, "GTA","https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg"),
            Games(1, "GTA","https://media.rawg.io/media/games/021/021c4e21a1824d2526f925eff6324653.jpg"),)
        binding.incSectionRecomendados.tvTitleSectionGames.text = "Populares"
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
            Games(1, "GTA","https://media.rawg.io/media/games/021/021c4e21a1824d2526f925eff6324653.jpg"),)
        binding.incSectionGames.tvTitleSectionGames.text = "Populares"
        rvGames = binding.incSectionGames.rvGames
        rvGames.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvGames.adapter = adapter
        adapter.submitList(data)
    }

}