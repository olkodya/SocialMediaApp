package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.EventStatus
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.EventUiState
import com.eltex.androidschool.model.PagingModel
import com.eltex.androidschool.reducer.EventReducer

class EventPagingModelMapper {

    fun map(eventUiState: EventUiState): List<PagingModel<EventUiModel>> {
        val events = eventUiState.events.map {
            PagingModel.Data(it)
        }
        val skeletonsList = List(EventReducer.PAGE_SIZE) {
            PagingModel.Skeleton
        }

        return when (val status = eventUiState.status) {
            is EventStatus.NextPageError -> events + PagingModel.Error(status.reason)
            EventStatus.NextPageLoading -> events + skeletonsList
            else -> events
        }
    }
}