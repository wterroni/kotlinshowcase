package com.example.linky.util

import io.mockk.MockKAnnotations
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * JUnit rule to initialize MockK annotations in Android instrumentation tests
 */
class MockKRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                MockKAnnotations.init(this)
                base.evaluate()
            }
        }
    }
}
