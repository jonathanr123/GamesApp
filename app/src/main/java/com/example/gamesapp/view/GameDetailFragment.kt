package com.example.gamesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gamesapp.R
import com.example.gamesapp.databinding.FragmentGameDetailBinding
import com.example.gamesapp.model.Games
import com.example.gamesapp.utils.*
import com.example.gamesapp.view.adapters.ScreenshotListAdapter
import com.example.gamesapp.viewmodel.GameDetailViewModel
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGameDetailBinding.inflate(layoutInflater, container, false)

        // Hide bottom navigation view
        val activity = requireActivity() as MainActivity
        activity.findViewById<BottomNavigationView>(R.id.navigationView).gone()

        // Received game from previous fragment and set it to view model
        setFragmentResultListener("game_detail") { _, bundle ->
            gameReceived = bundle.getSerializable("game") as Games?
            gameDetailViewModel.setGameSelected(gameReceived)
        }

        // Set views with data from view model
        gameDetailViewModel.gameSelected.observe(viewLifecycleOwner){ response ->
            response as Games
            setUpView(response)
            setUpRecyclerViewScreenshots(response)
            gameDetailViewModel.fetchGameById(response.id)
        }

        setUpViewAdditional()

        return binding.root
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

}