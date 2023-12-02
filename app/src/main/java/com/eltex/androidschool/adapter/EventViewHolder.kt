package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Event

class EventViewHolder(val binding: CardEventBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindEvent(event: Event) {
        binding.author.text = event.author
        binding.content.text = event.content
        binding.published.text = event.published
        binding.authorInitials.text = event.author.take(1)
        binding.eventType.text = event.type.type
        binding.eventDate.text = event.datetime
        binding.published.text = event.published
        binding.eventLink.text = event.link
        binding.like.setIconResource(
            if (event.likedByMe) {
                R.drawable.baseline_favorite_24
            } else {
                R.drawable.baseline_favorite_border_24
            }
        )
        binding.members.setIconResource(
            if (event.participatedByMe) {
                R.drawable.baseline_group_24
            } else {
                R.drawable.outline_group_24
            }
        )
        binding.like.text = if (event.likedByMe) {
            1
        } else {
            0
        }.toString()

        binding.members.text = if (event.participatedByMe) {
            1
        } else {
            0
        }.toString()
    }
}