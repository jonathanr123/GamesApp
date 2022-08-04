package com.example.gamesapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gamesapp.databinding.ItemGenresListBinding
import com.example.gamesapp.model.Genres

class GenreListAdapter : ListAdapter<Genres, RecyclerView.ViewHolder> (DiffUtilCallback()) {

    private var onItemClicked: ((Genres) -> Unit)? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: ViewHolder = holder as ViewHolder
        viewHolder.bind(getItem(position), onItemClicked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemGenresListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: ItemGenresListBinding) :
        RecyclerView.ViewHolder(binding.cvGenres) {

        internal fun bind(
            value: Genres,
            listener: ((Genres) -> Unit)?,
        ) {
            with(binding) {
                ivGenres.load(value.imageURL)
                tvNameGenre.text = value.name

                cvGenres.setOnClickListener {
                    listener?.invoke(value)
                }
            }
        }
    }


    class DiffUtilCallback : DiffUtil.ItemCallback<Genres>(){
        override fun areItemsTheSame(oldItem: Genres, newItem: Genres): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Genres, newItem: Genres): Boolean {
            return oldItem.id == newItem.id
        }
    }
}