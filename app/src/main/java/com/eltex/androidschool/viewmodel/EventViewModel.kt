package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EventViewModel(
    private val repository: EventRepository,
    private val mapper: EventUiModelMapper,
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(status = Status.Loading) }

        repository.getEvents()
            .map { events ->
                events.map {
                    mapper.map(it)
                }
            }
            .subscribeBy(
                onSuccess = { data ->
                    _uiState.update {
                        it.copy(events = data, status = Status.Idle)
                    }
                },

                onError = { throwable ->
                    _uiState.update {
                        it.copy(status = Status.Error(throwable))
                    }
                }
            )
            .addTo(disposable)
    }


    fun likeById(event: EventUiModel) {
        _uiState.update { it.copy(status = Status.Loading) }
        if (!event.likedByMe) {
            repository.likeById(event.id)
                .subscribeBy(
                    onSuccess = { data ->
                        _uiState.update { state ->
                            state.copy(
                                events = state.events.orEmpty().map {
                                    if (it.id == event.id) {
                                        mapper.map(data)
                                    } else {
                                        it
                                    }
                                },
                                status = Status.Idle,
                            )
                        }
                    },
                    onError = { throwable ->
                        _uiState.update {
                            it.copy(status = Status.Error(throwable))
                        }
                    }
                )
                .addTo(disposable)


        } else {
            repository.unLikeById(event.id)
                .subscribeBy(
                    onSuccess = { data ->
                        _uiState.update { state ->
                            state.copy(
                                events = state.events.orEmpty().map {
                                    if (it.id == event.id) {
                                        mapper.map(data)
                                    } else {
                                        it
                                    }
                                },
                                status = Status.Idle,
                            )
                        }
                    },
                    onError = { throwable ->
                        _uiState.update {
                            it.copy(status = Status.Error(throwable))
                        }
                    }
                )
                .addTo(disposable)

        }
    }

    fun participateById(event: EventUiModel) {
        _uiState.update { it.copy(status = Status.Loading) }
        if (!event.participatedByMe) {
            repository.participateById(event.id)
                .subscribeBy(
                    onSuccess = { data ->
                        _uiState.update { state ->
                            state.copy(
                                events = state.events.orEmpty().map {
                                    if (it.id == event.id) {
                                        mapper.map(data)
                                    } else {
                                        it
                                    }
                                },
                                status = Status.Idle,
                            )
                        }
                    },
                    onError = { throwable ->
                        _uiState.update {
                            it.copy(status = Status.Error(throwable))
                        }
                    }
                )
                .addTo(disposable)


        } else {
            repository.unParticipateById(event.id)
                .subscribeBy(
                    onSuccess = { data ->
                        _uiState.update { state ->
                            state.copy(
                                events = state.events.orEmpty().map {
                                    if (it.id == event.id) {
                                        mapper.map(data)
                                    } else {
                                        it
                                    }
                                },
                                status = Status.Idle,
                            )
                        }
                    },
                    onError = { throwable ->
                        _uiState.update {
                            it.copy(status = Status.Error(throwable))
                        }
                    }
                )
                .addTo(disposable)

        }
    }


    fun deleteById(id: Long) {
        _uiState.update { it.copy(status = Status.Loading) }

        repository.deleteById(id)
            .subscribeBy(
                onComplete = {
                    _uiState.update { state ->
                        state.copy(
                            events = state.events.orEmpty()
                                .filter { it.id != id },
                            status = Status.Idle
                        )
                    }
                },
                onError = { throwable ->
                    _uiState.update {
                        it.copy(status = Status.Error(throwable))
                    }
                }
            )
            .addTo(disposable)

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

    override fun onCleared() {
        disposable.dispose()
    }
}