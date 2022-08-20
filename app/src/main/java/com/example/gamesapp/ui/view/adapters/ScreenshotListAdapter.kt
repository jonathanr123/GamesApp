package com.example.gamesapp.ui.view.adapters

import com.example.gamesapp.databinding.ItemScreenshotListBinding
import com.example.gamesapp.data.model.ShortScreenshot
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gamesapp.utils.showDialogScreenshot

class ScreenshotListAdapter : ListAdapter<ShortScreenshot, RecyclerView.ViewHolder> (DiffUtilCallback()) {

    private var onItemClicked: ((ShortScreenshot) -> Unit)? = null
    private var widthCard: Int = 160
    private var heightCard: Int = 110

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: ViewHolder = holder as ViewHolder
        viewHolder.bind(getItem(position), onItemClicked, position, widthCard, heightCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemScreenshotListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: ItemScreenshotListBinding) :
        RecyclerView.ViewHolder(binding.cvScreenshot) {

        internal fun bind(
            value: ShortScreenshot,
            listener: ((ShortScreenshot) -> Unit)?,
            position: Int,
            widthCard: Int,
            heightCard: Int
        ) {
            with(binding) {
                ivScreenshot.load(value.image)

                // Resize the card view depending the parameters received
                cardScreenshot.layoutParams.width =
                    (binding.root.context.resources.displayMetrics.density * widthCard).toInt()
                ivScreenshot.layoutParams.height =
                    (binding.root.context.resources.displayMetrics.density * heightCard).toInt()

                cardScreenshot.setOnClickListener {
                    showDialogScreenshot(value, binding.root.context)
                }
            }
        }
    }


    class DiffUtilCallback : DiffUtil.ItemCallback<ShortScreenshot>(){
        override fun areItemsTheSame(oldItem: ShortScreenshot, newItem: ShortScreenshot): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ShortScreenshot, newItem: ShortScreenshot): Boolean {
            return oldItem.id == newItem.id
        }
    }
}