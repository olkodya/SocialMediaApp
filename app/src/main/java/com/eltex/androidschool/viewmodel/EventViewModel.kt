package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.utils.Callback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EventViewModel(private val repository: EventRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(status = Status.Loading) }

        repository.getEvents(
            object : Callback<List<Event>> {
                override fun onSuccess(data: List<Event>) {
                    _uiState.update {
                        it.copy(events = data, status = Status.Idle)
                    }
                }

                override fun onError(throwable: Throwable) {
                    _uiState.update {
                        it.copy(status = Status.Error(Throwable()))
                    }
                }

            }
        )
    }


    fun likeById(event: Event) {
        _uiState.update { it.copy(status = Status.Loading) }

        if (!event.likedByMe) {
            repository.likeById(
                event.id,
                object : Callback<Event> {
                    override fun onSuccess(data: Event) {
                        _uiState.update { state ->
                            state.copy(
                                events = state.events.orEmpty()
                                    .map {
                                        if (it.id == event.id) {
                                            data
                                        } else {
                                            it
                                        }
                                    }, status = Status.Idle
                            )
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        _uiState.update {
                            it.copy(status = Status.Error(Throwable()))
                        }
                    }

                }
            )
        } else {
            repository.unLikeById(
                event.id,
                object : Callback<Event> {
                    override fun onSuccess(data: Event) {
                        _uiState.update { state ->
                            state.copy(
                                events = state.events.orEmpty()
                                    .map {
                                        if (it.id == event.id) {
                                            data
                                        } else {
                                            it
                                        }
                                    }, status = Status.Idle
                            )
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        _uiState.update {
                            it.copy(status = Status.Error(Throwable()))
                        }
                    }

                }
            )

        }
    }

    fun participateById(event: Event) {
        _uiState.update { it.copy(status = Status.Loading) }
        if (!event.participatedByMe) {
            repository.participateById(
                event.id,
                object : Callback<Event> {
                    override fun onSuccess(data: Event) {
                        _uiState.update { state ->
                            state.copy(
                                events = state.events.orEmpty()
                                    .map {
                                        if (it.id == event.id) {
                                            data
                                        } else {
                                            it
                                        }
                                    }, status = Status.Idle
                            )
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        _uiState.update {
                            it.copy(status = Status.Error(Throwable()))
                        }
                    }

                }
            )
        } else {
            repository.unParticipateById(
                event.id,
                object : Callback<Event> {
                    override fun onSuccess(data: Event) {
                        _uiState.update { state ->
                            state.copy(
                                events = state.events.orEmpty()
                                    .map {
                                        if (it.id == event.id) {
                                            data
                                        } else {
                                            it
                                        }
                                    }, status = Status.Idle
                            )
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        _uiState.update {
                            it.copy(status = Status.Error(Throwable()))
                        }
                    }

                }
            )
        }

    }


    fun deleteById(id: Long) {
        _uiState.update { it.copy(status = Status.Loading) }

        repository.deleteById(
            id,
            object : Callback<Unit> {
                override fun onSuccess(data: Unit) {
                    _uiState.update { state ->
                        state.copy(
                            events = state.events.orEmpty()
                                .filter { it.id != id },
                            status = Status.Idle
                        )
                    }
                }

                override fun onError(throwable: Throwable) {
                    _uiState.update {
                        it.copy(status = Status.Error(Throwable()))
                    }
                }

            }
        )
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