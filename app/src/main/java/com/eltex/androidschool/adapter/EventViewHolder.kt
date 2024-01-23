package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.EventUiModel

class EventViewHolder(private val binding: CardEventBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindEvent(payload: EventPayload) {
        if (payload.liked != null) {
            updateLikeIcon(payload.liked)
        }

        if (payload.likes != null) {
            updateLikeNumber(payload.likes)
        }
        if (payload.participated != null) {
            updateParticipateIcon(payload.participated)
        }
        if (payload.participants != null) {
            updateParticipateNumber(payload.participants)
        }
    }

    private fun updateParticipateIcon(participatedByMe: Boolean) {
        binding.members.setIconResource(
            if (participatedByMe) {
                R.drawable.baseline_group_24
            } else {
                R.drawable.outline_group_24
            }
        )
    }

    private fun updateParticipateNumber(participants: Int) {
        binding.members.text = participants.toString()
    }

    private fun updateLikeIcon(likedByMe: Boolean) {
        binding.like.setIconResource(
            if (likedByMe) {
                R.drawable.baseline_favorite_24
            } else {
                R.drawable.baseline_favorite_border_24
            }
        )
    }

    private fun updateLikeNumber(likes: Int) {
        binding.like.text = likes.toString()
    }


    fun bindEvent(
        event: EventUiModel
    ) {
        binding.author.text = event.author
        binding.content.text = event.content
        binding.published.text = event.published
        binding.authorInitials.text = event.author.take(1)
//        binding.eventType.text = event.type.type
        binding.eventDate.text = event.datetime
        binding.published.text = event.published
//        binding.eventLink.text = event.link
        updateLikeIcon(event.likedByMe)
        updateLikeNumber(event.likes)
        updateParticipateIcon(event.participatedByMe)
        updateParticipateNumber(event.participants)
    }
}