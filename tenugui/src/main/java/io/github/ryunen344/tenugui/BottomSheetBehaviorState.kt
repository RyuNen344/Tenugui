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

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.Px
import androidx.compose.runtime.FloatState
import androidx.compose.runtime.IntState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Stable
public class BottomSheetBehaviorState(
    private val properties: BottomSheetBehaviorProperties,
    private val behavior: () -> ListenableBottomSheetBehavior?,
) {
    private val mutex = Mutex()

    /**
     * [BottomSheetBehavior.state]
     *
     * default [BottomSheetBehavior.STATE_HIDDEN]
     * @return [BottomSheetBehavior.State]
     */
    public fun getState(): IntState {
        return behavior()?.snapshotState ?: mutableIntStateOf(BottomSheetBehavior.STATE_HIDDEN)
    }

    /**
     * [BottomSheetBehavior.state]
     *
     * wraps [BottomSheetBehavior.setState]
     * @param state [BottomSheetBehavior.State]
     */
    public suspend fun setState(@BottomSheetBehavior.StableState state: Int) {
        behavior()?.let {
            // delay for internal layout calculation
            @Suppress("MagicNumber")
            delay(250)
            mutex.withLock {
                it.state = state
            }
        }
    }

    /**
     * [BottomSheetBehavior.fitToContents]
     *
     * default true
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_fitToContents
     */
    public fun getFitToContents(): State<Boolean> {
        return behavior()?.snapshotFitToContents ?: mutableStateOf(properties.fitToContents)
    }

    /**
     * [BottomSheetBehavior.fitToContents]
     *
     * wraps [BottomSheetBehavior.setFitToContents]
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_fitToContents
     */
    public suspend fun setFitToContents(fitToContents: Boolean) {
        behavior()?.let {
            mutex.withLock {
                it.isFitToContents = fitToContents
            }
        }
    }

    /**
     * [BottomSheetBehavior.maxWidth]
     *
     * default [BottomSheetBehaviorProperties.NO_MAX_SIZE]
     * @return [Px]
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_android_maxWidth
     */
    public fun getMaxWidth(): IntState {
        return behavior()?.snapshotMaxWidth ?: mutableIntStateOf(properties.maxWidth)
    }

    /**
     * [BottomSheetBehavior.maxHeight]
     *
     * default [BottomSheetBehaviorProperties.NO_MAX_SIZE]
     * @return [Px]
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_android_maxHeight
     */
    public fun getMaxHeight(): IntState {
        return behavior()?.snapshotMaxHeight ?: mutableIntStateOf(properties.maxHeight)
    }

    /**
     * [BottomSheetBehavior.peekHeight]
     *
     * default [BottomSheetBehavior.PEEK_HEIGHT_AUTO]
     * @return [IntRange] from = -1
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_peekHeight
     */
    public fun getPeekHeight(): IntState {
        return behavior()?.snapshotPeekHeight ?: mutableIntStateOf(properties.peekHeight)
    }

    /**
     * [BottomSheetBehavior.peekHeight]
     *
     * wraps [BottomSheetBehavior.setPeekHeight]
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_peekHeight
     */
    public suspend fun setPeekHeight(@IntRange(from = -1) peekHeight: Int) {
        behavior()?.let {
            mutex.withLock {
                it.peekHeight = peekHeight
            }
        }
    }

    /**
     * [BottomSheetBehavior.halfExpandedRatio]
     *
     * default 0.5f
     * @return [FloatRange] from = 0.0, to = 1.0
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_halfExpandedRatio
     */
    public fun getHalfExpandedRatio(): FloatState {
        return behavior()?.snapshotHalfExpandedRatio ?: mutableFloatStateOf(properties.halfExpandedRatio)
    }

    /**
     * [BottomSheetBehavior.halfExpandedRatio]
     *
     * wraps [BottomSheetBehavior.setHalfExpandedRatio]
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_halfExpandedRatio
     */
    public suspend fun setHalfExpandedRatio(
        @FloatRange(
            from = 0.0,
            to = 1.0,
            fromInclusive = false,
            toInclusive = false,
        ) ratio: Float,
    ) {
        behavior()?.let {
            mutex.withLock {
                it.halfExpandedRatio = ratio
            }
        }
    }

    /**
     * [BottomSheetBehavior.expandedOffset]
     *
     * default 0
     * @return [IntRange] from = 0
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_expandedOffset
     */
    public fun getExpandedOffset(): IntState {
        return behavior()?.snapshotExpandedOffset ?: mutableIntStateOf(properties.expandedOffset)
    }

    /**
     * [BottomSheetBehavior.expandedOffset]
     *
     * wraps [BottomSheetBehavior.setExpandedOffset]
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_expandedOffset
     */
    public suspend fun setExpandedOffset(@IntRange(from = 0) offset: Int) {
        behavior()?.let {
            mutex.withLock {
                it.expandedOffset = offset
            }
        }
    }

    /**
     * [BottomSheetBehavior.calculateSlideOffset]
     */
    public fun getSlideOffset(): FloatState {
        return mutableFloatStateOf(behavior()?.snapshotSlideOffset?.floatValue ?: -1f)
    }

    /**
     * [BottomSheetBehavior.hideable]
     *
     * default true
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_hideable
     */
    public fun getHideable(): State<Boolean> {
        return behavior()?.snapshotHideable ?: mutableStateOf(properties.hideable)
    }

    /**
     * [BottomSheetBehavior.hideable]
     *
     * wraps [BottomSheetBehavior.setHideable]
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_hideable
     */
    public suspend fun setHideable(hideable: Boolean) {
        behavior()?.let {
            mutex.withLock {
                it.isHideable = hideable
            }
        }
    }

    /**
     * [BottomSheetBehavior.skipCollapsed]
     *
     * default false
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_skipCollapsed
     */
    public fun getSkipCollapsed(): State<Boolean> {
        return behavior()?.snapshotSkipCollapsed ?: mutableStateOf(properties.skipCollapsed)
    }

    /**
     * [BottomSheetBehavior.skipCollapsed]
     *
     * wraps [BottomSheetBehavior.setSkipCollapsed]
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_skipCollapsed
     */
    public suspend fun setSkipCollapsed(skipCollapsed: Boolean) {
        behavior()?.let {
            mutex.withLock {
                it.skipCollapsed = skipCollapsed
            }
        }
    }

    /**
     * [BottomSheetBehavior.draggable]
     *
     * default true
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_draggable
     */
    public fun getDraggable(): State<Boolean> {
        return behavior()?.snapshotDraggable ?: mutableStateOf(properties.draggable)
    }

    /**
     * [BottomSheetBehavior.draggable]
     *
     * wraps [BottomSheetBehavior.setDraggable]
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_draggable
     */
    public suspend fun setDraggable(draggable: Boolean) {
        behavior()?.let {
            mutex.withLock {
                it.isDraggable = draggable
            }
        }
    }

    /**
     * [BottomSheetBehavior.significantVelocityThreshold]
     *
     * default [BottomSheetBehaviorProperties.DEFAULT_SIGNIFICANT_VEL_THRESHOLD]
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_significantVelocityThreshold
     */
    public fun getSignificantVelocityThreshold(): IntState {
        return behavior()?.snapshotSignificantVelocityThreshold
            ?: mutableIntStateOf(properties.significantVelocityThreshold)
    }

    /**
     * [BottomSheetBehavior.significantVelocityThreshold]
     *
     * wraps [BottomSheetBehavior.setSignificantVelocityThreshold]
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_significantVelocityThreshold
     */
    public suspend fun setSignificantVelocityThreshold(significantVelocityThreshold: Int) {
        behavior()?.let {
            mutex.withLock {
                it.significantVelocityThreshold = significantVelocityThreshold
            }
        }
    }

    /**
     * [BottomSheetBehavior.saveFlags]
     *
     * default [BottomSheetBehavior.SAVE_NONE]
     * @return [BottomSheetBehavior.SaveFlags]
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_saveFlags
     */
    public fun getSaveFlags(): IntState {
        return behavior()?.snapshotSaveFlags ?: mutableIntStateOf(properties.saveFlags)
    }

    /**
     * [BottomSheetBehavior.saveFlags]
     *
     * wraps [BottomSheetBehavior.setSaveFlags]
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_saveFlags
     */
    public suspend fun setSaveFlags(@BottomSheetBehavior.SaveFlags flags: Int) {
        behavior()?.let {
            mutex.withLock {
                it.saveFlags = flags
            }
        }
    }

    /**
     * [BottomSheetBehavior.hideFriction]
     *
     * default [BottomSheetBehaviorProperties.HIDE_FRICTION]
     */
    public fun getHideFriction(): FloatState {
        return behavior()?.snapshotHideFriction ?: mutableFloatStateOf(properties.hideFriction)
    }

    /**
     * [BottomSheetBehavior.hideFriction]
     *
     * wraps [BottomSheetBehavior.setHideFriction]
     */
    public suspend fun setHideFriction(hideFriction: Float) {
        behavior()?.let {
            mutex.withLock {
                it.hideFriction = hideFriction
            }
        }
    }
}
