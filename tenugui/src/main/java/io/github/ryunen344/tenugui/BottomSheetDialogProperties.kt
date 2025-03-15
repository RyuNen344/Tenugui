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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.core.os.ParcelCompat

@Immutable
public data class BottomSheetDialogProperties(
    val dismissOnBackPress: Boolean = true,

    /**
     * [com.google.android.material.bottomsheet.BottomSheetDialog.cancelable]
     */
    val cancelable: Boolean = true,

    /**
     * [com.google.android.material.bottomsheet.BottomSheetDialog.canceledOnTouchOutside]
     */
    val canceledOnTouchOutside: Boolean = true,

    /**
     * [com.google.android.material.bottomsheet.BottomSheetDialog.dismissWithAnimation]
     */
    val dismissWithAnimation: Boolean = true,

    /**
     * Policy for setting [android.view.WindowManager.LayoutParams.FLAG_SECURE] on the bottom sheet's window.
     */
    val securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,

    /**
     * [com.google.android.material.bottomsheet.BottomSheetDialog.edgeToEdgeEnabled]
     */
    val decorFitsSystemWindows: Boolean = true,

    /**
     * [com.google.android.material.R.dimen.design_bottom_sheet_elevation]
     */
    val elevation: Dp = 8.dp,
) : Parcelable {

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            ParcelCompat.writeBoolean(this, dismissOnBackPress)
            ParcelCompat.writeBoolean(this, cancelable)
            ParcelCompat.writeBoolean(this, canceledOnTouchOutside)
            ParcelCompat.writeBoolean(this, dismissWithAnimation)
            writeInt(securePolicy.ordinal)
            ParcelCompat.writeBoolean(this, decorFitsSystemWindows)
            writeFloat(elevation.value)
        }
    }

    public companion object {
        @JvmField
        public val CREATOR: Parcelable.ClassLoaderCreator<BottomSheetDialogProperties> = object : Parcelable.ClassLoaderCreator<BottomSheetDialogProperties> {
            override fun createFromParcel(source: Parcel?): BottomSheetDialogProperties? {
                return createFromParcel(source, null)
            }

            override fun createFromParcel(
                source: Parcel?,
                loader: ClassLoader?,
            ): BottomSheetDialogProperties? {
                return source?.let {
                    BottomSheetDialogProperties(
                        dismissOnBackPress = ParcelCompat.readBoolean(it),
                        cancelable = ParcelCompat.readBoolean(it),
                        canceledOnTouchOutside = ParcelCompat.readBoolean(it),
                        dismissWithAnimation = ParcelCompat.readBoolean(it),
                        securePolicy = SecureFlagPolicy.entries[it.readInt()],
                        decorFitsSystemWindows = ParcelCompat.readBoolean(it),
                        elevation = Dp(it.readFloat()),
                    )
                }
            }

            override fun newArray(size: Int): Array<out BottomSheetDialogProperties?>? = arrayOfNulls(size)
        }
    }
}

@Composable
public fun rememberBottomSheetDialogProperties(
    key: String? = null,
    dismissOnBackPress: Boolean = true,
    cancelable: Boolean = true,
    canceledOnTouchOutside: Boolean = true,
    dismissWithAnimation: Boolean = true,
    securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
    decorFitsSystemWindows: Boolean = true,
    elevation: Dp = 0.dp,
): BottomSheetDialogProperties {
    return remember(key1 = key) {
        BottomSheetDialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            cancelable = cancelable,
            canceledOnTouchOutside = canceledOnTouchOutside,
            dismissWithAnimation = dismissWithAnimation,
            securePolicy = securePolicy,
            decorFitsSystemWindows = decorFitsSystemWindows,
            elevation = elevation,
        )
    }
}
