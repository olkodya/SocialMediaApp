package com.eltex.androidschool

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.adapter.EventsAdapter
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
        val adapter = EventsAdapter {
            viewModel.likById(it.id)

        }

        binding.root.adapter = adapter
        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                adapter.submitList(it.events)
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