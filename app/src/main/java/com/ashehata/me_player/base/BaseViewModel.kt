package com.ashehata.me_player.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashehata.me_player.base.BaseEvent
import com.ashehata.me_player.base.BaseState
import com.ashehata.me_player.base.BaseViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel<Event : BaseEvent, ViewState : BaseViewState, State : BaseState>
    : ViewModel() {

    private val initialState: State? by lazy { createInitialState() }

    private val initialViewState: ViewState? by lazy { createInitialViewState() }

    val viewStates: ViewState? by lazy {
        createInitialViewState()
    }

    private val _state = MutableStateFlow(initialState)
    val state get() = _state.asStateFlow().filterNotNull()

    private val _event = MutableSharedFlow<Event>()
    private val event get() = _event.asSharedFlow()

    private val defaultExceptionHandler = exceptionHandler {
        Log.i("defaultExceptionHandler", it.localizedMessage)
        handleCustomNetworkError()
    }

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            event.collectLatest {
                handleEvents(it)
            }
        }
    }

    fun setEvent(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    /**
     * Set new Ui State
     */
    fun setState(builder: () -> State?) {
        viewModelScope.launch {
            _state.emit(builder())
        }
    }

    open fun createInitialState(): State? {
        return null
    }

    open fun createInitialViewState(): ViewState? {
        return null
    }

    open fun resetViewStates() {}

    abstract fun handleEvents(event: Event)
    fun consumeState() {
        setState { null }
    }

    protected fun launchCoroutine(
        customContext: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch(defaultExceptionHandler + customContext) {
            block()
        }
    }

    private fun handleCustomNetworkError() {
        viewStates?.isNetworkError?.value = true
        viewStates?.isLoading?.value = false
        viewStates?.isRefreshing?.value = false
    }

    protected fun exceptionHandler(onCatch: ((Throwable) -> Unit)): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, exception ->
            onCatch(exception)
        }
    }

    fun setLoading() {
        viewStates?.isLoading?.value = true
        viewStates?.isNetworkError?.value = false
    }

    fun setDoneLoading() {
        viewStates?.isLoading?.value = false
        viewStates?.isRefreshing?.value = false
    }

}
