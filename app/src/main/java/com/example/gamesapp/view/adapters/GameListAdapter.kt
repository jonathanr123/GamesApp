package com.example.gamesapp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gamesapp.R
import com.example.gamesapp.databinding.ItemGamesListBinding
import com.example.gamesapp.model.Games

class GameListAdapter : ListAdapter<Games, RecyclerView.ViewHolder> (DiffUtilCallback()) {

    private var onItemClicked: ((Games) -> Unit)? = null
    var isGamesTop: Boolean = false

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: ViewHolder = holder as ViewHolder
        viewHolder.bind(getItem(position), onItemClicked, position, isGamesTop)
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
            position: Int,
            isGamesTop: Boolean
        ) {
            with(binding) {
                ivGames.load(value.imageURL)

                /*
                * If the list is for the top games, then imageNumber is showed and change the src of the image
                * depending on the position of the game in the list.
                * */
                if (isGamesTop){
                    cvGames.setPadding(0, 8, 0, 8)
                    ivNumberTop.visibility = View.VISIBLE
                    when (position){
                        0 -> { ivNumberTop.load(R.drawable.ic_number_one) }
                        1 -> { ivNumberTop.load(R.drawable.ic_number_two) }
                        2 -> { ivNumberTop.load(R.drawable.ic_number_three) }
                        3 -> { ivNumberTop.load(R.drawable.ic_number_four) }
                        4 -> { ivNumberTop.load(R.drawable.ic_number_five) }
                        5 -> { ivNumberTop.load(R.drawable.ic_number_six) }
                        6 -> { ivNumberTop.load(R.drawable.ic_number_seven) }
                        7 -> { ivNumberTop.load(R.drawable.ic_number_eight) }
                        8 -> { ivNumberTop.load(R.drawable.ic_number_nine) }
                        9 -> { ivNumberTop.load(R.drawable.ic_number_ten) }
                    }
                }

                cvGames.setOnClickListener {
                    listener?.invoke(value)
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