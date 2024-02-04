package com.eltex.androidschool.adapter

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Attachment
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

        if (payload.attachment != null) {
            updateAttachment(payload.attachment)
        }

        if (payload.authorAvatar != null) {
            updateAuthorAvatar(payload.authorAvatar)
        }
    }

    private fun updateAuthorAvatar(authorAvatar: String) {
        Glide.with(binding.avatar)
            .load(authorAvatar)
            .transform(CircleCrop())
            .into(binding.avatar)
    }

    private fun updateAttachment(attachment: Attachment) {
        binding.attachment.isVisible = true
        Glide.with(binding.attachment)
            .load(attachment.url)
            .into(binding.attachment)
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
        binding.eventDate.text = event.datetime
        binding.published.text = event.published
        updateLikeIcon(event.likedByMe)
        updateLikeNumber(event.likes)
        updateParticipateIcon(event.participatedByMe)
        updateParticipateNumber(event.participants)
        if (event.attachment != null) {
            binding.attachment.isVisible = true
            updateAttachment(event.attachment)
        } else {
            binding.attachment.isGone = true
        }
        if (event.authorAvatar != null) {
            updateAuthorAvatar(event.authorAvatar)
            binding.authorInitials.isGone = true
        } else {
            binding.authorInitials.isVisible = true
            binding.avatar.setImageResource(R.drawable.avatar_background)
        }
    }
}