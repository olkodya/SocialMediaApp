package com.eltex.androidschool

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.repository.InMemoryEventRepository
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.EventViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private fun bindEvent(binding: EventBinding, event: Event) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = EventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<EventViewModel> {
            viewModelFactory {
                initializer { EventViewModel(InMemoryEventRepository()) }
            }
        }

        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach { bindEvent(binding, it.event) }
            .launchIn(lifecycleScope)

        binding.like.setOnClickListener {
            viewModel.like()

        }

        binding.members.setOnClickListener {
            viewModel.participate()
        }

        binding.share.setOnClickListener {
            toast(R.string.not_implemented)
        }

        binding.menu.setOnClickListener {
            toast(R.string.not_implemented)
        }

    }
}