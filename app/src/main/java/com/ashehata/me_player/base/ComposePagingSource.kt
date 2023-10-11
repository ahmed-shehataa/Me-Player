package com.ashehata.me_player.base

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.ashehata.me_player.common.models.PagingSetup
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

abstract class ComposePagingSource<T>(private val pagingSetup: PagingSetup = PagingSetup()) {
    companion object {
        const val FIRST_PAGE_NUMBER = 1
    }

    private var currentPage = 0

    /**
     *
     */
    private val _state = MutableStateFlow(PagingState.IDLE)
    val state: StateFlow<PagingState> = _state

    /**
     *
     */
    private val _list: SnapshotStateList<T> = mutableStateListOf()
    val list: List<T> = _list

    /**
     *
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.i("ComposePagingSource", ": "+ exception.localizedMessage)
        if (currentPage == FIRST_PAGE_NUMBER)
            _state.value = PagingState.FAILURE_AT_FIRST
        else
            _state.value = PagingState.FAILURE_AT_NEXT
    }
    private val coroutineScope = CoroutineScope(Dispatchers.IO + exceptionHandler)


    init {
        loadNextPage()
    }

    fun loadNextPage(isRetry: Boolean = false) {
        Log.i("loadNextPage", "out: " + _state.value.name)
        if (_state.value == PagingState.LOADING_FIRST_PAGE
            || _state.value == PagingState.LOADING_NEXT_PAGE
            || _state.value == PagingState.REACHED_LAST_PAGE
        ) return

        coroutineScope.launch {
            if (isRetry.not())
                currentPage++
            Log.i("loadNextPage", "currentPage: $currentPage")

            _state.value =
                if (currentPage == FIRST_PAGE_NUMBER) PagingState.LOADING_FIRST_PAGE else PagingState.LOADING_NEXT_PAGE

            // for testing purpose
            //delay(2.seconds)

            val result = loadPage(page = currentPage, perPage = pagingSetup.pageSize)
            withContext(Dispatchers.Main) {
                _list.addAll(
                    result
                )
                _state.value = if (result.isEmpty() || result.size < pagingSetup.pageSize)
                    PagingState.REACHED_LAST_PAGE
                else PagingState.IDLE
            }

        }
    }

    fun retry() {
        loadNextPage(isRetry = true)
    }

    fun refresh() {
        clearAll()
        resetToInitial()
        loadNextPage()
    }

    private fun resetToInitial() {
        _state.value = PagingState.IDLE
        currentPage = 0
    }

    fun updateItem(oldItem: T, newItem: T) {
        val itemIndex = _list.indexOf(oldItem)
        _list[itemIndex] = newItem
    }

    fun removeItem(item: T) {
        _list.remove(item)
    }

    fun addItem(item: T) {
        _list.add(item)
    }

    fun addHeader(item: T) {
        _list.add(0, item)
    }

    private fun clearAll() {
        _list.clear()
    }

    protected abstract suspend fun loadPage(page: Int, perPage: Int): List<T>
}

enum class PagingState {
    IDLE,
    LOADING_FIRST_PAGE,
    LOADING_NEXT_PAGE,
    FAILURE_AT_FIRST,
    FAILURE_AT_NEXT,
    REACHED_LAST_PAGE
}