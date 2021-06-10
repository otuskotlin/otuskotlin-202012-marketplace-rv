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
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.operations.stubs.ArtReadStub

object ArtRead: IOperation<MpBeContext> by pipeline( {
    execute(InitializePipeline)
    execute(QuerySetWorkMode)
    execute(ArtReadStub)

    validation {
        validate<String?> {
            on {requestArtId.id}
            validator(
                ValidatorStringNonEmpty(
                    field = "art-id",
                    message = "Requested Art Id mustn't be empty"
                )
            )
        }
    }

    operation {
        startIf { status == MpBeContextStatus.RUNNING }
        execute {
            try {
                artRepo.read(this)
                status = MpBeContextStatus.FINISHING
            } catch (t: Throwable) {
                status = MpBeContextStatus.FAILING
                errors.add(
                    MpError(
                        code = "couldn't get art(repo error)",
                        message = t.message?:"")
                )
            }
        }
    }

    execute(CompletePipeline)
} )