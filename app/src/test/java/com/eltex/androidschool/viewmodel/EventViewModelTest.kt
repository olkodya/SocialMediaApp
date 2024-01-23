package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.MainDispatcherRule
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class EventViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `deleteById error then error in state`() {

        val testError = RuntimeException("Test error")
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()
                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unLikeById(id: Long): Event = error("Not implemented")

                override suspend fun unParticipateById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String, datetime: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = throw testError
            },
            EventUiModelMapper()
        )

        viewModel.deleteById(121)

        assertEquals(testError, (viewModel.uiState.value.status as Status.Error).reason)
    }

    @Test
    fun `deleteById correct`() {
        val event1 = Event(id = 1L, content = "event1")
        val event2 = Event(id = 2L, content = "event2")
        val event3 = Event(id = 3L, content = "event3")
        val events = mutableListOf(event1, event2, event3)
        val expected: List<EventUiModel> =
            listOf(EventUiModelMapper().map(event1), EventUiModelMapper().map(event3))
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = events
                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unLikeById(id: Long): Event = error("Not implemented")

                override suspend fun unParticipateById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String, datetime: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) {
                    events.removeIf { it.id == id }
                }
            },
            EventUiModelMapper(),
        )
        viewModel.deleteById(2L)
        val result: List<EventUiModel> = viewModel.uiState.value.events ?: emptyList()
        assertEquals(expected, result)
    }

    @Test
    fun `likeById correct`() {
        val event3 = Event(id = 3L, content = "event3", likedByMe = false)
        val expected = Event(id = 3L, content = "event3", likedByMe = true)
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = listOf(event3)
                override suspend fun likeById(id: Long): Event = expected

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unLikeById(id: Long): Event = error("Not implemented")

                override suspend fun unParticipateById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String, datetime: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")
            },
            EventUiModelMapper(),
        )
        viewModel.likeById(EventUiModelMapper().map(event3))
        val result = viewModel.uiState.value.events?.find { it.id == expected.id } ?: EventUiModel()
        assertEquals(EventUiModelMapper().map(expected), result)
        assertEquals(Status.Idle, viewModel.uiState.value.status)
    }

    @Test
    fun `likeById error then error in state`() {
        val event3 = Event(id = 3L, content = "event3", likedByMe = false)
        val testError = RuntimeException("Test error")
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()
                override suspend fun likeById(id: Long): Event = throw testError

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unLikeById(id: Long): Event = error("Not implemented")

                override suspend fun unParticipateById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String, datetime: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")
            },
            EventUiModelMapper(),
        )
        viewModel.likeById(EventUiModelMapper().map(event3))
        assertEquals(testError, (viewModel.uiState.value.status as Status.Error).reason)
    }

    @Test
    fun `unlikeById correct`() {
        val event3 = Event(id = 3L, content = "event3", likedByMe = true)
        val expected = Event(id = 3L, content = "event3", likedByMe = false)
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = listOf(event3)
                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unLikeById(id: Long): Event = expected

                override suspend fun unParticipateById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String, datetime: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")
            },
            EventUiModelMapper(),
        )
        viewModel.likeById(EventUiModelMapper().map(event3))
        val result = viewModel.uiState.value.events?.find { it.id == expected.id } ?: EventUiModel()
        assertEquals(EventUiModelMapper().map(expected), result)
        assertEquals(Status.Idle, viewModel.uiState.value.status)
    }

    @Test
    fun `unlikeById error then error in state`() {
        val event3 = Event(id = 3L, content = "event3", likedByMe = true)
        val testError = RuntimeException("Test error")
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()
                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unLikeById(id: Long): Event = throw testError

                override suspend fun unParticipateById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String, datetime: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")
            },
            EventUiModelMapper(),
        )
        viewModel.likeById(EventUiModelMapper().map(event3))
        assertEquals(testError, (viewModel.uiState.value.status as Status.Error).reason)
    }

    @Test
    fun `participateById correct`() {
        val event3 = Event(id = 3L, content = "event3", participatedByMe = false)
        val expected = Event(id = 3L, content = "event3", participatedByMe = true)
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = listOf(event3)
                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun participateById(id: Long): Event = expected

                override suspend fun unLikeById(id: Long): Event = error("Not implemented")

                override suspend fun unParticipateById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String, datetime: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")
            },
            EventUiModelMapper(),
        )
        viewModel.participateById(EventUiModelMapper().map(event3))
        val result = viewModel.uiState.value.events?.find { it.id == expected.id } ?: EventUiModel()
        assertEquals(EventUiModelMapper().map(expected), result)
        assertEquals(Status.Idle, viewModel.uiState.value.status)
    }

    @Test
    fun `participateById error then error in state`() {
        val event3 = Event(id = 3L, content = "event3", participatedByMe = false)
        val testError = RuntimeException("Test error")
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()
                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun participateById(id: Long): Event = throw testError

                override suspend fun unLikeById(id: Long): Event = error("Not implemented")

                override suspend fun unParticipateById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String, datetime: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")
            },
            EventUiModelMapper(),
        )
        viewModel.participateById(EventUiModelMapper().map(event3))
        assertEquals(testError, (viewModel.uiState.value.status as Status.Error).reason)
    }

    @Test
    fun `unParticipateById correct`() {
        val event3 = Event(id = 3L, content = "event3", participatedByMe = true)
        val expected = Event(id = 3L, content = "event3", participatedByMe = false)
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = listOf(event3)
                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unLikeById(id: Long): Event = error("Not implemented")

                override suspend fun unParticipateById(id: Long): Event = expected

                override suspend fun saveEvent(id: Long, content: String, datetime: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")
            },
            EventUiModelMapper(),
        )
        viewModel.participateById(EventUiModelMapper().map(event3))
        val result = viewModel.uiState.value.events?.find { it.id == expected.id } ?: EventUiModel()
        assertEquals(EventUiModelMapper().map(expected), result)
        assertEquals(Status.Idle, viewModel.uiState.value.status)
    }

    @Test
    fun `unParticipateById error then error in state`() {
        val event3 = Event(id = 3L, content = "event3", participatedByMe = true)
        val testError = RuntimeException("Test error")
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()
                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unLikeById(id: Long): Event = error("Not implemented")

                override suspend fun unParticipateById(id: Long): Event = throw testError

                override suspend fun saveEvent(id: Long, content: String, datetime: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")
            },
            EventUiModelMapper(),
        )
        viewModel.participateById(EventUiModelMapper().map(event3))
        assertEquals(testError, (viewModel.uiState.value.status as Status.Error).reason)
    }

    @Test
    fun `load correct`() {
        val event1 = Event(id = 1L, content = "event1")
        val event2 = Event(id = 2L, content = "event2")
        val event3 = Event(id = 3L, content = "event3")
        val events = mutableListOf(event1, event2, event3)
        val expected: List<EventUiModel> =
            listOf(
                EventUiModelMapper().map(event1),
                EventUiModelMapper().map(event2),
                EventUiModelMapper().map(event3)
            )
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = events
                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unLikeById(id: Long): Event = error("Not implemented")

                override suspend fun unParticipateById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String, datetime: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) {
                    events.removeIf { it.id == id }
                }
            },
            EventUiModelMapper(),
        )
        viewModel.load()
        val result: List<EventUiModel> = viewModel.uiState.value.events ?: emptyList()
        assertEquals(expected, result)
    }

    @Test
    fun `load error then error in state`() {
        val testError = RuntimeException("Test error")
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = throw testError
                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unLikeById(id: Long): Event = error("Not implemented")

                override suspend fun unParticipateById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String, datetime: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")
            },
            EventUiModelMapper(),
        )
        viewModel.load()
        assertEquals(testError, (viewModel.uiState.value.status as Status.Error).reason)
    }

}