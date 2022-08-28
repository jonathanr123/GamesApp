package com.example.gamesapp.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gamesapp.R
import com.example.gamesapp.ui.view.adapters.GameListAdapter
import com.example.gamesapp.databinding.FragmentHomeBinding
import com.example.gamesapp.utils.*
import com.example.gamesapp.ui.viewmodel.HomeViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var video: ExoPlayer
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

    private lateinit var volumeState: VolumeState
    private lateinit var scroll: ScrollView
    private var positionVideo: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        // Show bottom navigation view
        val activity = requireActivity() as MainActivity
        activity.findViewById<BottomNavigationView>(R.id.navigationView).visible()

        // Set Up Toolbar
        activity.supportActionBar?.hide()

        setUpScrollView()

        setUpRecyclerViewGamesPopular()
        setUpRecyclerViewGamesTrending()
        setUpRecyclerViewGamesLastYear()
        setUpRecyclerViewGamesTagSpecific()
        setUpRecyclerViewGamesPublisherSpecific()

        return binding.root
    }

    /*
    * Function that pauses or plays the presentation video depending on whether it is visible in
    * the scrollview or not.
     */
    private fun setUpScrollView() {
        scroll = binding.svHome
        scroll.viewTreeObserver.addOnScrollChangedListener {
            println("scroll" + scroll.scrollY)
            val visible = binding.videoViewHeader.isPartiallyOrFullyVisible(scroll)
            println("vista$visible")
            if (!visible){
                positionVideo = binding.videoViewHeader.player?.currentPosition?.toInt() ?: 0
                binding.videoViewHeader.player?.pause()
            }else{
                if (binding.videoViewHeader.player?.isPlaying == false) {
                    binding.videoViewHeader.player?.seekTo(positionVideo.toLong())
                    binding.videoViewHeader.player?.play()

                }
            }
        }
    }

    // Set up the videoView with random url and volume controls
    private fun setUpVideoView() {
        val arrayVideos: List<MediaItem> = listOf(
            MediaItem.fromUri("https://media.rawg.io/media/stories/65f/65ff9214af64ca0c89abac55d80ed7ab.mp4"),
            MediaItem.fromUri("https://media.rawg.io/media/stories-640/17a/17ae7e8910249099275e1e3688bc1e37.mp4"),
            MediaItem.fromUri("https://media.rawg.io/media/stories-640/5c1/5c1914a7f914e849e3417f79e1dd2b71.mp4"),
            MediaItem.fromUri("https://media.rawg.io/media/stories-640/976/976316544d75b8db276ff5e4ca01e189.mp4"),
            MediaItem.fromUri("https://media.rawg.io/media/stories-640/e58/e58a4f95453d2bac7dde75f600d62c59.mp4"),
            MediaItem.fromUri("https://media.rawg.io/media/stories/e3c/e3c7fed123159b9bcfffad0454a0f87f.mp4"),
            MediaItem.fromUri("https://media.rawg.io/media/stories-640/6a1/6a153a00fd079483759a11187ee95b1b.mp4"),
            MediaItem.fromUri("https://media.rawg.io/media/stories-640/28e/28e5af04a0a66cb3b2ccc5cab2abaf09.mp4")
        )
        video = ExoPlayer.Builder(requireContext()).build()
        video.setMediaItems(arrayVideos,true)
        video.repeatMode = Player.REPEAT_MODE_ALL
        video.shuffleModeEnabled = true
        video.prepare()
        video.play()
        binding.videoViewHeader.player = video
        binding.videoViewHeader.useController = false
        binding.videoViewHeader.setOnClickListener { toggleVolume() }
        setVolumeControl(VolumeState.ON)
    }

    /**
     * Volume ENUM
     */
    private enum class VolumeState {
        ON, OFF
    }

    private fun toggleVolume() {
        if (volumeState == VolumeState.OFF) {
            setVolumeControl(VolumeState.ON)
        } else if (volumeState == VolumeState.ON) {
            setVolumeControl(VolumeState.OFF)
        }
    }

    // Set up the volume controls
    private fun setVolumeControl(state: VolumeState) {
        volumeState = state
        if (state == VolumeState.OFF) {
            binding.videoViewHeader.player?.volume = 0f
            animateVolumeControl()
        } else if (state == VolumeState.ON) {
            binding.videoViewHeader.player?.volume = 1f
            animateVolumeControl()
        }
    }

    // Animate the ivVolumeControl when is clicked
    private fun animateVolumeControl() {
        binding.ivVolumeControl.bringToFront()
        if (volumeState == VolumeState.OFF) {
            binding.ivVolumeControl.load(R.drawable.ic_volume_off)
        } else if (volumeState == VolumeState.ON) {
            binding.ivVolumeControl.load(R.drawable.ic_volume_on)
        }
        binding.ivVolumeControl.animate()?.cancel()
        binding.ivVolumeControl.alpha = 1f
        binding.ivVolumeControl.animate()
            ?.alpha(0f)
            ?.setDuration(600)?.startDelay = 1000
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
                    adapterPopular.manager = parentFragmentManager
                    adapterPopular.isGamesTop = true
                    binding.incSectionGames.lySectionGames.visible()
                    binding.progressLoader.root.gone()
                    // Activate the videoView of the presentation video
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
                    adapterTrending.manager = parentFragmentManager
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
                    adapterLastYear.manager = parentFragmentManager
                    adapterLastYear.widthCard = 110
                    adapterLastYear.heightCard = 160
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
                    adapterTagSpecific.manager = parentFragmentManager
                    adapterTagSpecific.widthCard = 110
                    adapterTagSpecific.heightCard = 160
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
                    adapterPublisherSpecific.manager = parentFragmentManager
                    adapterPublisherSpecific.widthCard = 110
                    adapterPublisherSpecific.heightCard = 160
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

    override fun onPause() {
        super.onPause()
        binding.videoViewHeader.player?.release()
    }

    override fun onStop() {
        super.onStop()
        binding.videoViewHeader.player?.release()
    }

    override fun onResume() {
        super.onResume()
        setUpVideoView()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoViewHeader.player?.release()
    }

}