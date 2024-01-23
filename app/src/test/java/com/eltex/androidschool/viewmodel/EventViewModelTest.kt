package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.TestSchedulersFactory
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import junit.framework.TestCase.assertEquals
import org.junit.Test

class EventViewModelTest {
    @Test
    fun `deleteById error then error in state`() {

        val testError = RuntimeException("Test error")
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override fun getEvents(): Single<List<Event>> = Single.just(emptyList())

                override fun likeById(id: Long): Single<Event> = Single.never()

                override fun participateById(id: Long): Single<Event> = Single.never()

                override fun unLikeById(id: Long): Single<Event> = Single.never()

                override fun unParticipateById(id: Long): Single<Event> = Single.never()

                override fun saveEvent(id: Long, content: String, datetime: String): Single<Event> =
                    Single.never()

                override fun deleteById(id: Long): Completable = Completable.error(testError)
            },
            EventUiModelMapper(),
            schedulersFactory = TestSchedulersFactory(),
        )

        viewModel.deleteById(121)

        assertEquals(testError, (viewModel.uiState.value.status as Status.Error).reason)
    }

    @Test
    fun `deleteById successful`() {

    }
}