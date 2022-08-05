package com.example.gamesapp.view

import android.annotation.SuppressLint
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
    private lateinit var rvTrending: RecyclerView
    private val adapterTrending = GameListAdapter()
    private lateinit var rvLastYear: RecyclerView
    private val adapterLastYear = GameListAdapter()
    private lateinit var rvTagSpecific: RecyclerView
    private val adapterTagSpecific = GameListAdapter()
    private lateinit var rvPublisherSpecific: RecyclerView
    private val adapterPublisherSpecific = GameListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        setUpRecyclerViewCreators()
        setUpRecyclerViewGenres()
        setUpRecyclerViewGamesPopular()
        setUpRecyclerViewGamesTrending()
        setUpRecyclerViewGamesLastYear()
        setUpRecyclerViewGamesTagSpecific()
        setUpRecyclerViewGamesPublisherSpecific()

        return binding.root
    }

    // Set up recycler view creators and adapter for section creators
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

    // Set up recycler view genres and adapter for section genres
    private fun setUpRecyclerViewGenres() {

        rvGenres = binding.incSectionGenres.rvGenres

        homeViewModel.genres.observe(viewLifecycleOwner){ response ->
            when (response) {
                is RawgApiResult.Success -> {
                    rvGenres.layoutManager = ZoomRecyclerLayout(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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

    // Set up recycler view games popular and adapter for section games popular
    private fun setUpRecyclerViewGamesPopular() {

        rvPopular = binding.incSectionGames.rvGames

        homeViewModel.gamesPopular.observe(viewLifecycleOwner){ response ->
            when (response) {
                is RawgApiResult.Success -> {
                    rvPopular.layoutManager = ZoomRecyclerLayout(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rvPopular.adapter = adapterPopular
                    adapterPopular.submitList(response.data?.result?.subList(0, 10))
                    adapterPopular.isGamesTop = true
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

    // Set up recycler view games trending and adapter for section games trending
    @SuppressLint("SetTextI18n")
    private fun setUpRecyclerViewGamesTrending() {

        rvTrending = binding.incSectionTrending.rvGames
        binding.incSectionTrending.tvTitleSectionGames.text = "New and Trending"

        homeViewModel.gamesTrending.observe(viewLifecycleOwner){ response ->
            when (response) {
                is RawgApiResult.Success -> {
                    rvTrending.layoutManager = ZoomRecyclerLayout(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rvTrending.adapter = adapterTrending
                    adapterTrending.submitList(response.data?.result)
                    adapterTrending.widthCard = 140
                    adapterTrending.heightCard = 320
                    binding.incSectionTrending.lySectionGames.visible()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.Failure -> {
                    binding.incSectionTrending.lySectionGames.gone()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.ErrorThrowable -> {
                    binding.incSectionTrending.lySectionGames.gone()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.Loading -> {
                    binding.incSectionTrending.lySectionGames.gone()
                    binding.progressLoader.root.visible()
                }
            }
        }
    }

    // Set up recycler view games of the last year and adapter for section games of the last year
    @SuppressLint("SetTextI18n")
    private fun setUpRecyclerViewGamesLastYear() {

        rvLastYear = binding.incSectionLastYear.rvGames
        binding.incSectionLastYear.tvTitleSectionGames.text = "Last Year"

        homeViewModel.gamesLastYear.observe(viewLifecycleOwner){ response ->
            when (response) {
                is RawgApiResult.Success -> {
                    rvLastYear.layoutManager = ZoomRecyclerLayout(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rvLastYear.adapter = adapterLastYear
                    adapterLastYear.submitList(response.data?.result)
                    adapterLastYear.widthCard = 90
                    adapterLastYear.heightCard = 130
                    binding.incSectionLastYear.lySectionGames.visible()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.Failure -> {
                    binding.incSectionLastYear.lySectionGames.gone()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.ErrorThrowable -> {
                    binding.incSectionLastYear.lySectionGames.gone()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.Loading -> {
                    binding.incSectionLastYear.lySectionGames.gone()
                    binding.progressLoader.root.visible()
                }
            }
        }
    }

    // Set up recycler view games of the tag specified and adapter for section games of the tag specified
    @SuppressLint("SetTextI18n")
    private fun setUpRecyclerViewGamesTagSpecific() {

        rvTagSpecific = binding.incSectionTag.rvGames
        binding.incSectionTag.tvTitleSectionGames.text = "Battle Royale"

        homeViewModel.gamesTagSpecific.observe(viewLifecycleOwner){ response ->
            when (response) {
                is RawgApiResult.Success -> {
                    rvTagSpecific.layoutManager = ZoomRecyclerLayout(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rvTagSpecific.adapter = adapterTagSpecific
                    adapterTagSpecific.submitList(response.data?.result)
                    adapterTagSpecific.widthCard = 90
                    adapterTagSpecific.heightCard = 130
                    binding.incSectionTag.lySectionGames.visible()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.Failure -> {
                    binding.incSectionTag.lySectionGames.gone()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.ErrorThrowable -> {
                    binding.incSectionTag.lySectionGames.gone()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.Loading -> {
                    binding.incSectionTag.lySectionGames.gone()
                    binding.progressLoader.root.visible()
                }
            }
        }
    }

    // Set up recycler view games of the Publisher specified and adapter for section games of the Publisher specified
    @SuppressLint("SetTextI18n")
    private fun setUpRecyclerViewGamesPublisherSpecific() {

        rvPublisherSpecific = binding.incSectionPublisher.rvGames
        binding.incSectionPublisher.tvTitleSectionGames.text = "By Ubisoft Entertainment"

        homeViewModel.gamesPublisherSpecific.observe(viewLifecycleOwner){ response ->
            when (response) {
                is RawgApiResult.Success -> {
                    rvPublisherSpecific.layoutManager = ZoomRecyclerLayout(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rvPublisherSpecific.adapter = adapterPublisherSpecific
                    adapterPublisherSpecific.submitList(response.data?.result)
                    adapterPublisherSpecific.widthCard = 90
                    adapterPublisherSpecific.heightCard = 130
                    binding.incSectionPublisher.lySectionGames.visible()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.Failure -> {
                    binding.incSectionPublisher.lySectionGames.gone()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.ErrorThrowable -> {
                    binding.incSectionPublisher.lySectionGames.gone()
                    binding.progressLoader.root.gone()
                }
                is RawgApiResult.Loading -> {
                    binding.incSectionPublisher.lySectionGames.gone()
                    binding.progressLoader.root.visible()
                }
            }
        }
    }

}