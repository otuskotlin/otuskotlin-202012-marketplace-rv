package ru.otus.otuskotlin.marketplace.pipelines.validation

import ru.otus.otuskotlin.marketplace.common.kmp.validation.IValidationError
import ru.otus.otuskotlin.marketplace.common.kmp.validation.IValidator
import ru.otus.otuskotlin.marketplace.common.kmp.validation.ValidationResult
import ru.otus.otuskotlin.marketplace.common.kmp.validation.validators.ValidatorStringNonEmpty
import ru.otus.otuskotlin.marketplace.pipelines.kmp.*
import kotlin.test.Test
import kotlin.test.assertEquals
import runBlockingTest

internal class ValidationTest {

    @Test
    fun pipelineValidation() {
        val pipeline = pipeline<TestContext> {

            validation {
                errorHandler { v: ValidationResult ->
                    if (v.isSuccess) return@errorHandler
                    errors.addAll(v.errors)
                }

                validate<String?> { validator(ValidatorStringNonEmpty()); on { x } }
                validate<String?> { validator(ValidatorStringNonEmpty()); on { y } }
            }
        }
        runBlockingTest {
            val context = TestContext()
            pipeline.execute(context)
            assertEquals(2, context.errors.size)
        }
    }

    data class TestContext(
        val x: String = "",
        val y: String = "",
        val errors: MutableList<IValidationError> = mutableListOf()
    )
}