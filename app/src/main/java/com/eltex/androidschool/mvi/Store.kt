package com.eltex.androidschool.mvi

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

class Store<State, Message, Effect>(
    private val reducer: Reducer<State, Effect, Message>,
    private val effectHandler: EffectHandler<Effect, Message>,
    private val initialMessage: Set<Message> = emptySet(),
    initialState: State
) {

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val messages = MutableSharedFlow<Message>(extraBufferCapacity = 64)
    private val effects = MutableSharedFlow<Effect>(extraBufferCapacity = 64)

    fun accept(message: Message) {
        messages.tryEmit(message)
    }

    suspend fun connect() = coroutineScope {
        launch {
            effectHandler.connect(effects)
                .collect {
                    messages.tryEmit(it)
                }
        }

        launch {
            listOf(messages, initialMessage.asFlow())
                .merge()
                .map {
                    reducer.reduce(_state.value, it)
                }
                .collect {
                    _state.value = it.state
                    it.effects.forEach(effects::tryEmit)
                }
        }
    }


}