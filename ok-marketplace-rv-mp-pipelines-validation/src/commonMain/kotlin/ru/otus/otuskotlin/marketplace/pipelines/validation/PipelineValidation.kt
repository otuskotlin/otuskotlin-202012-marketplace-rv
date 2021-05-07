package ru.otus.otuskotlin.marketplace.pipelines.validation

import ru.otus.otuskotlin.marketplace.pipelines.kmp.IOperation

class PipelineValidation<C>(
    private val validations: List<IValidationOperation<C,*>>
) : IOperation<C> {
    override suspend fun execute(context: C) {
        validations.forEach {
            it.execute(context)
        }
    }

}
