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

package io.github.ryunen344.tenugui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.Px
import androidx.compose.runtime.FloatState
import androidx.compose.runtime.IntState
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.asFloatState
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.google.android.material.bottomsheet.BottomSheetBehavior

private typealias ComposeState<T> = androidx.compose.runtime.State<T>

@Stable
public class ListenableBottomSheetBehavior @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : BottomSheetBehavior<FrameLayout>(context, attrs) {

    private val _snapshotState: MutableIntState = mutableIntStateOf(super.getState())
    public val snapshotState: IntState = _snapshotState.asIntState()

    private val _snapshotFitToContents: MutableState<Boolean> = mutableStateOf(super.isFitToContents())
    public val snapshotFitToContents: ComposeState<Boolean> = _snapshotFitToContents

    private val _snapshotMaxWidth: MutableIntState = mutableIntStateOf(super.getMaxWidth())
    public val snapshotMaxWidth: IntState = _snapshotMaxWidth.asIntState()

    private val _snapshotMaxHeight: MutableIntState = mutableIntStateOf(super.getMaxHeight())
    public val snapshotMaxHeight: IntState = _snapshotMaxHeight.asIntState()

    private val _snapshotPeekHeight: MutableIntState = mutableIntStateOf(super.getPeekHeight())
    public val snapshotPeekHeight: IntState = _snapshotPeekHeight.asIntState()

    private val _snapshotHalfExpandedRatio: MutableFloatState = mutableFloatStateOf(super.getHalfExpandedRatio())
    public val snapshotHalfExpandedRatio: FloatState = _snapshotHalfExpandedRatio.asFloatState()

    private val _snapshotExpandedOffset = mutableIntStateOf(super.getExpandedOffset())
    public val snapshotExpandedOffset: IntState = _snapshotExpandedOffset.asIntState()

    private val _snapshotSlideOffset: MutableFloatState = mutableFloatStateOf(super.calculateSlideOffset())
    public val snapshotSlideOffset: FloatState = _snapshotSlideOffset.asFloatState()

    private val _snapshotHideable: MutableState<Boolean> = mutableStateOf(super.isHideable())
    public val snapshotHideable: ComposeState<Boolean> = _snapshotHideable

    private val _snapshotSkipCollapsed: MutableState<Boolean> = mutableStateOf(super.getSkipCollapsed())
    public val snapshotSkipCollapsed: ComposeState<Boolean> = _snapshotSkipCollapsed

    private val _snapshotDraggable: MutableState<Boolean> = mutableStateOf(super.isDraggable())
    public val snapshotDraggable: ComposeState<Boolean> = _snapshotDraggable

    private val _snapshotSignificantVelocityThreshold: MutableIntState = mutableIntStateOf(super.getSignificantVelocityThreshold())
    public val snapshotSignificantVelocityThreshold: IntState = _snapshotSignificantVelocityThreshold.asIntState()

    private val _snapshotSaveFlags: MutableIntState = mutableIntStateOf(super.getSaveFlags())
    public val snapshotSaveFlags: IntState = _snapshotSaveFlags.asIntState()

    private val _snapshotHideFriction: MutableFloatState = mutableFloatStateOf(super.getHideFriction())
    public val snapshotHideFriction: FloatState = _snapshotHideFriction.asFloatState()

    private val _snapshotGestureInsetBottomIgnored: MutableState<Boolean> =
        mutableStateOf(super.isGestureInsetBottomIgnored())
    public val snapshotGestureInsetBottomIgnored: ComposeState<Boolean> = _snapshotGestureInsetBottomIgnored

    private val _snapshotShouldRemoveExpandedCorners: MutableState<Boolean> =
        mutableStateOf(super.isShouldRemoveExpandedCorners())
    public val snapshotShouldRemoveExpandedCorners: ComposeState<Boolean> = _snapshotShouldRemoveExpandedCorners

