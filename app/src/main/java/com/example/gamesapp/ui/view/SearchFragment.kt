package com.example.gamesapp.ui.view

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamesapp.R
import com.example.gamesapp.databinding.FragmentSearchBinding
import com.example.gamesapp.utils.RawgApiResult
import com.example.gamesapp.utils.gone
import com.example.gamesapp.utils.visible
import com.example.gamesapp.ui.view.adapters.AllGamesListAdapter
import com.example.gamesapp.ui.view.adapters.SearchListAdapter
import com.example.gamesapp.ui.viewmodel.SearchViewModel
import com.example.gamesapp.utils.setColorSwipe
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by activityViewModels()

    private lateinit var rvAllGames: RecyclerView
    private val adapterAllGames = AllGamesListAdapter()

    private lateinit var rvSearchResults: RecyclerView
    private val adapterSearchResults = SearchListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        // Show bottom navigation view
        val activity = requireActivity() as MainActivity
        activity.findViewById<BottomNavigationView>(R.id.navigationView).visible()

        // Set Up Toolbar
        activity.supportActionBar?.show()
        activity.supportActionBar?.title = "Search"
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.color.black))
        activity.findViewById<Toolbar>(R.id.toolbar).setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.gold))

        viewLifecycleOwner.lifecycleScope.launch {
            setUpRecyclerViewAllGames()
        }

        // Detect if the text in the search field is empty or not, and use it to filter the games list
        binding.svSearchGame.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchViewModel.fetchGamesByName(s.toString())
                if (s.toString().isEmpty()) {
                    binding.ivSearchClose.gone()
                }else{
                    binding.ivSearchClose.visible()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        setUpSwipeRefresh()

        setUpSearchView()

        setUpRecyclerViewGamesSearch()

        return binding.root
    }

    // Set up Swipe Refresh component
    private fun setUpSwipeRefresh() {
        setColorSwipe(requireContext(), binding.swipeRefreshHome)
        binding.swipeRefreshHome.setOnRefreshListener {
            binding.swipeRefreshHome.isRefreshing = true
            searchViewModel.refreshLayout()
            CoroutineScope(Dispatchers.Main).launch {
                delay(4000)
                binding.swipeRefreshHome.isRefreshing = false
            }
        }
    }

    // Set up recycler view for all games list
    private suspend fun setUpRecyclerViewAllGames() {
        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(context, 6)
            } else {
                GridLayoutManager(context, 3)
            }
        rvAllGames = binding.incSectionGames.rvGames
        binding.incSectionGames.tvTitleSectionGames.gone()
        rvAllGames.layoutManager = layoutManager
        rvAllGames.adapter = adapterAllGames
        adapterAllGames.manager = parentFragmentManager
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            searchViewModel.allGames.collect { pagingData ->
                adapterAllGames.submitData(pagingData.first())
            }
        }
    }

    // Set up recycler view for search results
    private fun setUpRecyclerViewGamesSearch() {

        rvSearchResults = binding.rvSearchGames

        searchViewModel.gamesSearch.observe(viewLifecycleOwner){ response ->
            when (response) {
                is RawgApiResult.Success -> {
                    rvSearchResults.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    rvSearchResults.adapter = adapterSearchResults
                    adapterSearchResults.submitList(response.data?.result)
                    adapterSearchResults.manager = parentFragmentManager
                }
                is RawgApiResult.Failure -> {
                }
                is RawgApiResult.ErrorThrowable -> {
                }
                is RawgApiResult.Loading -> {
                }
            }
        }
    }

    // Set up search view and its listeners
    private fun setUpSearchView() {
        // Detect if the search view is focused and show/hide the sections of the view depending on the focus
        binding.svSearchGame.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.incSectionGames.lySectionGames.visible()
                binding.rvSearchGames.gone()
            } else {
                binding.incSectionGames.lySectionGames.gone()
                binding.rvSearchGames.visible()
            }
        }

        // Set up btn close search view
        binding.ivSearchClose.setOnClickListener {
            binding.svSearchGame.text?.clear()
            binding.svSearchGame.clearFocus()
            binding.ivSearchClose.gone()
        }

        // Detect if the user press back button to close the keyboard and clear the search view
        binding.svSearchGame.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                binding.svSearchGame.clearFocus()
                return@setOnKeyListener true
            }
            false
        }
    }

}