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

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.test.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.github.ryunen344.tenugui.BottomSheetDialog
import io.github.ryunen344.tenugui.rememberBottomSheetBehaviorProperties
import io.github.ryunen344.tenugui.rememberBottomSheetDialogProperties
import io.github.ryunen344.tenugui.rememberBottomSheetDialogState
import io.github.ryunen344.tenugui.tests.rule.AnimationsRule
import io.github.ryunen344.tenugui.tests.rule.ScreenShotRule
import io.github.ryunen344.tenugui.tests.rule.SystemUiRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class HalfExpandedTest {

    @get:Rule(order = 0)
    val systemUiRule = SystemUiRule()

    @get:Rule(order = 1)
    val animationsRule = AnimationsRule(false)

    @get:Rule(order = 2)
    val screenShotRule = ScreenShotRule()

    @Test
    fun testDefaultActivity_givenGatherContent() = runAndroidComposeUiTest<EmptyDefaultActivity> {
        screenShotRule.device.waitForIdle()
        setContent {
            MaterialTheme {
                val dialogId = rememberSaveable { UUID.randomUUID().toString() }
                val state = rememberBottomSheetDialogState(
                    key = dialogId,
                    dialogProperties = rememberBottomSheetDialogProperties(key = dialogId),
                    behaviorProperties = rememberBottomSheetBehaviorProperties(key = dialogId),
                )

                BottomSheetDialog(
                    onDismissRequest = { },
                    dialogId = dialogId,
                    state = state,
                    content = GatherBottomSheetContent,
                )

                LaunchedEffect(key1 = Unit) { state.behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED) }
            }
        }
        mainClock.advanceTimeBy(3000L)
        screenShotRule.takeScreenshot()
    }

    @Test
    fun testDefaultActivity_givenGatherContent_whenHasEditText() = runAndroidComposeUiTest<EmptyDefaultActivity> {
        screenShotRule.device.waitForIdle()
        setContent {
            MaterialTheme {
                val dialogId = rememberSaveable { UUID.randomUUID().toString() }
                val state = rememberBottomSheetDialogState(
                    key = dialogId,
                    dialogProperties = rememberBottomSheetDialogProperties(key = dialogId),
                    behaviorProperties = rememberBottomSheetBehaviorProperties(key = dialogId),
                )

                BottomSheetDialog(
                    onDismissRequest = { },
                    dialogId = dialogId,
                    state = state,
                    content = GatherTextFieldBottomSheetContent,
                )

                LaunchedEffect(key1 = Unit) { state.behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED) }
            }
        }
        mainClock.advanceTimeBy(3000L)
        screenShotRule.takeScreenshot()
    }

    @Test
    fun testDefaultActivity_givenGatherContent_whenHasFocusedEditText() =
        runAndroidComposeUiTest<EmptyDefaultActivity> {
            screenShotRule.device.waitForIdle()
            setContent {
                MaterialTheme {
                    val dialogId = rememberSaveable { UUID.randomUUID().toString() }
                    val state = rememberBottomSheetDialogState(
                        key = dialogId,
                        dialogProperties = rememberBottomSheetDialogProperties(key = dialogId),
                        behaviorProperties = rememberBottomSheetBehaviorProperties(key = dialogId),
                    )

                    BottomSheetDialog(
                        onDismissRequest = { },
                        dialogId = dialogId,
                        state = state,
                        content = GatherFocusedTextFieldBottomSheetContent,
                    )

                    LaunchedEffect(key1 = Unit) { state.behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED) }
                }
            }
            mainClock.advanceTimeBy(3000L)
            screenShotRule.takeScreenshot()
        }

    @Test
    fun testDefaultActivity_givenSpreadContent() = runAndroidComposeUiTest<EmptyDefaultActivity> {
        screenShotRule.device.waitForIdle()
        setContent {
            MaterialTheme {
                val dialogId = rememberSaveable { UUID.randomUUID().toString() }
                val state = rememberBottomSheetDialogState(
                    key = dialogId,
                    dialogProperties = rememberBottomSheetDialogProperties(key = dialogId),
                    behaviorProperties = rememberBottomSheetBehaviorProperties(key = dialogId),
                )

                BottomSheetDialog(
                    onDismissRequest = { },
                    dialogId = dialogId,
                    state = state,
                    content = SpreadBottomSheetContent,
                )

                LaunchedEffect(key1 = Unit) { state.behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED) }
            }
        }
        mainClock.advanceTimeBy(3000L)
        screenShotRule.takeScreenshot()
    }

    @Test
    fun testDefaultActivity_givenSpreadContent_whenHasEditText() = runAndroidComposeUiTest<EmptyDefaultActivity> {
        screenShotRule.device.waitForIdle()
        setContent {
            MaterialTheme {
                val dialogId = rememberSaveable { UUID.randomUUID().toString() }
                val state = rememberBottomSheetDialogState(
                    key = dialogId,
                    dialogProperties = rememberBottomSheetDialogProperties(key = dialogId),
                    behaviorProperties = rememberBottomSheetBehaviorProperties(key = dialogId),
                )

                BottomSheetDialog(
                    onDismissRequest = { },
                    dialogId = dialogId,
                    state = state,
                    content = SpreadTextFieldBottomSheetContent,
                )

                LaunchedEffect(key1 = Unit) { state.behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED) }
            }
        }
        mainClock.advanceTimeBy(3000L)
        screenShotRule.takeScreenshot()
    }

    @Test
    fun testDefaultActivity_givenSpreadContent_whenHasFocusedEditText() =
        runAndroidComposeUiTest<EmptyDefaultActivity> {
            screenShotRule.device.waitForIdle()
            setContent {
                MaterialTheme {
                    val dialogId = rememberSaveable { UUID.randomUUID().toString() }
                    val state = rememberBottomSheetDialogState(
                        key = dialogId,
                        dialogProperties = rememberBottomSheetDialogProperties(key = dialogId),
                        behaviorProperties = rememberBottomSheetBehaviorProperties(key = dialogId),
                    )

                    BottomSheetDialog(
                        onDismissRequest = { },
                        dialogId = dialogId,
                        state = state,
                        content = SpreadFocusedTextFieldBottomSheetContent,
                    )

                    LaunchedEffect(key1 = Unit) { state.behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED) }
                }
            }
            mainClock.advanceTimeBy(3000L)
            screenShotRule.takeScreenshot()
        }

    @Test
    fun testEdgeToEdgeActivity_givenGatherContent() = runAndroidComposeUiTest<EmptyEdgeToEdgeActivity> {
        screenShotRule.device.waitForIdle()
        setContent {
            MaterialTheme {
                val dialogId = rememberSaveable { UUID.randomUUID().toString() }
                val state = rememberBottomSheetDialogState(
                    key = dialogId,
                    dialogProperties = rememberBottomSheetDialogProperties(
                        key = dialogId,
                        decorFitsSystemWindows = false,
                    ),
                    behaviorProperties = rememberBottomSheetBehaviorProperties(key = dialogId),
                )

                BottomSheetDialog(
                    onDismissRequest = { },
                    dialogId = dialogId,
                    state = state,
                    content = GatherBottomSheetContent,
                )

                LaunchedEffect(key1 = Unit) { state.behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED) }
            }
        }
        mainClock.advanceTimeBy(3000L)
        screenShotRule.takeScreenshot()
    }

    @Test
    fun testEdgeToEdgeActivity_givenGatherContent_whenHasEditText() = runAndroidComposeUiTest<EmptyEdgeToEdgeActivity> {
        screenShotRule.device.waitForIdle()
        setContent {
            MaterialTheme {
                val dialogId = rememberSaveable { UUID.randomUUID().toString() }
                val state = rememberBottomSheetDialogState(
                    key = dialogId,
                    dialogProperties = rememberBottomSheetDialogProperties(
                        key = dialogId,
                        decorFitsSystemWindows = false,
                    ),
                    behaviorProperties = rememberBottomSheetBehaviorProperties(key = dialogId),
                )

                BottomSheetDialog(
                    onDismissRequest = { },
                    dialogId = dialogId,
                    state = state,
                    content = GatherTextFieldBottomSheetContent,
                )

                LaunchedEffect(key1 = Unit) { state.behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED) }
            }
        }
        mainClock.advanceTimeBy(3000L)
        screenShotRule.takeScreenshot()
    }

    @Test
    fun testEdgeToEdgeActivity_givenGatherContent_whenHasFocusedEditText() =
        runAndroidComposeUiTest<EmptyEdgeToEdgeActivity> {
            screenShotRule.device.waitForIdle()
            setContent {
                MaterialTheme {
                    val dialogId = rememberSaveable { UUID.randomUUID().toString() }
                    val state = rememberBottomSheetDialogState(
                        key = dialogId,
                        dialogProperties = rememberBottomSheetDialogProperties(
                            key = dialogId,
                            decorFitsSystemWindows = false,
                        ),
                        behaviorProperties = rememberBottomSheetBehaviorProperties(key = dialogId),
                    )

                    BottomSheetDialog(
                        onDismissRequest = { },
                        dialogId = dialogId,
                        state = state,
                        content = GatherFocusedTextFieldBottomSheetContent,
                    )

                    LaunchedEffect(key1 = Unit) { state.behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED) }
                }
            }
            mainClock.advanceTimeBy(3000L)
            screenShotRule.takeScreenshot()
        }

    @Test
    fun testEdgeToEdgeActivity_givenSpreadContent() = runAndroidComposeUiTest<EmptyEdgeToEdgeActivity> {
        screenShotRule.device.waitForIdle()
        setContent {
            MaterialTheme {
                val dialogId = rememberSaveable { UUID.randomUUID().toString() }
                val state = rememberBottomSheetDialogState(
                    key = dialogId,
                    dialogProperties = rememberBottomSheetDialogProperties(
                        key = dialogId,
                        decorFitsSystemWindows = false,
                    ),
                    behaviorProperties = rememberBottomSheetBehaviorProperties(key = dialogId),
                )

                BottomSheetDialog(
                    onDismissRequest = { },
                    dialogId = dialogId,
                    state = state,
                    content = SpreadBottomSheetContent,
                )

                LaunchedEffect(key1 = Unit) { state.behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED) }
            }
        }
        mainClock.advanceTimeBy(3000L)
        screenShotRule.takeScreenshot()
    }

    @Test
    fun testEdgeToEdgeActivity_givenSpreadContent_whenHasEditText() = runAndroidComposeUiTest<EmptyEdgeToEdgeActivity> {
        screenShotRule.device.waitForIdle()
        setContent {
            MaterialTheme {
                val dialogId = rememberSaveable { UUID.randomUUID().toString() }
                val state = rememberBottomSheetDialogState(
                    key = dialogId,
                    dialogProperties = rememberBottomSheetDialogProperties(
                        key = dialogId,
                        decorFitsSystemWindows = false,
                    ),
                    behaviorProperties = rememberBottomSheetBehaviorProperties(key = dialogId),
                )

                BottomSheetDialog(
                    onDismissRequest = { },
                    dialogId = dialogId,
                    state = state,
                    content = SpreadTextFieldBottomSheetContent,
                )

                LaunchedEffect(key1 = Unit) { state.behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED) }
            }
        }
        mainClock.advanceTimeBy(3000L)
        screenShotRule.takeScreenshot()
    }

    @Test
    fun testEdgeToEdgeActivity_givenSpreadContent_whenHasFocusedEditText() =
        runAndroidComposeUiTest<EmptyEdgeToEdgeActivity> {
            screenShotRule.device.waitForIdle()
            setContent {
                MaterialTheme {
                    val dialogId = rememberSaveable { UUID.randomUUID().toString() }
                    val state = rememberBottomSheetDialogState(
                        key = dialogId,
                        dialogProperties = rememberBottomSheetDialogProperties(
                            key = dialogId,
                            decorFitsSystemWindows = false,
                        ),
                        behaviorProperties = rememberBottomSheetBehaviorProperties(key = dialogId),
                    )

                    BottomSheetDialog(
                        onDismissRequest = { },
                        dialogId = dialogId,
                        state = state,
                        content = SpreadFocusedTextFieldBottomSheetContent,
                    )

                    LaunchedEffect(key1 = Unit) { state.behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED) }
                }
            }
            mainClock.advanceTimeBy(3000L)
            screenShotRule.takeScreenshot()
        }
}
