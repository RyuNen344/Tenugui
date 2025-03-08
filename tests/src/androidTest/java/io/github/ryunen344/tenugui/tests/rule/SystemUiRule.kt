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

package io.github.ryunen344.tenugui.tests.rule

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class SystemUiRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return GrantPermissionRule
            .grant(Manifest.permission.DUMP, Manifest.permission.WRITE_SECURE_SETTINGS)
            .apply(
                object : Statement() {
                    override fun evaluate() {
                        val context = ApplicationProvider.getApplicationContext<Context>()
                        try {
                            val instrumentation = InstrumentationRegistry.getInstrumentation()
                            UiDevice.getInstance(instrumentation).waitForIdle()
                            context.demoMode(true)
                            context.sendOrderedBroadcast(
                                listOf(
                                    Intent(ACTION_SYSTEM_UI_DEMO_MODE).apply { putExtras(COMMAND_ENTER) },
                                    Intent(ACTION_SYSTEM_UI_DEMO_MODE).apply { putExtras(COMMAND_STATUS) },
                                    Intent(ACTION_SYSTEM_UI_DEMO_MODE).apply { putExtras(COMMAND_NOTIFICATIONS) },
                                    Intent(ACTION_SYSTEM_UI_DEMO_MODE).apply { putExtras(COMMAND_CLOCK) },
                                ),
                            )
                            UiDevice.getInstance(instrumentation).waitForIdle()
                            base.evaluate()
                        } finally {
                            context.sendBroadcast(Intent(ACTION_SYSTEM_UI_DEMO_MODE).apply { putExtras(COMMAND_EXIT) })
                            context.demoMode(false)
                        }
                    }
                },
                description,
            )
    }

    private fun Context.sendOrderedBroadcast(intents: List<Intent>) {
        val countDownLatch = CountDownLatch(intents.size)
        intents.forEach { intent ->
            sendOrderedBroadcast(
                intent,
                null,
                object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        countDownLatch.countDown()
                    }
                },
                null,
                0,
                null,
                null,
            )
        }
        countDownLatch.await(10, TimeUnit.SECONDS)
    }

    private fun Context.demoMode(enable: Boolean) {
        Settings.Global.putInt(contentResolver, SYSTEM_UI_DEMO_ALLOWED, if (enable) 1 else 0)
        Settings.Global.putInt(contentResolver, SYSTEM_UI_TUNER_DEMO_ON, if (enable) 1 else 0)
    }

    private companion object {
        const val SYSTEM_UI_DEMO_ALLOWED = "sysui_demo_allowed"
        const val SYSTEM_UI_TUNER_DEMO_ON = "sysui_tuner_demo_on"
        const val ACTION_SYSTEM_UI_DEMO_MODE = "com.android.systemui.demo"

        val COMMAND_ENTER = Bundle().apply { putString("command", "enter") }
        val COMMAND_EXIT = Bundle().apply { putString("command", "exit") }
        val COMMAND_STATUS = Bundle().apply {
            putString("command", "status")
            putString("volume", "hide")
            putString("bluetooth", "hide")
            putString("location", "hide")
            putString("alarm", "hide")
            putString("sync", "hide")
            putString("tty", "hide")
            putString("eri", "hide")
            putString("mute", "hide")
            putString("speakerphone", "hide")
        }
        val COMMAND_NOTIFICATIONS = Bundle().apply {
            putString("command", "notifications")
            putString("visible", "false")
        }
        val COMMAND_CLOCK = Bundle().apply {
            putString("command", "clock")
            putString("hhmm", "0941")
        }
    }
}
