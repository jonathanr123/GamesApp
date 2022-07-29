package com.example.gamesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gamesapp.databinding.ItemCreatorListBinding
import com.example.gamesapp.model.Creators

class CreatorListAdapter() : ListAdapter<Creators, RecyclerView.ViewHolder> (DiffUtilCallback()) {

    private var onItemClicked: ((Creators) -> Unit)? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: ViewHolder = holder as ViewHolder
        viewHolder.bind(getItem(position), onItemClicked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCreatorListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: ItemCreatorListBinding) :
        RecyclerView.ViewHolder(binding.cvCreators) {

        internal fun bind(
            value: Creators,
            listener: ((Creators) -> Unit)?,
        ) {
            with(binding) {
                ivCreator.load(value.urlImage)
                tvNameCreator.text = value.name

                cvCreators.setOnClickListener {
                    listener?.invoke(value)
                }
            }
        }
    }


    class DiffUtilCallback : DiffUtil.ItemCallback<Creators>(){
        override fun areItemsTheSame(oldItem: Creators, newItem: Creators): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Creators, newItem: Creators): Boolean {
            return oldItem.id == newItem.id
        }
    }
}