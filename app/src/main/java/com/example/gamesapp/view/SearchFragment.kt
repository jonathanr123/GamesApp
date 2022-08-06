package com.example.gamesapp.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamesapp.databinding.FragmentSearchBinding
import com.example.gamesapp.utils.gone
import com.example.gamesapp.utils.visible
import com.example.gamesapp.view.adapters.AllGamesListAdapter
import com.example.gamesapp.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by activityViewModels()

    private lateinit var rvAllGames: RecyclerView
    private val adapterAllGames = AllGamesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            setUpRecyclerViewAllGames()
        }

        return binding.root
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
        rvAllGames.layoutManager = layoutManager
        rvAllGames.adapter = adapterAllGames
        searchViewModel.allGames.collectLatest { pagingData ->
            adapterAllGames.submitData(pagingData)
        }
        binding.incSectionGames.lySectionGames.visible()
        binding.incSectionGames.tvTitleSectionGames.gone()
    }

}