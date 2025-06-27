package com.example.linky.util

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * JUnit rule to mock Android Log class for unit tests
 */
class MockLogRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                mockLog()
                base.evaluate()
            }
        }
    }

    companion object {
        /**
         * Mock all Log methods to prevent "Method not mocked" exceptions in tests
         */
        fun mockLog() {
            mockkStatic(Log::class)
            // Métodos com String como segundo parâmetro
            every { Log.v(any(), any<String>()) } returns 0
            every { Log.d(any(), any<String>()) } returns 0
            every { Log.i(any(), any<String>()) } returns 0
            every { Log.w(any(), any<String>()) } returns 0
            every { Log.e(any(), any<String>()) } returns 0
            
            // Métodos com Throwable como segundo parâmetro
            every { Log.w(any(), any<Throwable>()) } returns 0
            
            // Métodos com String e Throwable
            every { Log.v(any(), any<String>(), any<Throwable>()) } returns 0
            every { Log.d(any(), any<String>(), any<Throwable>()) } returns 0
            every { Log.i(any(), any<String>(), any<Throwable>()) } returns 0
            every { Log.w(any(), any<String>(), any<Throwable>()) } returns 0
            every { Log.e(any(), any<String>(), any<Throwable>()) } returns 0
        }
    }
}
