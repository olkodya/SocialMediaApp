package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.MainDispatcherRule
import com.eltex.androidschool.TestEventRepository
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class EventViewModelTest {

    private companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `deleteById error then error in state`() {

        val testError = RuntimeException("Test error")
        val viewModel = EventViewModel(
            repository = object : TestEventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()
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
            repository = object : TestEventRepository {
                override suspend fun getEvents(): List<Event> = events
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
        val testEvents = Event(id = 3L, content = "event3", likedByMe = true)
        val expected = EventUiModel(
            id = 3L, content = "event3", likedByMe = true,
            published = FORMATTER.format(
                Instant.now().atZone(
                    ZoneId.systemDefault()
                )
            ),
        )
        val viewModel = EventViewModel(
            repository = object : TestEventRepository {
                override suspend fun getEvents(): List<Event> = listOf(event3)
                override suspend fun likeById(id: Long): Event = testEvents
            },
            EventUiModelMapper(),
        )
        viewModel.likeById(EventUiModelMapper().map(event3))
        val result = viewModel.uiState.value.events?.find { it.id == expected.id } ?: EventUiModel()
        assertEquals(expected, result)
        assertEquals(Status.Idle, viewModel.uiState.value.status)
    }

    @Test
    fun `likeById error then error in state`() {
        val event3 = Event(id = 3L, content = "event3", likedByMe = false)
        val testError = RuntimeException("Test error")
        val viewModel = EventViewModel(
            repository = object : TestEventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()
                override suspend fun likeById(id: Long): Event = throw testError
            },
            EventUiModelMapper(),
        )
        viewModel.likeById(EventUiModelMapper().map(event3))
        assertEquals(testError, (viewModel.uiState.value.status as Status.Error).reason)
    }

    @Test
    fun `unlikeById correct`() {
        val event3 = Event(id = 3L, content = "event3", likedByMe = true)
        val testEvent = Event(id = 3L, content = "event3", likedByMe = false)
        val expected = EventUiModel(
            id = 3L, content = "event3", likedByMe = false, published = FORMATTER.format(
                Instant.now().atZone(
                    ZoneId.systemDefault()
                )
            )
        )
        val viewModel = EventViewModel(
            repository = object : TestEventRepository {
                override suspend fun getEvents(): List<Event> = listOf(event3)
                override suspend fun unLikeById(id: Long): Event = testEvent
            },
            EventUiModelMapper(),
        )
        viewModel.likeById(EventUiModelMapper().map(event3))
        val result = viewModel.uiState.value.events?.find { it.id == expected.id } ?: EventUiModel()
        assertEquals(expected, result)
        assertEquals(Status.Idle, viewModel.uiState.value.status)
    }

    @Test
    fun `unlikeById error then error in state`() {
        val event3 = Event(id = 3L, content = "event3", likedByMe = true)
        val testError = RuntimeException("Test error")
        val viewModel = EventViewModel(
            repository = object : TestEventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()

                override suspend fun unLikeById(id: Long): Event = throw testError
            },
            EventUiModelMapper(),
        )
        viewModel.likeById(EventUiModelMapper().map(event3))
        assertEquals(testError, (viewModel.uiState.value.status as Status.Error).reason)
    }

    @Test
    fun `participateById correct`() {
        val event3 = Event(id = 3L, content = "event3", participatedByMe = false)
        val testEvent = Event(id = 3L, content = "event3", participatedByMe = true)
        val expected = EventUiModel(
            id = 3L, content = "event3", participatedByMe = true, published = FORMATTER.format(
                Instant.now().atZone(
                    ZoneId.systemDefault()
                )
            )
        )
        val viewModel = EventViewModel(
            repository = object : TestEventRepository {
                override suspend fun getEvents(): List<Event> = listOf(event3)

                override suspend fun participateById(id: Long): Event = testEvent

            },
            EventUiModelMapper(),
        )
        viewModel.participateById(EventUiModelMapper().map(event3))
        val result = viewModel.uiState.value.events?.find { it.id == expected.id } ?: EventUiModel()
        assertEquals(expected, result)
        assertEquals(Status.Idle, viewModel.uiState.value.status)
    }

    @Test
    fun `participateById error then error in state`() {
        val event3 = Event(id = 3L, content = "event3", participatedByMe = false)
        val testError = RuntimeException("Test error")
        val viewModel = EventViewModel(
            repository = object : TestEventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()

                override suspend fun participateById(id: Long): Event = throw testError

            },
            EventUiModelMapper(),
        )
        viewModel.participateById(EventUiModelMapper().map(event3))
        assertEquals(testError, (viewModel.uiState.value.status as Status.Error).reason)
    }

    @Test
    fun `unParticipateById correct`() {
        val event3 = Event(id = 3L, content = "event3", participatedByMe = true)
        val testEvent = Event(id = 3L, content = "event3", participatedByMe = false)
        val expected = EventUiModel(
            id = 3L, content = "event3", participatedByMe = false, published = FORMATTER.format(
                Instant.now().atZone(
                    ZoneId.systemDefault()
                )
            )
        )
        val viewModel = EventViewModel(
            repository = object : TestEventRepository {
                override suspend fun getEvents(): List<Event> = listOf(event3)

                override suspend fun unParticipateById(id: Long): Event = testEvent

            },
            EventUiModelMapper(),
        )
        viewModel.participateById(EventUiModelMapper().map(event3))
        val result = viewModel.uiState.value.events?.find { it.id == expected.id } ?: EventUiModel()
        assertEquals(expected, result)
        assertEquals(Status.Idle, viewModel.uiState.value.status)
    }

    @Test
    fun `unParticipateById error then error in state`() {
        val event3 = Event(id = 3L, content = "event3", participatedByMe = true)
        val testError = RuntimeException("Test error")
        val viewModel = EventViewModel(
            repository = object : TestEventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()

                override suspend fun unParticipateById(id: Long): Event = throw testError

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
            repository = object : TestEventRepository {
                override suspend fun getEvents(): List<Event> = events
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
            repository = object : TestEventRepository {
                override suspend fun getEvents(): List<Event> = throw testError
            },
            EventUiModelMapper(),
        )
        viewModel.load()
        assertEquals(testError, (viewModel.uiState.value.status as Status.Error).reason)
    }

}