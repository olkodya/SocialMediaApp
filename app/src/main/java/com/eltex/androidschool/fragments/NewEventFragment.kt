package com.eltex.androidschool.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentNewEventBinding
import com.eltex.androidschool.repository.NetworkEventRepository
import com.eltex.androidschool.utils.toEditable
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.NewEventViewModel
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NewEventFragment : Fragment() {

    companion object {
        const val ARG_ID = "ARG_ID"
        const val ARG_CONTENT = "ARG_CONTENT"
    }

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()
    override fun onStart() {
        super.onStart()
        toolbarViewModel.showSave(true)

    }


    override fun onStop() {
        super.onStop()
        toolbarViewModel.showSave(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewEventBinding.inflate(inflater, container, false)


        val id = arguments?.getLong(ARG_ID) ?: 0L
        val content = arguments?.getString(ARG_CONTENT)
        binding.content.text = content?.toEditable()

        val viewModel by viewModels<NewEventViewModel>() {
            viewModelFactory {
                initializer {
                    NewEventViewModel(
                        repository = NetworkEventRepository(),
                        id = id,
                    )
                }
            }
        }


        toolbarViewModel.saveClicked
            .filter { it }
            .onEach {
                val newContent = binding.content.text?.toString().orEmpty()
                if (newContent.isNotBlank()) {
                    viewModel.save(newContent)
                    findNavController().navigateUp()
                } else {
                    requireContext().toast(R.string.event_empty_error, true)
                }

                toolbarViewModel.saveClicked(false)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        return binding.root
    }

}