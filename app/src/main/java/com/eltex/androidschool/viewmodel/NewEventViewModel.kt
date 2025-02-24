package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewEventViewModel(
    private val repository: EventRepository,
    private val id: Long,
) : ViewModel() {

    private val _state = MutableStateFlow(NewEventUiState())
    val state = _state.asStateFlow()

    fun save(content: String, datetime: String) {

        viewModelScope.launch {
            try {
                val data = repository.saveEvent(id, content, datetime, _state.value.file)
                _state.update { it.copy(result = data, status = Status.Idle) }

            } catch (e: Exception) {
                _state.update { it.copy(status = Status.Error(e)) }

            }

        }
    }

    fun saveFile(fileModel: FileModel?) = _state.update {
        it.copy(file = fileModel)
    }

    fun handleError() {
        _state.update { it.copy(status = Status.Idle) }
    }

}