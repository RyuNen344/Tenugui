/*
 * Copyright (C) 2017 The Android Open Source Project
 * Copyright (C) 2025 RyuNen344
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * License-Filename: LICENSE.md
 */

package io.github.ryunen344.tenugui

import android.view.View
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.semantics.dialog
import androidx.compose.ui.semantics.semantics
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.UUID

@Composable
public fun BottomSheetDialog(
    onDismissRequest: () -> Unit,
    dialogId: UUID = rememberSaveable { UUID.randomUUID() },
    state: BottomSheetDialogState = rememberBottomSheetDialogState(dialogId = dialogId),
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val composition = rememberCompositionContext()
    val currentOnDismissRequest by rememberUpdatedState(onDismissRequest)
    val currentContent by rememberUpdatedState(content)
    val dialog = remember(view, density) {
        BottomSheetDialogWrapper(
            currentOnDismissRequest,
            state.dialogProperties,
            view,
            layoutDirection,
            density,
            dialogId,
        ).apply {
            setContent(composition) {
                val draggable by state.behavior.getDraggable()
                BottomSheetDialogLayout(
                    modifier = Modifier
                        .then(
                            if (draggable) {
                                Modifier
                                    .nestedScroll(rememberNestedScrollInteropConnection())
                                    .scrollable(
                                        state = rememberScrollableState { 0f },
                                        orientation = Orientation.Vertical,
                                    )
                            } else {
                                Modifier
                            },
                        )
                        .semantics { dialog() },
                    content = currentContent,
                )
            }
        }
    }
    val saveInstanceStateCallback by remember(dialog) {
        mutableStateOf(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, @BottomSheetBehavior.State newState: Int) {
                    state.onSaveInstanceState(dialog)
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // noop
                }
            },
        )
    }

    DisposableEffect(dialog) {
        state.onRestoreInstanceState(dialog)
        state.setProperties(dialog.behavior)
        dialog.behavior.addBottomSheetCallback(saveInstanceStateCallback)
        dialog.show()

        onDispose {
            dialog.dismiss()
            dialog.behavior.removeBottomSheetCallback(saveInstanceStateCallback)
            state.dispose()
            dialog.disposeComposition()
        }
    }

    SideEffect {
        dialog.updateParameters(currentOnDismissRequest, state.dialogProperties, layoutDirection)
        state.onSaveInstanceState(dialog)
    }
}
