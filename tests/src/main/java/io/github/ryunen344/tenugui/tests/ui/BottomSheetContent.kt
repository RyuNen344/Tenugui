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

package io.github.ryunen344.tenugui.tests.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.github.ryunen344.tenugui.BottomSheetDialog
import io.github.ryunen344.tenugui.rememberBottomSheetDialogState
import kotlinx.coroutines.delay
import java.util.UUID

private const val TAG = "BottomSheetContent"

@Composable
fun BottomSheetContent(decorFitsSystemWindows: Boolean) {
    var visible by rememberSaveable { mutableStateOf(false) }
    var text by rememberSaveable { mutableStateOf("false") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .background(Color.Green)
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Text(
                modifier = Modifier
                    .background(Color.Magenta)
                    .fillMaxWidth()
                    .padding(16.dp),
                text = "window top",
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                onClick = {
                    visible = true
                },
            ) {
                Text(text = "Toggle Compose")
            }

            Spacer(modifier = Modifier.weight(1f))


            Text(
                modifier = Modifier
                    .background(Color.Magenta)
                    .fillMaxWidth()
                    .padding(16.dp),
                text = "window bottom",
            )
        }
    }

    if (visible) {
        val dialogId = rememberSaveable { UUID.randomUUID().toString() }
        LocalDensity.current
        val bottomSheetDialogState =
            rememberBottomSheetDialogState(
                key = dialogId,
                decorFitsSystemWindows = decorFitsSystemWindows,
            )

        val behaviorState by bottomSheetDialogState.behavior.getState()

        LaunchedEffect(behaviorState) {
            Log.e("BehaviorState", "BehaviorState is $behaviorState")
        }

        LaunchedEffect(key1 = bottomSheetDialogState) {
            delay(3000)
            bottomSheetDialogState.behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED)
        }

        BottomSheetDialog(
            onDismissRequest = {
                visible = false
            },
            dialogId = dialogId,
            state = bottomSheetDialogState,
        ) {
            Column(
                modifier = Modifier
                    .background(Color.Cyan)
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .imePadding(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "first item",
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "first item",
                    )
                }

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = text,
                    onValueChange = {
                        text = it
                    },
                )

                repeat(33) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "column $it",
                    )
                }

                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "last item",
                )
            }
        }
    }
}
