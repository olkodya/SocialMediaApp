package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class EventViewModel(private val repository: EventRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    init {
        repository.getEvent().onEach { events ->
            _uiState.update {
                it.copy(events = events)
            }

        }.launchIn(viewModelScope)
    }

    fun likeById(id: Long) {
        repository.likeById(id)
    }

    fun participateById(id: Long) {
        repository.participateById(id)
    }

    fun addEvent(content: String) {
        repository.addEvent(content)
    }

    fun deleteById(id: Long) {
        repository.deleteById(id)

    }
}