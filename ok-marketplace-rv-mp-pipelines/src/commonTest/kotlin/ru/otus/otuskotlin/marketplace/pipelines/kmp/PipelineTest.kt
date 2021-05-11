package ru.otus.otuskotlin.marketplace.pipelines.kmp

import runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PipelineTest {
    @Test
    fun simplePipeline() {
        val givenOperation = operation<TestContext> {
            execute { s += "c" }
        }
        val givenPipeline = pipeline<TestContext> {
            execute { s = "a" }
            execute(givenOperation)
            operation {
                startIf {
                    println("Check a: $s")
                    s.isNotEmpty()
                }
                execute { s += "b" }
            }
        }
        val givenContext = TestContext()

        runBlockingTest {
            println("Starting test")
            givenPipeline.execute(givenContext)
            assertEquals("acb", givenContext.s)
            println("Test Done")
            println("Finish test")
        }
    }

    @Test
    fun nestedPipeline() {
        val givenOperation = operation<TestContext> {
            execute { s += "c" }
        }
        val givenPipeline = pipeline<TestContext> {
            execute { s = "a" }
            execute(givenOperation)
            pipeline {
                startIf {
                    println("Check a: $s")
                    s.isNotEmpty()
                }
                execute { s += "b" }
            }
        }
        val givenContext = TestContext()

        runBlockingTest {
            println("Starting test")
            givenPipeline.execute(givenContext)
            assertEquals("acb", givenContext.s)
            println("Test Done")
            println("Finish test")
        }
    }

    data class TestContext(
        var s: String = ""
    )
}