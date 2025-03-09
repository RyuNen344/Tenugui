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

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class Retry(val times: Int = 3, val timeout: Long = 300)

class RetryException(val errors: List<Throwable>) : RuntimeException(
    buildString {
        appendLine("Invoked test still failed after ${errors.size} retries.")
        errors.forEachIndexed { index, t ->
            appendLine("Attempt #$index threw exception")
            appendLine(t.stackTraceToString())
        }
    },
)

class RetryRule : TestRule {

    private val errors = mutableListOf<Throwable>()

    private var attempt = 0

    override fun apply(base: Statement, description: Description): Statement {
        val retry = description.getAnnotation(Retry::class.java) ?: return base
        require(retry.times > 0)
        require(retry.timeout > 0)

        return object : Statement() {
            override fun evaluate() {
                while (attempt < retry.times) {
                    try {
                        base.evaluate()
                        return
                    } catch (t: Throwable) {
                        errors.add(t)
                        attempt++
                        Thread.sleep(retry.timeout)
                    }
                }
                throw RetryException(errors)
            }
        }
    }
}
