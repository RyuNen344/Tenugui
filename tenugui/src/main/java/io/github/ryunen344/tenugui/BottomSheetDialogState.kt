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

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.Px
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.core.os.ParcelCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.github.ryunen344.tenugui.BottomSheetBehaviorProperties.Companion.DEFAULT_SIGNIFICANT_VEL_THRESHOLD
import io.github.ryunen344.tenugui.BottomSheetBehaviorProperties.Companion.HIDE_FRICTION
import io.github.ryunen344.tenugui.BottomSheetBehaviorProperties.Companion.NO_MAX_SIZE

@Stable
public class BottomSheetDialogState(
    internal val savedState: SavedState = SavedState(null, null),
    public val dialogProperties: BottomSheetDialogProperties = BottomSheetDialogProperties(),
    public val behaviorProperties: BottomSheetBehaviorProperties = BottomSheetBehaviorProperties(),
) {
    private var snapshotBehavior: ListenableBottomSheetBehavior? by mutableStateOf(null)

    public val behavior: BottomSheetBehaviorState = BottomSheetBehaviorState(behaviorProperties) { snapshotBehavior }

    public fun onSaveInstanceState(dialog: BottomSheetDialogWrapper) {
        val behavior = dialog.behavior as? ListenableBottomSheetBehavior
        snapshotBehavior = behavior
        this.savedState.dialogState = dialog.onSaveInstanceState()
        val coordinator = dialog.coordinator ?: return
        val bottomSheet = dialog.bottomSheet ?: return
        this.savedState.behaviorState = behavior?.onSaveInstanceState(coordinator, bottomSheet)
    }

    public fun onRestoreInstanceState(dialog: BottomSheetDialogWrapper) {
        val behavior = dialog.behavior as? ListenableBottomSheetBehavior
        snapshotBehavior = behavior
        val coordinator = dialog.coordinator ?: return
        val bottomSheet = dialog.bottomSheet ?: return
        savedState.behaviorState?.let { behavior?.onRestoreInstanceState(coordinator, bottomSheet, it) }
        savedState.dialogState?.let(dialog::onRestoreInstanceState)
    }

    public fun setProperties(behavior: BottomSheetBehavior<*>) {
        with(behaviorProperties) {
            with(this) {
                behavior.isFitToContents = fitToContents
                behavior.maxWidth = maxWidth
                behavior.maxHeight = maxHeight
                behavior.peekHeight = peekHeight
                behavior.halfExpandedRatio = halfExpandedRatio
                behavior.expandedOffset = expandedOffset
                behavior.isHideable = hideable
                behavior.skipCollapsed = skipCollapsed
                behavior.isDraggable = draggable
                behavior.significantVelocityThreshold = significantVelocityThreshold
                behavior.saveFlags = saveFlags
                behavior.hideFriction = hideFriction
                /**
                 * disable status bar inset
                 * [BottomSheetBehavior.setWindowInsetsListener]
                 */
                behavior.isGestureInsetBottomIgnored = !dialogProperties.decorFitsSystemWindows
            }
        }
    }

    public fun dispose() {
        snapshotBehavior = null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BottomSheetDialogState

        if (savedState != other.savedState) return false
        if (dialogProperties != other.dialogProperties) return false
        if (behaviorProperties != other.behaviorProperties) return false
        if (snapshotBehavior != other.snapshotBehavior) return false
        if (behavior != other.behavior) return false

        return true
    }

    override fun hashCode(): Int {
        var result = savedState.hashCode()
        result = 31 * result + dialogProperties.hashCode()
        result = 31 * result + behaviorProperties.hashCode()
        result = 31 * result + snapshotBehavior.hashCode()
        result = 31 * result + behavior.hashCode()
        return result
    }

    override fun toString(): String {
        return "BottomSheetDialogState(savedState = $savedState, dialogProperties = $dialogProperties, behaviorProperties = $behaviorProperties, " +
            "snapshotBehavior = $snapshotBehavior, behavior = $behavior)"
    }

    @Stable
    public class SavedState(
        public var dialogState: Bundle? = null,
        public var behaviorState: Parcelable? = null,
    ) : Parcelable {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SavedState

            if (dialogState != other.dialogState) return false
            if (behaviorState != other.behaviorState) return false

            return true
        }

        override fun hashCode(): Int {
            var result = dialogState?.hashCode() ?: 0
            result = 31 * result + (behaviorState?.hashCode() ?: 0)
            return result
        }

        override fun describeContents(): Int = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            with(dest) {
                writeBundle(dialogState)
                writeParcelable(behaviorState, flags)
            }
        }

        public companion object {
            @JvmField
            public val CREATOR: Parcelable.ClassLoaderCreator<SavedState> = object : Parcelable.ClassLoaderCreator<SavedState> {
                override fun createFromParcel(source: Parcel?): SavedState? {
                    return createFromParcel(source, null)
                }

                override fun createFromParcel(
                    source: Parcel?,
                    loader: ClassLoader?,
                ): SavedState? {
                    return source?.let {
                        SavedState(
                            dialogState = it.readBundle(loader),
                            behaviorState = ParcelCompat.readParcelable(it, loader, Parcelable::class.java),
                        )
                    }
                }

                override fun newArray(size: Int): Array<out SavedState?>? = arrayOfNulls(size)
            }
        }
    }
}

