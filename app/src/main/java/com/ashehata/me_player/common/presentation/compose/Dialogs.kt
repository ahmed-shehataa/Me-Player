package com.ashehata.me_player.common.presentation.compose

import androidx.annotation.StringRes
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties

@Composable
fun AlertDialog(
    state: MutableState<Boolean>,
    @StringRes title: Int,
    @StringRes content: Int,
    @StringRes positiveTitleRes: Int,
    @StringRes negativeTitleRes: Int,
    positive: (() -> Unit)? = null,
    negative: (() -> Unit)? = null,
    isCancellable: Boolean = true
) {

    if (state.value) {
        AlertDialog(
            properties = DialogProperties(
                dismissOnClickOutside = isCancellable,
            ),
            onDismissRequest = {
                state.value = false
            },
            title = {
                Text(text = stringResource(id = title))
            },
            text = {
                Text(text = stringResource(id = content))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (positive != null) {
                            positive()
                        }
                        state.value = false
                    }
                ) {
                    Text(text = stringResource(id = positiveTitleRes))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if (negative != null) {
                            negative()
                        }
                        state.value = false
                    }
                ) {
                    Text(text = stringResource(id = negativeTitleRes))
                }
            }
        )
    }
}
