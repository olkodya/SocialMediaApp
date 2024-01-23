package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventViewModel(
    private val repository: EventRepository,
    private val mapper: EventUiModelMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(status = Status.Loading) }
        viewModelScope.launch {
            try {
                val events: List<EventUiModel> = repository.getEvents().map {
                    mapper.map(it)
                }

                _uiState.update {
                    it.copy(events = events, status = Status.Idle)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(status = Status.Error(e))
                }
            }

        }

    }


    fun likeById(event: EventUiModel) {
        _uiState.update { it.copy(status = Status.Loading) }
        viewModelScope.launch {
            try {
                val result = if (!event.likedByMe) {
                    repository.likeById(event.id)
                } else {
                    repository.unLikeById(event.id)
                }
                val uiModel = mapper.map(result)
                _uiState.update { state ->
                    state.copy(
                        events = state.events.orEmpty().map {
                            if (it.id == event.id) {
                                uiModel
                            } else {
                                it
                            }
                        },
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(status = Status.Error(e))
                }
            }

        }
    }

    fun participateById(event: EventUiModel) {
        _uiState.update { it.copy(status = Status.Loading) }
        viewModelScope.launch {
            try {
                val result = if (!event.participatedByMe) {
                    repository.participateById(event.id)
                } else {
                    repository.unParticipateById(event.id)
                }
                val uiModel = mapper.map(result)
                _uiState.update { state ->
                    state.copy(
                        events = state.events.orEmpty().map {
                            if (it.id == event.id) {
                                uiModel
                            } else {
                                it
                            }
                        },
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(status = Status.Error(e))
                }
            }

        }
    }


    fun deleteById(id: Long) {
        _uiState.update { it.copy(status = Status.Loading) }
        viewModelScope.launch {
            try {
                repository.deleteById(id)

                _uiState.update { state ->
                    state.copy(
                        events = state.events.orEmpty()
                            .filter { it.id != id },
                        status = Status.Idle
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(status = Status.Error(e))
                }
            }

        }
    }


    fun handleError() {
        _uiState.update {
            if (it.status is Status.Error) {
                it.copy(status = Status.Idle)
            } else {
                it
            }
        }
    }

}