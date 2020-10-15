package io.zelbess.grpcandroid.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.zelbess.grpcandroid.R
import kotlinx.android.synthetic.main.item_feed.view.*


class FeedAdapter(
    var onMessageClicked: (Int) -> Unit
) : ListAdapter<FeedItem, FeedViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return FeedViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onMessageClicked)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<FeedItem>() {
            override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
                return oldItem.journeyId == newItem.journeyId
            }

            override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

data class FeedItem(
    val journeyId: Int,
    val message: String
)

class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: FeedItem, onMessageClicked: (Int) -> Unit) {
        itemView.apply {
            feedMessage.text = item.message
            setOnClickListener { onMessageClicked(item.journeyId) }
        }
    }
}
