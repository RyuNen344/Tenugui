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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.truth.os.ParcelableSubject.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BottomSheetDialogPropertiesTest {

    private lateinit var parcel: Parcel

    @Before
    fun setUp() {
        parcel = Parcel.obtain()
    }

    @After
    fun tearDown() {
        parcel.recycle()
    }

    @Test
    fun testCreateFromParcel() {
        val expect = BottomSheetDialogProperties(
            dismissOnBackPress = false,
            cancelable = false,
            canceledOnTouchOutside = false,
            dismissWithAnimation = false,
            securePolicy = SecureFlagPolicy.SecureOn,
            decorFitsSystemWindows = false,
            elevation = 16.dp,
        )

        expect.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val actual = BottomSheetDialogProperties.CREATOR.createFromParcel(parcel)

        assertThat(actual).recreatesEqual(BottomSheetDialogProperties.CREATOR)
        assertThat(actual).marshallsEquallyTo(expect)
    }
}
