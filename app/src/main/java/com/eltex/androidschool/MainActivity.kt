package com.eltex.androidschool

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventType
import com.eltex.androidschool.utils.toast

class MainActivity : AppCompatActivity() {

    private fun bindEvent(binding: EventBinding, event: Event) {
        binding.author.text = event.author
        binding.content.text = event.content
        binding.published.text = event.published
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
            if (event.likedByMe) {
                R.drawable.people
            } else {
                R.drawable.people
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = EventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var event = Event(
            id = 1L,
            content = "Приглашаю провести уютный вечер за увлекательными играми! У нас есть несколько вариантов настолок, подходящих для любой компании.",
            author = "Lydia Westervelt",
            published = "11.05.22 11:21",
            type = EventType.OFFLINE,
            datetime = "16.05.22 12:00",
            likedByMe = false,
            participatedByMe = false,
            link = "https://m2.material.io/components/cards",
        )
        bindEvent(binding, event)

        binding.like.setOnClickListener {
            event = event.copy(likedByMe = !event.likedByMe)
            bindEvent(binding, event)

        }

        binding.members.setOnClickListener {
            event = event.copy(participatedByMe = !event.participatedByMe)
            bindEvent(binding, event)
        }

        binding.share.setOnClickListener {
            toast(R.string.not_implemented)
        }

        binding.members.setOnClickListener {
            toast(R.string.not_implemented)
        }

    }
}