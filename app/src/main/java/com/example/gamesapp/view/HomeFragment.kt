package com.example.gamesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamesapp.view.adapters.CreatorListAdapter
import com.example.gamesapp.view.adapters.GameListAdapter
import com.example.gamesapp.view.adapters.GenreListAdapter
import com.example.gamesapp.databinding.FragmentHomeBinding
import com.example.gamesapp.utils.RawgApiResult
import com.example.gamesapp.utils.ZoomRecyclerLayout
import com.example.gamesapp.utils.gone
import com.example.gamesapp.utils.visible
import com.example.gamesapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var rvCreators: RecyclerView
    private val adapterCreators = CreatorListAdapter()
    private lateinit var rvGenres: RecyclerView
    private val adapterGenres = GenreListAdapter()
    private lateinit var rvPopular: RecyclerView
    private val adapterPopular = GameListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        setUpRecyclerViewCreators()

        setUpRecyclerViewGenres()

        setUpRecyclerViewGamesPopular()

        return binding.root
    }


    private fun setUpRecyclerViewCreators() {

        rvCreators = binding.incSectionCreators.rvCreator

        homeViewModel.creators.observe(viewLifecycleOwner){ response ->
            when (response) {
                is RawgApiResult.Success -> {
                    rvCreators.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rvCreators.adapter = adapterCreators
                    adapterCreators.submitList(response.data?.result)
                    binding.incSectionCreators.lySectionCreators.visible()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.Failure -> {
                    binding.incSectionCreators.lySectionCreators.gone()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.ErrorThrowable -> {
                    binding.incSectionCreators.lySectionCreators.gone()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.Loading -> {
                    binding.incSectionCreators.lySectionCreators.gone()
                    binding.progressLoader.root.visible()
                }
            }
        }
    }

    private fun setUpRecyclerViewGenres() {

        rvGenres = binding.incSectionGenres.rvGenres

        homeViewModel.genres.observe(viewLifecycleOwner){ response ->
            when (response) {
                is RawgApiResult.Success -> {
                    rvGenres.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rvGenres.adapter = adapterGenres
                    adapterGenres.submitList(response.data?.result)
                    binding.incSectionGenres.lySectionGenres.visible()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.Failure -> {
                    binding.incSectionGenres.lySectionGenres.gone()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.ErrorThrowable -> {
                    binding.incSectionGenres.lySectionGenres.gone()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.Loading -> {
                    binding.incSectionGenres.lySectionGenres.gone()
                    binding.progressLoader.root.visible()
                }
            }
        }
    }

    private fun setUpRecyclerViewGamesPopular() {

        rvPopular = binding.incSectionGames.rvGames

        homeViewModel.gamesPopular.observe(viewLifecycleOwner){ response ->
            when (response) {
                is RawgApiResult.Success -> {
                    rvPopular.layoutManager = ZoomRecyclerLayout(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rvPopular.adapter = adapterPopular
                    adapterPopular.submitList(response.data?.result)
                    binding.incSectionGames.lySectionGames.visible()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.Failure -> {
                    binding.incSectionGames.lySectionGames.gone()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.ErrorThrowable -> {
                    binding.incSectionGames.lySectionGames.gone()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.Loading -> {
                    binding.incSectionGames.lySectionGames.gone()
                    binding.progressLoader.root.visible()
                }
            }
        }
    }

}