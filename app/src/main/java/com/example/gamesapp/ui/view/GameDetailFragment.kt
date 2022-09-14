package com.example.gamesapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gamesapp.R
import com.example.gamesapp.databinding.FragmentGameDetailBinding
import com.example.gamesapp.data.model.Games
import com.example.gamesapp.ui.view.adapters.CreatorListAdapter
import com.example.gamesapp.utils.*
import com.example.gamesapp.ui.view.adapters.ScreenshotListAdapter
import com.example.gamesapp.ui.viewmodel.GameDetailViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameDetailFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentGameDetailBinding
    private val gameDetailViewModel: GameDetailViewModel by activityViewModels()
    private var gameReceived: Games? = null

    private lateinit var rvScreenshot: RecyclerView
    private val adapterScreenshot = ScreenshotListAdapter()
    private lateinit var rvCreators: RecyclerView
    private val adapterCreators = CreatorListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGameDetailBinding.inflate(layoutInflater, container, false)

        // Hide bottom navigation view
        val activity = requireActivity() as MainActivity
        activity.findViewById<BottomNavigationView>(R.id.navigationView).gone()

        // Set Up Toolbar
        activity.supportActionBar?.show()
        activity.supportActionBar?.title = "Game Details"
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.color.gold))
        activity.findViewById<Toolbar>(R.id.toolbar).setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.black))

        // Received game from previous fragment and set it to view model
        gameReceived = arguments?.getSerializable("game") as Games?
        gameDetailViewModel.setGameSelected(gameReceived)

        // Set views with data from view model
        gameDetailViewModel.gameSelected.observe(viewLifecycleOwner){ response ->
            response as Games
            setUpView(response)
            setUpRecyclerViewScreenshots(response)
            gameDetailViewModel.fetchGameById(response.id)
            gameDetailViewModel.fetchCreators(response.id)
        }

        setUpViewAdditional()

        setUpRecyclerViewCreators()

        setUpClickListenerBtnFavorite()

        return binding.root
    }

    // Set up click listener of btnFavorite depending if the game is favorite list in the DB
    private fun setUpClickListenerBtnFavorite() {

        gameDetailViewModel.isFavoriteGame()

        gameDetailViewModel.isFavorite.observe(viewLifecycleOwner){ response ->
            response as Boolean
            if(response){
                binding.ivBtnFavorite.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gold))
                binding.ivBtnFavorite.setOnClickListener {
                    // Delete game of the favorite list in the DB
                    gameDetailViewModel.deleteFavoriteGame()
                    // Change tint color of ivBtnFavorite
                    binding.ivBtnFavorite.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_shimmer))
                    gameDetailViewModel.isFavoriteGame()
                }
            }else{
                binding.ivBtnFavorite.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_shimmer))
                binding.ivBtnFavorite.setOnClickListener {
                    // Insert game in the favorite list in the DB
                    gameDetailViewModel.insertFavoriteGame()
                    // Change tint color of ivBtnFavorite
                    binding.ivBtnFavorite.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gold))
                    gameDetailViewModel.isFavoriteGame()
                }
            }
        }
    }

    // Set up view for info received from previous fragment
    private fun setUpView(game: Games) {
        with (binding){
            ivImageDetail.load(game.imageURL)
            tvTitleDetail.text = game.name
            tvDateDetail.text = game.released?.convertDate()
            rbarRatingDetail.rating = game.rating!!
            // foreach loop to set up genres
            tvGenreDetail.text = ""
            tvPlatformDetail.text = ""
            game.genres?.forEach {
                if (it == game.genres.last()) {
                    tvGenreDetail.append(it.name + ".")
                } else {
                    tvGenreDetail.append(it.name + ", ")
                }
            }
            // foreach loop to set up platforms
            game.platforms?.forEach {
                if (it == game.platforms.last()) {
                    tvPlatformDetail.append(it.platform?.name + ".")
                } else {
                    tvPlatformDetail.append(it.platform?.name + ", ")
                }
            }
        }
    }

    // Set up recycler view for screenshots
    private fun setUpRecyclerViewScreenshots(game: Games) {
        rvScreenshot = binding.rvScreenshots
        rvScreenshot.adapter = adapterScreenshot
        adapterScreenshot.submitList(game.shortScreenshots)
    }

    // Set up view additional for game selected
    private fun setUpViewAdditional(){
        gameDetailViewModel.gameDetail.observe(viewLifecycleOwner){ response ->
            when (response) {
                is RawgApiResult.Success -> {
                    binding.tvDescriptionDetail.text = response.data?.description
                    binding.tvDevelopersDetail.text = ""
                    response.data?.developers?.forEach {
                        if (it == response.data.developers.last()) {
                            binding.tvDevelopersDetail.append(it.name + ".")
                        } else {
                            binding.tvDevelopersDetail.append(it.name + ", ")
                        }
                    }
                    binding.tvWebsiteDetail.text = response.data?.websiteURL
                }
                is RawgApiResult.Failure -> {
                    gameDetailViewModel.fetchGameById(gameDetailViewModel.gameSelected.value?.id!!)
                }
                is RawgApiResult.ErrorThrowable -> {
                    gameDetailViewModel.fetchGameById(gameDetailViewModel.gameSelected.value?.id!!)
                }
                is RawgApiResult.Loading -> {
                }
            }
        }
    }

    // Set up recycler view creators and adapter for section Creators (Development team)
    private fun setUpRecyclerViewCreators() {

        rvCreators = binding.incSectionCreators.rvCreator
        binding.incSectionCreators.tvTitleSectionCreators.gone()

        gameDetailViewModel.creators.observe(viewLifecycleOwner){ response ->
            when (response) {
                is RawgApiResult.Success -> {
                    rvCreators.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rvCreators.adapter = adapterCreators
                    adapterCreators.submitList(response.data?.result)
                    binding.incSectionCreators.lySectionCreators.visible()
                }
                is RawgApiResult.Failure -> {
                    gameDetailViewModel.fetchCreators(gameDetailViewModel.gameSelected.value?.id!!)
                    binding.incSectionCreators.lySectionCreators.gone()
                }
                is RawgApiResult.ErrorThrowable -> {
                    gameDetailViewModel.fetchCreators(gameDetailViewModel.gameSelected.value?.id!!)
                    binding.incSectionCreators.lySectionCreators.gone()
                }
                is RawgApiResult.Loading -> {
                    binding.incSectionCreators.lySectionCreators.gone()
                }
            }
        }
    }

}