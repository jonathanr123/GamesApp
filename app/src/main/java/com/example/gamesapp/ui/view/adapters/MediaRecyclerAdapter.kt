package com.example.gamesapp.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gamesapp.databinding.ItemMediaListBinding
import com.example.gamesapp.domain.model.MediaObject

class MediaRecyclerAdapter : ListAdapter<MediaObject, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: PlayerViewHolder = holder as PlayerViewHolder
        viewHolder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemMediaListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlayerViewHolder(binding)
    }

    class PlayerViewHolder(private val binding: ItemMediaListBinding) :
        RecyclerView.ViewHolder(binding.cvVideo) {

        internal fun bind(
            value: MediaObject
        ) {
            binding.cvVideo.tag = this
            with(binding) {
                tvTitle.text = value.title
                ivMediaCoverImage.load(value.coverUrl)
                tvDescription.text = value.description
                tvGenres.text = value.genres
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<MediaObject>(){
        override fun areItemsTheSame(oldItem: MediaObject, newItem: MediaObject): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MediaObject, newItem: MediaObject): Boolean {
            return oldItem.id == newItem.id
        }
    }
}