package ru.otus.otuskotlin.marketplace.rv.business.logic.backend.pipelines

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContextStatus
import ru.otus.otuskotlin.marketplace.common.backend.models.MpError
import ru.otus.otuskotlin.marketplace.common.kmp.validation.validators.ValidatorStringNonEmpty
import ru.otus.otuskotlin.marketplace.pipelines.kmp.IOperation
import ru.otus.otuskotlin.marketplace.pipelines.kmp.operation
import ru.otus.otuskotlin.marketplace.pipelines.kmp.pipeline
import ru.otus.otuskotlin.marketplace.pipelines.validation.validation
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.operations.CompletePipeline
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.operations.InitializePipeline
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.operations.QuerySetWorkMode
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.operations.stubs.WorkshopCreateStub

object WorkshopCreate: IOperation<MpBeContext> by pipeline( {
    execute(InitializePipeline)
    execute(QuerySetWorkMode)
    execute(WorkshopCreateStub)

    validation {
        validate<String?> {
            validator(ValidatorStringNonEmpty(field = "title", message = "you've to fill title"))
            on {requestWorkshop.title}
        }
    }

    operation {
        startIf { status == MpBeContextStatus.RUNNING }
        execute {
            try {
                workshopRepo.create(this)
                status = MpBeContextStatus.FINISHING
            } catch (t: Throwable) {
                status = MpBeContextStatus.FAILING
                errors.add(
                    MpError(
                        code = "couldn't create workshop(repo error)",
                        message = t.message?:"")
                )
            }
        }
    }

    execute(CompletePipeline)
} )