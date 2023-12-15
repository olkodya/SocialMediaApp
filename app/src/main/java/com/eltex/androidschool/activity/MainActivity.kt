package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.EventsAdapter
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.db.AppDb
import com.eltex.androidschool.itemdecoration.OffsetDecoration
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.repository.SqliteEventsRepository
import com.eltex.androidschool.viewmodel.EventViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {
    private companion object {
        const val ID = "ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = EventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<EventViewModel> {
            viewModelFactory {
                initializer {
                    EventViewModel(
                        SqliteEventsRepository(
                            AppDb.getInstance(
                                applicationContext
                            ).eventDao
                        )
                    )
                }
            }
        }

        val newEventContract =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val content = it.data?.getStringExtra(Intent.EXTRA_TEXT)
                if (content != null) {
                    viewModel.addEvent(content)
                }
            }

        binding.newEvent.setOnClickListener {
            val intent = Intent(this, NewEventActivity::class.java)
            newEventContract.launch(intent)
        }

        val editEventContract =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val id = it.data?.getLongExtra(ID, 0)
                val content = it.data?.getStringExtra(Intent.EXTRA_TEXT)
                if (content != null && id != null) {
                    viewModel.editById(id, content)
                }
            }

        if (intent.action == Intent.ACTION_SEND) {
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)
            intent.removeExtra(Intent.EXTRA_TEXT)
            if (!text.isNullOrBlank()) {
                newEventContract.launch(
                    Intent(this, NewEventActivity::class.java)
                        .putExtra(Intent.EXTRA_TEXT, text)
                )
            }
        }


        val adapter = EventsAdapter(
            object : EventsAdapter.EventListener {
                override fun onLikeClickListener(event: Event) {
                    viewModel.likeById(event.id)
                }

                override fun onShareClickListener(event: Event) {
                    val intent = Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(
                            Intent.EXTRA_TEXT,
                            event.content
                        )
                        .setType("text/plain")

                    val chooser = Intent.createChooser(intent, null)
                    startActivity(chooser)
                }

                override fun onDeleteClickListener(event: Event) {
                    viewModel.deleteById(event.id)
                }

                override fun onParticipateClickListener(event: Event) {
                    viewModel.participateById(event.id)
                }

                override fun onEditClickListener(event: Event) {
                    val intent = Intent(this@MainActivity, EditEventActivity::class.java)
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(
                            Intent.EXTRA_TEXT,
                            event.content
                        ).putExtra(ID, event.id)
                    editEventContract.launch(intent)
                }

            }
        )

        binding.list.adapter = adapter

        binding.list.addItemDecoration(OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing)))
        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                adapter.submitList(it.events)
            }
            .launchIn(lifecycleScope)

    }
}