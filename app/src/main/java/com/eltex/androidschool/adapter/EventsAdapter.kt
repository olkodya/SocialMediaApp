package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Event

class EventsAdapter(private val likeClickListener: (Event) -> Unit) :
    ListAdapter<Event, EventViewHolder>(EventItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val eventBinding = CardEventBinding.inflate(inflater, parent, false)
        val viewHolder = EventViewHolder(eventBinding)
        eventBinding.like.setOnClickListener {
            likeClickListener(getItem(viewHolder.adapterPosition))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindEvent(getItem(position))
    }

}