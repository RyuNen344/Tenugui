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

package io.github.ryunen344.tenugui.tests.rule

import android.app.UiAutomation
import android.graphics.Bitmap
import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.dropbox.differ.SimpleImageComparator
import com.dropbox.dropshots.Dropshots
import com.dropbox.dropshots.ThresholdValidator
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.math.roundToInt

class ScreenShotRule(
    val dropshots: Dropshots = Dropshots(
        filenameFunc = { it },
        imageComparator = SimpleImageComparator(maxDistance = 0.5f),
        resultValidator = ThresholdValidator(threshold = 0.007f), // 0.7%
    ),
) : TestRule {

    private lateinit var description: Description

    private val fileName: String
        get() = with(description) {
            buildString {
                append(className.split(".").lastOrNull() ?: "Test")
                append("_")
                append(methodName.replace(".", "_"))
                append("_")
                append("sdk")
                append(Build.VERSION.SDK_INT)
            }
        }

    val device: UiDevice by lazy { UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()) }

    val automation: UiAutomation by lazy { InstrumentationRegistry.getInstrumentation().uiAutomation }

    override fun apply(base: Statement, description: Description): Statement {
        this.description = description
        return dropshots.apply(base, description)
    }

    fun takeScreenshot(scale: Float = 1f) {
        try {
            takeInternal(scale)
        } finally {
            // retry once
            takeInternal(scale)
        }
    }

    private fun takeInternal(scale: Float) {
        device.waitForIdle()
        val screenshot = with(automation.takeScreenshot()) {
            Bitmap.createScaledBitmap(this, (scale * width).roundToInt(), (scale * height).roundToInt(), false)
        }
        AutoCloseable(screenshot::recycle).use {
            dropshots.assertSnapshot(screenshot, fileName)
        }
    }
}