    init {
        addBottomSheetCallback(
            object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    executeUpdates()
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    executeUpdates()
                }

                private fun executeUpdates() {
                    state
                    isFitToContents
                    maxWidth
                    maxHeight
                    peekHeight
                    halfExpandedRatio
                    expandedOffset
                    _snapshotSlideOffset.floatValue = calculateSlideOffset()
                    isHideable
                    skipCollapsed
                    isDraggable
                    significantVelocityThreshold
                    saveFlags
                    hideFriction
                    isGestureInsetBottomIgnored
                    isShouldRemoveExpandedCorners
                }
            },
        )
    }

    override fun setState(@StableState state: Int) {
        super.setState(state)
        if (_snapshotState != null) _snapshotState.intValue = super.getState()
    }

    @State
    override fun getState(): Int {
        return super.getState().also { _snapshotState.intValue = it }
    }

    override fun setFitToContents(fitToContents: Boolean) {
        super.setFitToContents(fitToContents)
        if (_snapshotFitToContents != null) _snapshotFitToContents.value = super.isFitToContents()
    }

    override fun isFitToContents(): Boolean {
        return super.isFitToContents().also { _snapshotFitToContents.value = it }
    }

    override fun setMaxWidth(@Px maxWidth: Int) {
        super.setMaxWidth(maxWidth)
        if (_snapshotMaxWidth != null) _snapshotMaxWidth.intValue = super.getMaxWidth()
    }

    @Px
    override fun getMaxWidth(): Int {
        return super.getMaxWidth().also { _snapshotMaxWidth.intValue = it }
    }

    override fun setMaxHeight(@Px maxHeight: Int) {
        super.setMaxHeight(maxHeight)
        if (_snapshotMaxHeight != null) _snapshotMaxHeight.intValue = super.getMaxHeight()
    }

    @Px
    override fun getMaxHeight(): Int {
        return super.getMaxHeight().also { _snapshotMaxHeight.intValue = it }
    }

    override fun setPeekHeight(peekHeight: Int) {
        super.setPeekHeight(peekHeight, true)
        if (_snapshotPeekHeight != null) _snapshotPeekHeight.intValue = super.getPeekHeight()
    }

    override fun getPeekHeight(): Int {
        return super.getPeekHeight().also { _snapshotPeekHeight.intValue = it }
    }

    override fun setHalfExpandedRatio(
        @FloatRange(
            from = 0.0,
            to = 1.0,
            fromInclusive = false,
            toInclusive = false,
        ) ratio: Float,
    ) {
        super.setHalfExpandedRatio(ratio)
        if (_snapshotHalfExpandedRatio != null) _snapshotHalfExpandedRatio.floatValue = super.getHalfExpandedRatio()
    }

    @FloatRange(from = 0.0, to = 1.0)
    override fun getHalfExpandedRatio(): Float {
        return super.getHalfExpandedRatio().also { _snapshotHalfExpandedRatio.floatValue = it }
    }

    override fun setExpandedOffset(@IntRange(from = 0) offset: Int) {
        super.setExpandedOffset(offset)
        if (_snapshotExpandedOffset != null) _snapshotExpandedOffset.intValue = super.getExpandedOffset()
    }

    @IntRange(from = 0)
    override fun getExpandedOffset(): Int {
        return super.getExpandedOffset().also { _snapshotExpandedOffset.intValue = it }
    }

    override fun setHideable(hideable: Boolean) {
        super.setHideable(hideable)
        if (_snapshotHideable != null) _snapshotHideable.value = super.isHideable()
    }

    override fun isHideable(): Boolean {
        return super.isHideable().also { _snapshotHideable.value = it }
    }

    override fun setSkipCollapsed(skipCollapsed: Boolean) {
        super.setSkipCollapsed(skipCollapsed)
        if (_snapshotSkipCollapsed != null) _snapshotSkipCollapsed.value = super.getSkipCollapsed()
    }

    override fun getSkipCollapsed(): Boolean {
        return super.getSkipCollapsed().also { _snapshotSkipCollapsed.value = it }
    }

    override fun setDraggable(draggable: Boolean) {
        super.setDraggable(draggable)
        if (_snapshotDraggable != null) _snapshotDraggable.value = super.isDraggable()
    }

    override fun isDraggable(): Boolean {
        return super.isDraggable().also { _snapshotDraggable.value = it }
    }

    override fun setSignificantVelocityThreshold(significantVelocityThreshold: Int) {
        super.setSignificantVelocityThreshold(significantVelocityThreshold)
        if (_snapshotSignificantVelocityThreshold != null) _snapshotSignificantVelocityThreshold.intValue = super.getSignificantVelocityThreshold()
    }

    override fun getSignificantVelocityThreshold(): Int {
        return super.getSignificantVelocityThreshold().also { _snapshotSignificantVelocityThreshold.intValue = it }
    }

    override fun setSaveFlags(@SaveFlags flags: Int) {
        super.setSaveFlags(flags)
        if (_snapshotSaveFlags != null) _snapshotSaveFlags.intValue = super.getSaveFlags()
    }

    @SaveFlags
    override fun getSaveFlags(): Int {
        return super.getSaveFlags().also { _snapshotSaveFlags.intValue = it }
    }

    override fun setHideFriction(hideFriction: Float) {
        super.setHideFriction(hideFriction)
        if (_snapshotHideFriction != null) _snapshotHideFriction.floatValue = super.getHideFriction()
    }

    override fun getHideFriction(): Float {
        return super.getHideFriction().also { _snapshotHideFriction.floatValue = it }
    }

    override fun setGestureInsetBottomIgnored(gestureInsetBottomIgnored: Boolean) {
        super.setGestureInsetBottomIgnored(gestureInsetBottomIgnored)
        if (_snapshotGestureInsetBottomIgnored != null) _snapshotGestureInsetBottomIgnored.value = super.isGestureInsetBottomIgnored()
    }

    override fun isGestureInsetBottomIgnored(): Boolean {
        return super.isGestureInsetBottomIgnored().also { _snapshotGestureInsetBottomIgnored.value = it }
    }

    override fun setShouldRemoveExpandedCorners(shouldRemoveExpandedCorners: Boolean) {
        super.setShouldRemoveExpandedCorners(shouldRemoveExpandedCorners)
        if (_snapshotShouldRemoveExpandedCorners != null) _snapshotShouldRemoveExpandedCorners.value = super.isShouldRemoveExpandedCorners()
    }

    override fun isShouldRemoveExpandedCorners(): Boolean {
        return super.isShouldRemoveExpandedCorners().also { _snapshotShouldRemoveExpandedCorners.value = it }
    }
}
