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

import android.app.Application
import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.runAndroidComposeUiTest
import androidx.compose.ui.test.runComposeUiTest
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.robolectric.Shadows.shadowOf

fun runSuspendComposeUiTest(block: ComposeUiTest.(scope: TestScope) -> Unit) = runTest {
    val appContext: Application = ApplicationProvider.getApplicationContext()
    val activityInfo = ActivityInfo().apply {
        name = ComponentActivity::class.java.name
        packageName = appContext.packageName
    }
    shadowOf(appContext.packageManager).addOrUpdateActivity(activityInfo)
    runComposeUiTest(
        effectContext = coroutineContext,
        block = { block(this@runTest) },
    )
}

inline fun <reified A : ComponentActivity> runSuspendAndroidComposeUiTest(
    noinline block: AndroidComposeUiTest<A>.(scope: TestScope) -> Unit,
) = runTest {
    val appContext: Application = ApplicationProvider.getApplicationContext()
    val activityInfo = ActivityInfo().apply {
        name = A::class.java.name
        packageName = appContext.packageName
    }
    shadowOf(appContext.packageManager).addOrUpdateActivity(activityInfo)
    runAndroidComposeUiTest<A>(
        effectContext = coroutineContext,
        block = { block(this@runTest) },
    )
}

fun <A : ComponentActivity> runSuspendAndroidComposeUiTest(
    activityClass: Class<A>,
    block: AndroidComposeUiTest<A>.(scope: TestScope) -> Unit,
) = runTest {
    val appContext: Application = ApplicationProvider.getApplicationContext()
    val activityInfo = ActivityInfo().apply {
        name = activityClass.name
        packageName = appContext.packageName
    }
    shadowOf(appContext.packageManager).addOrUpdateActivity(activityInfo)
    runAndroidComposeUiTest(
        activityClass = activityClass,
        effectContext = coroutineContext,
        block = { block(this@runTest) },
    )
}
