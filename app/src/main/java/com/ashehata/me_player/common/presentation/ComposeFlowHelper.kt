package com.ashehata.me_player.common.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ashehata.me_player.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


@Composable
fun <State, VM : BaseViewModel<*, *, *>> GeneralObservers(
    viewModel: VM,
    current: (state: State) -> Unit
) {
    viewModel.state.CollectAsEffect {
        viewModel.consumeState()
        (it as? State)?.let { state ->
            current.invoke(state)
        }
    }
}

@Composable
fun <T> Flow<T>.CollectAsEffect(
    context: CoroutineContext = EmptyCoroutineContext,
    block: (T) -> Unit
) {
    LaunchedEffect(true) {
        onEach(block).flowOn(context).launchIn(this)
    }
}