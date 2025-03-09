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

import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.ViewRootForInspector
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.motion.MaterialBackOrchestrator

public class BottomSheetDialogWrapper(
    private var onDismissRequest: () -> Unit,
    private var properties: BottomSheetDialogProperties,
    private val view: View,
    layoutDirection: LayoutDirection,
    density: Density,
    dialogId: String,
) : BottomSheetDialog(
    ContextThemeWrapper(
        view.context,
        if (!properties.decorFitsSystemWindows) {
            R.style.Theme_Tenugui_EdgeToEdge
        } else {
            R.style.Theme_Tenugui
        },
    ),
),
    ViewRootForInspector {

    private val dialogLayout: BottomSheetDialogLayout

    private val defaultSoftInputMode: Int

    override val subCompositionView: AbstractComposeView
        get() = dialogLayout

    override val viewRoot: View?
        get() = container

    /**
     * [BottomSheetDialog.coordinator]
     */
    public val coordinator: CoordinatorLayout?
        get() = findViewById(com.google.android.material.R.id.coordinator)

    /**
     * [BottomSheetDialog.container]
     */
    public val container: FrameLayout?
        get() = findViewById(com.google.android.material.R.id.container)

    /**
     * [BottomSheetDialog.bottomSheet]
     */
    public val bottomSheet: FrameLayout?
        get() = findViewById(com.google.android.material.R.id.design_bottom_sheet)

    init {
        val window = window ?: error("BottomSheetDialog has no window")
        defaultSoftInputMode = window.attributes.softInputMode and WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST
        window.setBackgroundDrawableResource(android.R.color.transparent)

        dialogLayout = BottomSheetDialogLayout(
            context = context,
            window = window,
        ).apply {
            setTag(androidx.compose.ui.R.id.compose_view_saveable_id_tag, "BottomSheetDialog:$dialogId")
            clipChildren = false
            with(density) { elevation = properties.elevation.toPx() }
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRect(0, 0, view.width, view.height)
                    outline.alpha = 0f
                }
            }
        }
        (window.decorView as? ViewGroup)?.disableClipping()
        setContentView(dialogLayout)

        // decorFitsSystemWindows
        bottomSheet?.fitsSystemWindows = properties.decorFitsSystemWindows
        if (!properties.decorFitsSystemWindows) {
            bottomSheet?.let {
                /**
                 * consume default windowInset
                 * [BottomSheetBehavior.setWindowInsetsListener]
                 */
                ViewCompat.setOnApplyWindowInsetsListener(it) { _, windowInsets -> windowInsets }
            }
        }

        dialogLayout.setViewTreeLifecycleOwner(view.findViewTreeLifecycleOwner())
        dialogLayout.setViewTreeViewModelStoreOwner(view.findViewTreeViewModelStoreOwner())
        dialogLayout.setViewTreeSavedStateRegistryOwner(view.findViewTreeSavedStateRegistryOwner())
        updateParameters(onDismissRequest, properties, layoutDirection)

        onBackPressedDispatcher.addCallback(this) {
            if (properties.cancelable && properties.dismissOnBackPress) {
                if (behavior.skipCollapsed) {
                    dismissInternal()
                } else {
                    when (behavior.state) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            behavior.state = if (behavior.isFitToContents) {
                                BottomSheetBehavior.STATE_COLLAPSED
                            } else {
                                BottomSheetBehavior.STATE_HALF_EXPANDED
                            }
                        }

                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        }

                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            dismissInternal()
                        }

                        BottomSheetBehavior.STATE_HIDDEN -> {
                            onDismissRequest()
                        }

                        BottomSheetBehavior.STATE_DRAGGING,
                        BottomSheetBehavior.STATE_SETTLING,
                        -> {
                            // noop
                        }
                    }
                }
            } else {
                when (behavior.state) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        behavior.state = if (behavior.isFitToContents) {
                            BottomSheetBehavior.STATE_COLLAPSED
                        } else {
                            BottomSheetBehavior.STATE_HALF_EXPANDED
                        }
                    }

                    BottomSheetBehavior.STATE_HALF_EXPANDED,
                    BottomSheetBehavior.STATE_HIDDEN,
                    -> {
                        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }

                    BottomSheetBehavior.STATE_COLLAPSED,
                    BottomSheetBehavior.STATE_DRAGGING,
                    BottomSheetBehavior.STATE_SETTLING,
                    -> {
                        // noop
                    }
                }
            }
        }
    }

    // BottomSheetBehavior.STATE_HIDDEN or click outside
    override fun cancel() {
        if (properties.cancelable) {
            dismissInternal()
        } else {
            behavior.state = behavior.state.takeIf {
                it != BottomSheetBehavior.STATE_HIDDEN &&
                    it != BottomSheetBehavior.STATE_DRAGGING &&
                    it != BottomSheetBehavior.STATE_SETTLING
            } ?: BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun setContentView(layoutResID: Int) {
        ensureContainerAndListenableBehavior()
        super.setContentView(layoutResID)
    }

    override fun setContentView(view: View) {
        ensureContainerAndListenableBehavior()
        super.setContentView(view)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        ensureContainerAndListenableBehavior()
        super.setContentView(view, params)
    }

    public fun setContent(parentComposition: CompositionContext, children: @Composable () -> Unit) {
        dialogLayout.setContent(parentComposition, children)
    }

    public fun updateParameters(
        onDismissRequest: () -> Unit,
        properties: BottomSheetDialogProperties,
        layoutDirection: LayoutDirection,
    ) {
        this.onDismissRequest = onDismissRequest
        this.properties = properties

        // BottomSheetDialog
        setCanceledOnTouchOutside(properties.canceledOnTouchOutside)
        dismissWithAnimation = properties.dismissWithAnimation

        // securePolicy
        val secureFlagEnabled = properties.securePolicy.shouldApplySecureFlag(view.isFlagSecureEnabled())
        window?.setFlags(
            if (secureFlagEnabled) WindowManager.LayoutParams.FLAG_SECURE else WindowManager.LayoutParams.FLAG_SECURE.inv(),
            WindowManager.LayoutParams.FLAG_SECURE,
        )

        // layoutDirection
        dialogLayout.layoutDirection = when (layoutDirection) {
            LayoutDirection.Ltr -> android.util.LayoutDirection.LTR
            LayoutDirection.Rtl -> android.util.LayoutDirection.RTL
        }

        // softInputMode
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            if (!properties.decorFitsSystemWindows) {
                @Suppress("DEPRECATION")
                window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            } else {
                window?.setSoftInputMode(defaultSoftInputMode)
            }
        }
    }

    public fun disposeComposition() {
        dialogLayout.disposeComposition()
    }

    /**
     * call [BottomSheetDialog.ensureContainerAndBehavior] before inflate to attach custom behavior
     */
    @Suppress("PrivateResource", "RestrictedApi")
    private fun ensureContainerAndListenableBehavior() {
        /**
         * [BottomSheetDialog.container]
         */
        val container = View.inflate(context, com.google.android.material.R.layout.design_bottom_sheet_dialog, null)
        val containerField = this::class.java.superclass.getDeclaredField("container")
        containerField.isAccessible = true
        containerField.set(this, container)
        containerField.isAccessible = false

        /**
         * [BottomSheetDialog.coordinator]
         */
        val coordinator = container.findViewById<CoordinatorLayout>(com.google.android.material.R.id.coordinator)
        val coordinatorField = this::class.java.superclass.getDeclaredField("coordinator")
        coordinatorField.isAccessible = true
        coordinatorField.set(this, coordinator)
        coordinatorField.isAccessible = false

        /**
         * [BottomSheetDialog.bottomSheet]
         */
        val bottomSheet =
            container.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val bottomSheetField = this::class.java.superclass.getDeclaredField("bottomSheet")
        bottomSheetField.isAccessible = true
        bottomSheetField.set(this, bottomSheet)
        bottomSheetField.isAccessible = false

        /**
         * [BottomSheetDialog.behavior]
         */
        val listenableBehavior = ListenableBottomSheetBehavior(context)
        (bottomSheet.layoutParams as CoordinatorLayout.LayoutParams).behavior = listenableBehavior
        val behaviorField = this::class.java.superclass.getDeclaredField("behavior")
        behaviorField.isAccessible = true
        behaviorField.set(this, listenableBehavior)
        behaviorField.isAccessible = false

        /**
         * [BottomSheetDialog.bottomSheetCallback]
         */
        val bottomSheetCallbackFiled = this::class.java.superclass.getDeclaredField("bottomSheetCallback")
        bottomSheetCallbackFiled.isAccessible = true
        listenableBehavior.addBottomSheetCallback(bottomSheetCallbackFiled.get(this) as BottomSheetBehavior.BottomSheetCallback)
        bottomSheetCallbackFiled.isAccessible = false

        /**
         * [BottomSheetDialog.cancelable]
         */
        val cancelableField = this::class.java.superclass.getDeclaredField("cancelable")
        cancelableField.isAccessible = true
        listenableBehavior.setHideable(cancelableField.get(this) as Boolean)
        cancelableField.isAccessible = false

        /**
         * [BottomSheetDialog.backOrchestrator]
         */
        val backOrchestratorField = this::class.java.superclass.getDeclaredField("backOrchestrator")
        backOrchestratorField.isAccessible = true
        backOrchestratorField.set(this, MaterialBackOrchestrator(listenableBehavior, bottomSheet))
        backOrchestratorField.isAccessible = false
    }

    private fun ViewGroup.disableClipping() {
        clipChildren = false
        if (this is BottomSheetDialogLayout) return
        for (i in 0 until childCount) {
            (getChildAt(i) as? ViewGroup)?.disableClipping()
        }
    }

    /**
     * [androidx.compose.ui.window.shouldApplySecureFlag]
     */
    private fun SecureFlagPolicy.shouldApplySecureFlag(isSecureFlagSetOnParent: Boolean): Boolean {
        return when (this) {
            SecureFlagPolicy.SecureOff -> false
            SecureFlagPolicy.SecureOn -> true
            SecureFlagPolicy.Inherit -> isSecureFlagSetOnParent
        }
    }

    /**
     * [androidx.compose.ui.window.isFlagSecureEnabled]
     */
    private fun View.isFlagSecureEnabled(): Boolean {
        val windowParams = rootView.layoutParams as? WindowManager.LayoutParams
        if (windowParams != null) {
            return (windowParams.flags and WindowManager.LayoutParams.FLAG_SECURE) != 0
        }
        return false
    }

    private fun dismissInternal() {
        if (!properties.dismissWithAnimation || behavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            onDismissRequest()
        } else {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }
}
