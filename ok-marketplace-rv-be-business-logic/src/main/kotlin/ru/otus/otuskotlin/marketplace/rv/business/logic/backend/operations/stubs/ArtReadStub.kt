package ru.otus.otuskotlin.marketplace.rv.business.logic.backend.operations.stubs

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContextStatus
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpStubCase
import ru.otus.otuskotlin.marketplace.pipelines.kmp.IOperation
import ru.otus.otuskotlin.marketplace.pipelines.kmp.operation
import ru.otus.otuskotlin.marketplace.pipelines.kmp.pipeline

object ArtReadStub : IOperation<MpBeContext> by pipeline({
    startIf { stubCase != MpStubCase.NONE }

    operation {
        startIf { stubCase == MpStubCase.ART_READ_SUCCESS }
        execute {
            responseArt = MpArtModel(
                id = MpArtIdModel("some_id"),
                title = "some_title",
                description = "some_description",
                tagIds = mutableSetOf("tag1","tag2"),
            )
            status = MpBeContextStatus.FINISHING
        }
    }
})