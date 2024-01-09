package com.eltex.androidschool.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.EventsAdapter
import com.eltex.androidschool.databinding.FragmentEventsBinding
import com.eltex.androidschool.db.AppDb
import com.eltex.androidschool.itemdecoration.OffsetDecoration
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.repository.RoomEventsRepository
import com.eltex.androidschool.viewmodel.EventViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment


        val viewModel by viewModels<EventViewModel> {
            viewModelFactory {
                initializer {
                    EventViewModel(
                        RoomEventsRepository(
                            AppDb.getInstance(
                                requireContext().applicationContext
                            ).eventDao
                        )
                    )
                }
            }
        }


        val binding = FragmentEventsBinding.inflate(inflater, container, false)


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
                    requireContext()
                    findNavController()
                        .navigate(
                            R.id.action_eventsFragment4_to_newEventFragment
                        )
                }

//                override fun onEditClickListener(event: Event) {
//                    val intent = Intent(this@MainActivity, EditEventActivity::class.java)
//                        .setAction(Intent.ACTION_SEND)
//                        .putExtra(
//                            Intent.EXTRA_TEXT,
//                            event.content
//                        ).putExtra(ID, event.id)
//                    editEventContract.launch(intent)
//                }

            }
        )

        binding.list.adapter = adapter

        binding.list.addItemDecoration(OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing)))
        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                adapter.submitList(it.events)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root

    }
}


