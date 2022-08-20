package com.example.gamesapp.ui.view.adapters

import com.example.gamesapp.data.model.Games

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gamesapp.databinding.ItemGamesListBinding
import com.example.gamesapp.utils.showModalBottomSheetGames

class AllGamesListAdapter : PagingDataAdapter<Games, RecyclerView.ViewHolder> (DiffUtilCallback()) {

    private var onItemClicked: ((Games) -> Unit)? = null
    private var widthCard: Int = 120
    private var heightCard: Int = 170
    var manager: FragmentManager? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: ViewHolder = holder as ViewHolder
        getItem(position)?.let { viewHolder.bind(it, onItemClicked, widthCard, heightCard, manager) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemGamesListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: ItemGamesListBinding) :
        RecyclerView.ViewHolder(binding.cvGames) {

        internal fun bind(
            value: Games,
            listener: ((Games) -> Unit)?,
            widthCard: Int,
            heightCard: Int,
            manager: FragmentManager? = null
        ) {
            with(binding) {
                ivGames.load(value.imageURL)

                // Resize the card view depending the parameters received
                cardGames.layoutParams.width =
                    (binding.root.context.resources.displayMetrics.density * widthCard).toInt()
                ivGames.layoutParams.height =
                    (binding.root.context.resources.displayMetrics.density * heightCard).toInt()

                cardGames.setOnClickListener {
                    showModalBottomSheetGames(value, binding.root.context, manager)
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