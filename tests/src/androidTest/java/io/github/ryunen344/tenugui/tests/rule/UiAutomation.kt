/*
 * Copyright (C) 2017 The Android Open Source Project
 * Copyright (C) 2025-2025 RyuNen344
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

internal fun UiAutomation.configOverlay() {
    executeShellCommand("cmd overlay enable com.android.internal.systemui.navbar.gestural")
    executeShellCommand("cmd overlay enable com.android.internal.emulation.pixel_7")
    executeShellCommand("cmd overlay enable com.android.systemui.emulation.pixel_7")
}

internal fun UiAutomation.enableDemoMode() {
    executeShellCommand("settings put global sysui_demo_allowed 1")
    executeShellCommand("settings put global sysui_tuner_demo_on 1")
}

internal fun UiAutomation.disableDemoMode() {
    executeShellCommand("settings put global sysui_demo_allowed 0")
    executeShellCommand("settings put global sysui_tuner_demo_on 0")
}

internal fun UiAutomation.enableAnimations() {
    executeShellCommand("settings put global transition_animation_scale 1")
    executeShellCommand("settings put global window_animation_scale 1")
    executeShellCommand("settings put global animator_duration_scale 1")
}

internal fun UiAutomation.disableAnimations() {
    executeShellCommand("settings put global transition_animation_scale 0")
    executeShellCommand("settings put global window_animation_scale 0")
    executeShellCommand("settings put global animator_duration_scale 0")
}

internal class AnimationScales(
    val transition: Int,
    val window: Int,
    val animator: Int,
)

internal fun UiAutomation.saveAnimations(): AnimationScales {
    val transition = executeShellCommand("settings get global transition_animation_scale").result
    val window = executeShellCommand("settings get global window_animation_scale").result
    val animator = executeShellCommand("settings get global animator_duration_scale").result
    return AnimationScales(
        transition = transition,
        window = window,
        animator = animator,
    )
}

internal fun UiAutomation.restoreAnimations(scales: AnimationScales) {
    executeShellCommand("settings put global transition_animation_scale ${scales.transition}")
    executeShellCommand("settings put global window_animation_scale ${scales.window}")
    executeShellCommand("settings put global animator_duration_scale ${scales.animator}")
}
