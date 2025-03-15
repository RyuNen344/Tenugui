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
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.truth.os.ParcelableSubject.assertThat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BottomSheetBehaviorPropertiesTest {

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
        val expect = BottomSheetBehaviorProperties(
            fitToContents = false,
            maxWidth = 36,
            maxHeight = 56,
            peekHeight = 456,
            halfExpandedRatio = 0.6f,
            expandedOffset = 234,
            hideable = false,
            skipCollapsed = true,
            draggable = false,
            significantVelocityThreshold = 210,
            saveFlags = BottomSheetBehavior.SAVE_ALL,
            hideFriction = 0.6f,
        )

        expect.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val actual = BottomSheetBehaviorProperties.CREATOR.createFromParcel(parcel)

        assertThat(actual).recreatesEqual(BottomSheetBehaviorProperties.CREATOR)
        assertThat(actual).marshallsEquallyTo(expect)
    }
}
