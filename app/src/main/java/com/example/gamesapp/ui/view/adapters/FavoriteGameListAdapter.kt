package com.example.gamesapp.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gamesapp.databinding.ItemGamesListBinding
import com.example.gamesapp.domain.model.GamesItem
import com.example.gamesapp.utils.showModalBottomSheetGames

class FavoriteGameListAdapter : ListAdapter<GamesItem, RecyclerView.ViewHolder> (DiffUtilCallback()) {

    private var widthCard: Int = 180
    private var heightCard: Int = 250
    var manager: FragmentManager? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: ViewHolder = holder as ViewHolder
        viewHolder.bind(getItem(position), widthCard, heightCard, manager)
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
            value: GamesItem,
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
                    showModalBottomSheetGames(value.toModel(), binding.root.context, manager)
                }
            }
        }
    }


    class DiffUtilCallback : DiffUtil.ItemCallback<GamesItem>(){
        override fun areItemsTheSame(oldItem: GamesItem, newItem: GamesItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GamesItem, newItem: GamesItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
}