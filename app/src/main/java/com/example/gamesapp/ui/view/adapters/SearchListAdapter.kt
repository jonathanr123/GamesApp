package com.example.gamesapp.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gamesapp.R
import com.example.gamesapp.databinding.ItemSearchListBinding
import com.example.gamesapp.data.model.Games

class SearchListAdapter : ListAdapter<Games, RecyclerView.ViewHolder> (DiffUtilCallback()) {

    private var onItemClicked: ((Games) -> Unit)? = null
    var manager: FragmentManager? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: ViewHolder = holder as ViewHolder
        viewHolder.bind(getItem(position), onItemClicked, manager)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemSearchListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: ItemSearchListBinding) :
        RecyclerView.ViewHolder(binding.cvGameSearch) {

        internal fun bind(
            value: Games,
            listener: ((Games) -> Unit)?,
            manager: FragmentManager? = null
        ) {
            with(binding) {
                ivImageSearch.load(value.imageURL)
                tvTitleSearch.text = value.name
                tvDateSearch.text = value.released
                rbarRatingSearch.rating = value.rating!!

                cvGameSearch.setOnClickListener {
                    // Navigate to Game Detail Fragment with object games
                    val navController = manager?.findFragmentById(R.id.nav_host_fragment)?.findNavController()
                    navController?.navigate(R.id.GameDetailFragment, bundleOf("game" to value))
                }
            }
        }
    }


    class DiffUtilCallback : DiffUtil.ItemCallback<Games>(){
        override fun areItemsTheSame(oldItem: Games, newItem: Games): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Games, newItem: Games): Boolean {
            return oldItem.id == newItem.id
        }
    }
}