package ru.otus.otuskotlin.marketplace.rv.business.logic.backend.operations.stubs

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContextStatus
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpStubCase
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopModel
import ru.otus.otuskotlin.marketplace.pipelines.kmp.IOperation
import ru.otus.otuskotlin.marketplace.pipelines.kmp.operation
import ru.otus.otuskotlin.marketplace.pipelines.kmp.pipeline

object WorkshopDeleteStub: IOperation<MpBeContext> by pipeline ({
    startIf { stubCase != MpStubCase.NONE }

    operation {
        startIf { stubCase == MpStubCase.WORKSHOP_DELETE_SUCCESS }
        execute {
            responseWorkshop = MpWorkshopModel(
                id = MpWorkshopIdModel("some_id"),
                title = "some_title",
                description = "some_description",
                tagIds = mutableSetOf("tag1","tag2"),
                arts = mutableSetOf(MpArtIdModel("1"), MpArtIdModel("2"))
            )
            status = MpBeContextStatus.FINISHING
        }
    }
})