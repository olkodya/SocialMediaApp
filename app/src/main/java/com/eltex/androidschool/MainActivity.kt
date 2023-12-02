package com.eltex.androidschool

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.repository.InMemoryEventRepository
import com.eltex.androidschool.viewmodel.EventViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {



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
            .onEach {
                binding.container.removeAllViews()
                val events = it.events
                events.forEach { event ->
                    val eventBinding =
                        CardEventBinding.inflate(layoutInflater, binding.container, true)
                    bindEvent(eventBinding, event)
                    eventBinding.like.setOnClickListener {
                        viewModel.likById(event.id)
                    }
                }
            }
            .launchIn(lifecycleScope)

//        binding.like.setOnClickListener {
//            viewModel.like()
//
//        }
//
//        binding.members.setOnClickListener {
//            viewModel.participate()
//        }
//
//        binding.share.setOnClickListener {
//            toast(R.string.not_implemented)
//        }
//
//        binding.menu.setOnClickListener {
//            toast(R.string.not_implemented)
//        }

    }
}