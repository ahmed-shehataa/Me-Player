package com.ashehata.me_player.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

interface BaseEvent

interface BaseState

abstract class BaseViewState {
    val isNetworkError: MutableState<Boolean> = mutableStateOf(false)
    val isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
}