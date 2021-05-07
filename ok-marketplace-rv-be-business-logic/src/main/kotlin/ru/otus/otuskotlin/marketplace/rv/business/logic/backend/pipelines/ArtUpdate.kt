package ru.otus.otuskotlin.marketplace.rv.business.logic.backend.pipelines

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.kmp.validation.validators.ValidatorStringNonEmpty
import ru.otus.otuskotlin.marketplace.pipelines.kmp.IOperation
import ru.otus.otuskotlin.marketplace.pipelines.kmp.pipeline
import ru.otus.otuskotlin.marketplace.pipelines.validation.validation
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.operations.CompletePipeline
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.operations.InitializePipeline
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.operations.stubs.ArtUpdateStub

object ArtUpdate: IOperation<MpBeContext> by pipeline( {
    execute(InitializePipeline)
    execute(ArtUpdateStub)


    validation {
        validate<String?> {
            validator(ValidatorStringNonEmpty(field = "title", message = "you've to fill title"))
            on {requestArt.title}
        }
    }


    execute(CompletePipeline)
} )