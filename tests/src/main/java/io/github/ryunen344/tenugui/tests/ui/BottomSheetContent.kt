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
import android.widget.EditText
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.github.ryunen344.tenugui.BottomSheetDialog
import io.github.ryunen344.tenugui.rememberBottomSheetDialogState
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(decorFitsSystemWindows: Boolean) {
    var visible by rememberSaveable { mutableStateOf(false) }


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
        val dialogId = rememberSaveable { UUID.randomUUID() }
        val bottomSheetDialogState = rememberBottomSheetDialogState(decorFitsSystemWindows = decorFitsSystemWindows)

//        Dialog(
//            onDismissRequest = { visible = false },
//            content = Content,
//        )
//
        BottomSheetDialog(
            onDismissRequest = {
                visible = false
            },
            dialogId = dialogId,
            state = bottomSheetDialogState,
            content = Content,
        )
//
//        ModalBottomSheet(
//            onDismissRequest = {
//                visible = false
//            },
//            content = {
//                Content()
//            },
//        )
    }
}

private val Content: @Composable () -> Unit = {
    var text by rememberSaveable { mutableStateOf("false") }
    Column(
        modifier = Modifier
            .background(Color.Cyan)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .wrapContentHeight(),
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

        AndroidView(
            factory = ::EditText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            it.setText(text)
        }

        TextField(
            modifier = Modifier
                .pointerInput(UUID.randomUUID()) {
                    detectTapGestures {
                        Log.wtf("TextField", "pointerInput $size")
                        Log.wtf("TextField", "detectTapGestures $it")
                    }
                }
                .fillMaxWidth()
                .padding(16.dp),
            value = text,
            onValueChange = {
                text = it
            },
        )

        @Suppress("MagicNumber")
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
