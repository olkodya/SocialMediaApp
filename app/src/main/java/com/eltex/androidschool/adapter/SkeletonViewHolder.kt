package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventSkeletonBinding

class SkeletonViewHolder(private val binding: CardEventSkeletonBinding) : ViewHolder(binding.root) {
    fun bind() {
        binding.author.text = binding.root.context.getString(R.string.default_author)
        binding.published.text = binding.root.context.getString(R.string.default_published_skeleton)
        binding.content.text = binding.root.context.getString(R.string.default_content_skeleton)
        binding.eventType.text = binding.root.context.getString(R.string.default_eventType_skeleton)
        binding.eventDate.text =
            binding.root.context.getString(R.string.default_event_date_skeleton)
        binding.like.text = binding.root.context.getString(R.string.default_likes_amount_skeleton)
        binding.eventLink.text = binding.root.context.getString(R.string.default_link_skeleton)
        binding.members.text =
            binding.root.context.getString(R.string.default_participants_amount_skeleton)
    }
}