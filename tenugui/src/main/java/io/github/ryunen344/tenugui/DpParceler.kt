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
import androidx.compose.ui.unit.Dp
import kotlinx.parcelize.Parceler

internal object DpParceler : Parceler<Dp> {
    override fun Dp.write(parcel: Parcel, flags: Int) {
        parcel.writeFloat(value)
    }

    override fun create(parcel: Parcel): Dp {
        return Dp(parcel.readFloat())
    }
}
