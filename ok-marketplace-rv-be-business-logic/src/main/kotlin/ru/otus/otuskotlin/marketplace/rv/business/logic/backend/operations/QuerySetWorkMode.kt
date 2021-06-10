package ru.otus.otuskotlin.marketplace.rv.business.logic.backend.operations

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContextStatus
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkMode
import ru.otus.otuskotlin.marketplace.pipelines.kmp.IOperation
import ru.otus.otuskotlin.marketplace.pipelines.kmp.operation

object QuerySetWorkMode: IOperation<MpBeContext> by operation ({
    startIf { status == MpBeContextStatus.RUNNING }
    execute {
        artRepo = when(workMode) {
            MpWorkMode.TEST -> artRepoTest
            MpWorkMode.PROD -> artRepoProd
        }
        workshopRepo = when(workMode) {
            MpWorkMode.TEST -> workshopRepoTest
            MpWorkMode.PROD -> workshopRepoProd
        }
    }
})
