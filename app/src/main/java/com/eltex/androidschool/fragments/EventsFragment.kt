package com.eltex.androidschool.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.EventsAdapter
import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.databinding.FragmentEventsBinding
import com.eltex.androidschool.effecthandler.EventEffectHandler
import com.eltex.androidschool.itemdecoration.OffsetDecoration
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.EventUiState
import com.eltex.androidschool.mvi.Store
import com.eltex.androidschool.reducer.EventReducer
import com.eltex.androidschool.repository.NetworkEventRepository
import com.eltex.androidschool.utils.getText
import com.eltex.androidschool.viewmodel.EventViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val viewModel by viewModels<EventViewModel> {
            viewModelFactory {
                initializer {
                    EventViewModel(

                        Store(
                            EventReducer(),
                            EventEffectHandler(NetworkEventRepository(EventsApi.INSTANCE)),
                            setOf(EventMessage.Refresh),
                            EventUiState(),
                        ),

                        )
                }
            }
        }


        val binding = FragmentEventsBinding.inflate(inflater, container, false)


        val adapter = EventsAdapter(
            object : EventsAdapter.EventListener {
                override fun onLikeClickListener(event: EventUiModel) {
                    viewModel.accept(EventMessage.Like(event))
                }

                override fun onShareClickListener(event: EventUiModel) {
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

                override fun onDeleteClickListener(event: EventUiModel) {
                    viewModel.accept(EventMessage.Delete(event))
                }

                override fun onParticipateClickListener(event: EventUiModel) {
                    viewModel.accept(EventMessage.Participate(event))
                }

                override fun onEditClickListener(event: EventUiModel) {
                    requireParentFragment()
                        .requireParentFragment()
                        .findNavController()
                        .navigate(
                            R.id.action_bottomNavigationFragment_to_editEventFragment,
                            bundleOf(
                                NewEventFragment.ARG_ID to event.id,
                                NewEventFragment.ARG_CONTENT to event.content
                            ),
                        )
                }
            }
        )

        binding.list.adapter = adapter

        binding.list.addItemDecoration(OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing)))


        binding.swipeRefresh.setOnRefreshListener {
            viewModel.accept(EventMessage.Refresh)
        }

        binding.retryButton.setOnClickListener {
            viewModel.accept(EventMessage.Refresh)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewEventFragment.EVENT_UPDATED,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.accept(EventMessage.Refresh)
        }

        binding.list.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                val count = adapter.itemCount
                val position = binding.list.getChildAdapterPosition(view)
                if (position != count - 3) return
                viewModel.accept(EventMessage.LoadNextPage)
            }

            override fun onChildViewDetachedFromWindow(view: View) = Unit
        }
        )


        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                binding.swipeRefresh.isRefreshing = state.isRefreshing

                val emptyError = state.emptyError
                binding.errorGroup.isVisible = emptyError != null
                binding.errorText.text = emptyError?.getText(requireContext())

                binding.progress.isVisible = state.isEmptyLoading

                state.singleError?.let {
                    Toast.makeText(
                        requireContext(),
                        it.getText(requireContext()),
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.accept(EventMessage.HandleError)
                }

                adapter.submitList(state.events)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root

    }
}


