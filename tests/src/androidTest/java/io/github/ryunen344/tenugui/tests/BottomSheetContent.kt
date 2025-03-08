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

package io.github.ryunen344.tenugui.tests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

internal val GatherBottomSheetContent: @Composable () -> Unit = {
    Column(
        modifier = Modifier
            .background(Color.Cyan)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "first item",
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = "column 0",
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = "last item",
        )
    }
}

internal val GatherTextFieldBottomSheetContent: @Composable () -> Unit = {
    var text by rememberSaveable { mutableStateOf("text field") }

    Column(
        modifier = Modifier
            .background(Color.Cyan)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = text,
            onValueChange = {
                text = it
            },
        )

        Text(
            modifier = Modifier.padding(16.dp),
            text = "first item",
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = "column 0",
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = "last item",
        )
    }
}

internal val GatherFocusedTextFieldBottomSheetContent: @Composable () -> Unit = {
    val focusRequester = remember { FocusRequester() }
    var text by rememberSaveable { mutableStateOf("text field") }

    Column(
        modifier = Modifier
            .background(Color.Cyan)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
    ) {
        TextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .padding(16.dp),
            value = text,
            onValueChange = {
                text = it
            },
        )

        Text(
            modifier = Modifier.padding(16.dp),
            text = "first item",
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = "column 0",
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = "last item",
        )

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

internal val SpreadBottomSheetContent: @Composable () -> Unit = {
    Column(
        modifier = Modifier
            .background(Color.Cyan)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "first item",
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

internal val SpreadTextFieldBottomSheetContent: @Composable () -> Unit = {
    var text by rememberSaveable { mutableStateOf("text field") }

    Column(
        modifier = Modifier
            .background(Color.Cyan)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = text,
            onValueChange = {
                text = it
            },
        )

        Text(
            modifier = Modifier.padding(16.dp),
            text = "first item",
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

internal val SpreadFocusedTextFieldBottomSheetContent: @Composable () -> Unit = {
    val focusRequester = remember { FocusRequester() }
    var text by rememberSaveable { mutableStateOf("text field") }

    Column(
        modifier = Modifier
            .background(Color.Cyan)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
    ) {
        TextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .padding(16.dp),
            value = text,
            onValueChange = {
                text = it
            },
        )

        Text(
            modifier = Modifier.padding(16.dp),
            text = "first item",
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

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}
