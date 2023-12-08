package com.eltex.androidschool

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.adapter.EventsAdapter
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.itemdecoration.OffsetDecoration
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
        val adapter = EventsAdapter(
            likeClickListener = {
                viewModel.likeById(it.id)
            },
            participateClickListener = {
                viewModel.participateById(it.id)

            },
            shareClickListener = { event ->
                val intent = Intent()
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(
                        Intent.EXTRA_TEXT,
                        getString(R.string.share_text, event.author, event.content)
                    )
                    .setType("text/plain")

                val chooser = Intent.createChooser(intent, null)
                startActivity(chooser)
            }
        )

        binding.root.adapter = adapter

        binding.root.addItemDecoration(OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing)))
        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                adapter.submitList(it.events)
            }
            .launchIn(lifecycleScope)

//        binding.share.setOnClickListener {
//            toast(R.string.not_implemented)
//        }
//
//        binding.menu.setOnClickListener {
//            toast(R.string.not_implemented)
//        }

    }
}