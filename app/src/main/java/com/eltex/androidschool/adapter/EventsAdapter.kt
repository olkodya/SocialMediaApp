package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.utils.toast

class EventsAdapter(
    private val likeClickListener: (Event) -> Unit,
    private val participateClickListener: (Event) -> Unit
) :
    ListAdapter<Event, EventViewHolder>(EventItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val eventBinding = CardEventBinding.inflate(inflater, parent, false)
        val viewHolder = EventViewHolder(eventBinding)
        eventBinding.like.setOnClickListener {
            likeClickListener(getItem(viewHolder.adapterPosition))
        }
        eventBinding.members.setOnClickListener {
            participateClickListener(getItem(viewHolder.adapterPosition))
        }

        eventBinding.share.setOnClickListener {
            parent.context.toast(R.string.not_implemented)
        }

        eventBinding.menu.setOnClickListener {
            parent.context.toast(R.string.not_implemented)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindEvent(getItem(position))
    }

    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            payloads.forEach {
                if (it is EventPayload)
                    holder.bindEvent(it)
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

}