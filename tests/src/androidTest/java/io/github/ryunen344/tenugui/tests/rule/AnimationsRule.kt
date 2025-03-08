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

import android.os.ParcelFileDescriptor
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.FileInputStream
import java.io.InputStream

class AnimationsRule(private val animate: Boolean) : TestRule {

    private val ParcelFileDescriptor.result: Int
        get() = FileInputStream(fileDescriptor).buffered().use(InputStream::readBytes)
            .decodeToString().trimIndent().toFloat().toInt()

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                val current = saveAnimations()
                if (animate) {
                    enableAnimations()
                } else {
                    disableAnimations()
                }
                try {
                    base.evaluate()
                } finally {
                    restoreAnimations(current)
                }
            }
        }
    }

    private fun enableAnimations() {
        with(InstrumentationRegistry.getInstrumentation().uiAutomation) {
            executeShellCommand("settings put global transition_animation_scale 1")
            executeShellCommand("settings put global window_animation_scale 1")
            executeShellCommand("settings put global animator_duration_scale 1")
        }
    }

    private fun disableAnimations() {
        with(InstrumentationRegistry.getInstrumentation().uiAutomation) {
            executeShellCommand("settings put global transition_animation_scale 0")
            executeShellCommand("settings put global window_animation_scale 0")
            executeShellCommand("settings put global animator_duration_scale 0")
        }
    }

    private fun saveAnimations(): Scales {
        return with(InstrumentationRegistry.getInstrumentation().uiAutomation) {
            val transition = executeShellCommand("settings get global transition_animation_scale").result
            val window = executeShellCommand("settings get global window_animation_scale").result
            val animator = executeShellCommand("settings get global animator_duration_scale").result
            Scales(
                transition = transition,
                window = window,
                animator = animator,
            )
        }
    }

    private fun restoreAnimations(scales: Scales) {
        with(InstrumentationRegistry.getInstrumentation().uiAutomation) {
            executeShellCommand("settings put global transition_animation_scale ${scales.transition}")
            executeShellCommand("settings put global window_animation_scale ${scales.window}")
            executeShellCommand("settings put global animator_duration_scale ${scales.animator}")
        }
    }

    private class Scales(
        val transition: Int,
        val window: Int,
        val animator: Int,
    )
}
