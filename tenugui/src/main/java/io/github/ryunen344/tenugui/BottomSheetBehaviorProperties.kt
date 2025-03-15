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

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.Px
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.core.os.ParcelCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.github.ryunen344.tenugui.BottomSheetBehaviorProperties.Companion.DEFAULT_SIGNIFICANT_VEL_THRESHOLD
import io.github.ryunen344.tenugui.BottomSheetBehaviorProperties.Companion.HIDE_FRICTION
import io.github.ryunen344.tenugui.BottomSheetBehaviorProperties.Companion.NO_MAX_SIZE

@Immutable
public data class BottomSheetBehaviorProperties(
    /**
     * [BottomSheetBehavior.fitToContents]
     *
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_fitToContents
     */
    val fitToContents: Boolean = true,

    /**
     * [BottomSheetBehavior.maxWidth]
     *
     * This value should be set before [BottomSheetDialog.show] in order for the width to be adjusted as expected.
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_android_maxWidth
     */
    @Px val maxWidth: Int = NO_MAX_SIZE,

    /**
     * [BottomSheetBehavior.maxHeight]
     *
     * This value should be set before [BottomSheetDialog.show] in order for the height to be adjusted as expected.
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_android_maxHeight
     */
    @Px val maxHeight: Int = NO_MAX_SIZE,

    /**
     * [BottomSheetBehavior.peekHeight]
     *
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_peekHeight
     */
    @IntRange(from = -1) val peekHeight: Int = BottomSheetBehavior.PEEK_HEIGHT_AUTO,

    /**
     * [BottomSheetBehavior.halfExpandedRatio]
     *
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_halfExpandedRatio
     */
    @FloatRange(from = 0.0, to = 1.0, fromInclusive = false, toInclusive = false) val halfExpandedRatio: Float = 0.5f,

    /**
     * [BottomSheetBehavior.expandedOffset]
     *
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_expandedOffset
     */
    @IntRange(from = 0) val expandedOffset: Int = 0,

    /**
     * [BottomSheetBehavior.hideable]
     *
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_hideable
     */
    val hideable: Boolean = true,

    /**
     * [BottomSheetBehavior.skipCollapsed]
     *
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_skipCollapsed
     */
    val skipCollapsed: Boolean = false,

    /**
     * [BottomSheetBehavior.draggable]
     *
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_draggable
     */
    val draggable: Boolean = true,

    /**
     * [BottomSheetBehavior.significantVelocityThreshold]
     *
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_significantVelocityThreshold
     */
    val significantVelocityThreshold: Int = DEFAULT_SIGNIFICANT_VEL_THRESHOLD,

    /**
     * [BottomSheetBehavior.saveFlags]
     *
     * @attr com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_saveFlags
     */
    @BottomSheetBehavior.SaveFlags val saveFlags: Int = BottomSheetBehavior.SAVE_NONE,

    /**
     * [BottomSheetBehavior.hideFriction]
     */
    val hideFriction: Float = HIDE_FRICTION,
) : Parcelable {
    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            ParcelCompat.writeBoolean(this, fitToContents)
            writeInt(maxWidth)
            writeInt(maxHeight)
            writeInt(peekHeight)
            writeFloat(halfExpandedRatio)
            writeInt(expandedOffset)
            ParcelCompat.writeBoolean(this, hideable)
            ParcelCompat.writeBoolean(this, skipCollapsed)
            ParcelCompat.writeBoolean(this, draggable)
            writeInt(significantVelocityThreshold)
            writeInt(saveFlags)
            writeFloat(hideFriction)
        }
    }

    public companion object {
        /**
         * [BottomSheetBehavior.NO_MAX_SIZE]
         */
        public const val NO_MAX_SIZE: Int = -1

        /**
         * [BottomSheetBehavior.DEFAULT_SIGNIFICANT_VEL_THRESHOLD]
         */
        public const val DEFAULT_SIGNIFICANT_VEL_THRESHOLD: Int = 500

        /**
         * [BottomSheetBehavior.HIDE_FRICTION]
         */
        public const val HIDE_FRICTION: Float = 0.1f

        @JvmField
        public val CREATOR: Parcelable.ClassLoaderCreator<BottomSheetBehaviorProperties> =
            object : Parcelable.ClassLoaderCreator<BottomSheetBehaviorProperties> {
                override fun createFromParcel(source: Parcel?): BottomSheetBehaviorProperties? {
                    return createFromParcel(source, null)
                }

                override fun createFromParcel(
                    source: Parcel?,
                    loader: ClassLoader?,
                ): BottomSheetBehaviorProperties? {
                    return source?.let {
                        BottomSheetBehaviorProperties(
                            fitToContents = ParcelCompat.readBoolean(it),
                            maxWidth = it.readInt(),
                            maxHeight = it.readInt(),
                            peekHeight = it.readInt(),
                            halfExpandedRatio = it.readFloat(),
                            expandedOffset = it.readInt(),
                            hideable = ParcelCompat.readBoolean(it),
                            skipCollapsed = ParcelCompat.readBoolean(it),
                            draggable = ParcelCompat.readBoolean(it),
                            significantVelocityThreshold = it.readInt(),
                            saveFlags = it.readInt(),
                            hideFriction = it.readFloat(),
                        )
                    }
                }

                override fun newArray(size: Int): Array<BottomSheetBehaviorProperties?> = arrayOfNulls(size)
            }
    }
}

@Composable
public fun rememberBottomSheetBehaviorProperties(
    key: String? = null,
    fitToContents: Boolean = true,
    @Px maxWidth: Int = NO_MAX_SIZE,
    @Px maxHeight: Int = NO_MAX_SIZE,
    @IntRange(from = -1) peekHeight: Int = BottomSheetBehavior.PEEK_HEIGHT_AUTO,
    @FloatRange(from = 0.0, to = 1.0, fromInclusive = false, toInclusive = false) halfExpandedRatio: Float = 0.5f,
    @IntRange(from = 0) expandedOffset: Int = 0,
    hideable: Boolean = true,
    skipCollapsed: Boolean = false,
    draggable: Boolean = true,
    significantVelocityThreshold: Int = DEFAULT_SIGNIFICANT_VEL_THRESHOLD,
    @BottomSheetBehavior.SaveFlags saveFlags: Int = BottomSheetBehavior.SAVE_NONE,
    hideFriction: Float = HIDE_FRICTION,
): BottomSheetBehaviorProperties {
    return remember(key1 = key) {
        BottomSheetBehaviorProperties(
            fitToContents = fitToContents,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
            peekHeight = peekHeight,
            halfExpandedRatio = halfExpandedRatio,
            expandedOffset = expandedOffset,
            hideable = hideable,
            skipCollapsed = skipCollapsed,
            draggable = draggable,
            significantVelocityThreshold = significantVelocityThreshold,
            saveFlags = saveFlags,
            hideFriction = hideFriction,
        )
    }
}
