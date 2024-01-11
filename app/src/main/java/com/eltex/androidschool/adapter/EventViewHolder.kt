package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Event

class EventViewHolder(val binding: CardEventBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindEvent(payload: EventPayload) {
        if (payload.liked != null) {
            updateLike(payload.liked)
        }
        if (payload.participated != null) {
            updateParticipate(payload.participated)
        }
    }

    private fun updateParticipate(participatedByMe: Boolean) {
        binding.members.setIconResource(
            if (participatedByMe) {
                R.drawable.baseline_group_24
            } else {
                R.drawable.outline_group_24
            }
        )
        binding.members.text = if (participatedByMe) {
            1
        } else {
            0
        }.toString()
    }

    private fun updateLike(likedByMe: Boolean) {
        binding.like.setIconResource(
            if (likedByMe) {
                R.drawable.baseline_favorite_24
            } else {
                R.drawable.baseline_favorite_border_24
            }
        )

        binding.like.text = if (likedByMe) {
            1
        } else {
            0
        }.toString()
    }

    fun bindEvent(
        event: Event
    ) {
        binding.author.text = event.author
        binding.content.text = event.content
        binding.published.text = event.published
        binding.authorInitials.text = event.author.take(1)
//        binding.eventType.text = event.type.type
        binding.eventDate.text = event.datetime
        binding.published.text = event.published
//        binding.eventLink.text = event.link
        updateLike(event.likedByMe)
        updateParticipate(event.participatedByMe)
    }
}