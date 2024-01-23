package com.eltex.androidschool.viewmodel

import org.junit.Test

class EventViewModelTest {
    @Test
    fun `deleteById error then error in state`() {

//        val testError = RuntimeException("Test error")
//        val viewModel = EventViewModel(
//            repository = object : EventRepository {
//                override suspend fun getEvents(): List<Event> = Single.just(emptyList())
//
//                override suspend fun likeById(id: Long): Event = Single.never()
//
//                override fun participateById(id: Long): Single<Event> = Single.never()
//
//                override fun unLikeById(id: Long): Single<Event> = Single.never()
//
//                override fun unParticipateById(id: Long): Single<Event> = Single.never()
//
//                override fun saveEvent(id: Long, content: String, datetime: String): Single<Event> =
//                    Single.never()
//
//                override fun deleteById(id: Long): Completable = Completable.error(testError)
//            },
//            EventUiModelMapper(),
//            schedulersFactory = TestSchedulersFactory(),
//        )
//
//        viewModel.deleteById(121)
//
//        assertEquals(testError, (viewModel.uiState.value.status as Status.Error).reason)
    }

    @Test
    fun `deleteById successful`() {

    }
}