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

package io.github.ryunen344.tenugui.tests.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.ryunen344.tenugui.R

class NormalBottomSheetDialogFragment : BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.Theme_Tenugui_EdgeToEdge).apply {
            setContentView(
                ComposeView(requireContext()).apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Cyan),
                        ) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = "column it",
                            )
                        }
                    }
                },
            )
            requireNotNull(findViewById<FrameLayout>(com.google.android.material.R.id.container))
            requireNotNull(findViewById<CoordinatorLayout>(com.google.android.material.R.id.coordinator))
            val bottomSheet =
                requireNotNull(findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet))
            val behavior = BottomSheetBehavior.from(bottomSheet)
            setCancelable(false)

            behavior.maxWidth = -1
            behavior.maxHeight = -1
            behavior.isHideable = false

            ViewCompat.setOnApplyWindowInsetsListener(bottomSheet) { v, windowInsets ->
                val insets =
                    windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.displayCutout())

                val insetsWidth = insets.right + insets.left
                val insetsHeight = insets.top + insets.bottom

                Log.e(TAG, "insetsWidth $insetsWidth")
                Log.e(TAG, "insetsHeight $insetsHeight")

                // Now apply the insets on our view
//                ViewCompat.onApplyWindowInsets(v, windowInsets.inset(Insets.NONE))
                WindowInsetsCompat.CONSUMED
            }

            ViewCompat.setOnApplyWindowInsetsListener(requireNotNull(window?.decorView)) { view, insets ->
                val metrics = resources.displayMetrics
                val displayCutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
                val insetsWidth = (displayCutout.right + displayCutout.left) / metrics.density
                val insetsHeight = (displayCutout.top + displayCutout.bottom) / metrics.density

                Log.e(TAG, "displayCutout ${insets.displayCutout}")
                Log.e(TAG, "decorView insetsWidth $insetsWidth")
                Log.e(TAG, "decorView insetsHeight $insetsHeight")
                // ごにょごにょ
//            view.onApplyWindowInsets(insets.toWindowInsets())
                WindowInsetsCompat.CONSUMED
            }

            setOnShowListener {
//                container.fitsSystemWindows = false
//                container.clipChildren = false
//                container.clipToPadding = false
//                coordinator.fitsSystemWindows = false
//                coordinator.clipChildren = false
//                coordinator.clipToPadding = false
//                bottomSheet.fitsSystemWindows = false
//                bottomSheet.clipChildren = false
//                bottomSheet.clipToPadding = false

                // 1. BottomSheetDialogのコンテンツ表示部分のlayout_heightにmatch_parentを指定する
                bottomSheet.layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT
                bottomSheet.layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT

                Log.e(TAG, "${bottomSheet.layoutParams.width}")
                Log.e(TAG, "${bottomSheet.layoutParams.height}")

                // 2. BottomSheetBehaviorのpeekHeightを画面の縦幅と一致させる
                val metrics = context.resources.displayMetrics
                val activityScreenWidth = requireActivity().resources.displayMetrics.widthPixels / metrics.density
                val activityScreenHeight = requireActivity().resources.displayMetrics.heightPixels / metrics.density
                val screenWidth = context.resources.displayMetrics.widthPixels / metrics.density
                val screenHeight = context.resources.displayMetrics.heightPixels / metrics.density

                Log.e(TAG, "activityScreenWidth $activityScreenWidth")
                Log.e(TAG, "activityScreenHeight $activityScreenHeight")
                Log.e(TAG, "screenWidth $screenWidth")
                Log.e(TAG, "screenHeight $screenHeight")
            }
        }
    }

    companion object {
        const val TAG = "NormalBottomSheetDialogFragment"

        fun AppCompatActivity.findFragment(): NormalBottomSheetDialogFragment {
            return (supportFragmentManager.findFragmentByTag(this::class.java.simpleName) as? NormalBottomSheetDialogFragment)
                ?: NormalBottomSheetDialogFragment()
        }
    }
}