@Composable
public fun rememberBottomSheetDialogState(
    key: String? = null,
    dialogState: Bundle? = null,
    behaviorState: Parcelable? = null,
    dialogProperties: BottomSheetDialogProperties = rememberBottomSheetDialogProperties(key = key),
    behaviorProperties: BottomSheetBehaviorProperties = rememberBottomSheetBehaviorProperties(key = key),
): BottomSheetDialogState {
    return rememberSaveable(dialogProperties, behaviorProperties, saver = BottomSheetDialogStateSaver, key = key) {
        BottomSheetDialogState(
            savedState = BottomSheetDialogState.SavedState(dialogState, behaviorState),
            dialogProperties = dialogProperties,
            behaviorProperties = behaviorProperties,
        )
    }
}

@Composable
public fun rememberBottomSheetDialogState(
    key: String? = null,
    dialogState: Bundle? = null,
    behaviorState: Parcelable? = null,
    dismissOnBackPress: Boolean = true,
    cancelable: Boolean = true,
    canceledOnTouchOutside: Boolean = true,
    dismissWithAnimation: Boolean = true,
    securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
    decorFitsSystemWindows: Boolean = true,
    elevation: Dp = 8.dp,
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
): BottomSheetDialogState {
    return rememberSaveable(
        dismissOnBackPress,
        cancelable,
        canceledOnTouchOutside,
        dismissWithAnimation,
        securePolicy,
        decorFitsSystemWindows,
        elevation,
        fitToContents,
        maxWidth,
        maxHeight,
        peekHeight,
        halfExpandedRatio,
        expandedOffset,
        hideable,
        skipCollapsed,
        draggable,
        significantVelocityThreshold,
        saveFlags,
        hideFriction,
        saver = BottomSheetDialogStateSaver,
        key = key,
    ) {
        BottomSheetDialogState(
            savedState = BottomSheetDialogState.SavedState(dialogState, behaviorState),
            dialogProperties = BottomSheetDialogProperties(
                dismissOnBackPress = dismissOnBackPress,
                cancelable = cancelable,
                canceledOnTouchOutside = canceledOnTouchOutside,
                dismissWithAnimation = dismissWithAnimation,
                securePolicy = securePolicy,
                decorFitsSystemWindows = decorFitsSystemWindows,
                elevation = elevation,
            ),
            behaviorProperties = BottomSheetBehaviorProperties(
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
            ),
        )
    }
}

internal val BottomSheetDialogStateSaver: Saver<BottomSheetDialogState, Any> = run {
    val savedStateKey = "savedState"
    val dialogPropertiesKey = "dialogProperties"
    val behaviorPropertiesKey = "behaviorProperties"
    mapSaver(
        save = {
            mapOf(
                savedStateKey to it.savedState,
                dialogPropertiesKey to it.dialogProperties,
                behaviorPropertiesKey to it.behaviorProperties,
            )
        },
        restore = {
            BottomSheetDialogState(
                it[savedStateKey] as BottomSheetDialogState.SavedState,
                it[dialogPropertiesKey] as BottomSheetDialogProperties,
                it[behaviorPropertiesKey] as BottomSheetBehaviorProperties,
            )
        },
    )
}
