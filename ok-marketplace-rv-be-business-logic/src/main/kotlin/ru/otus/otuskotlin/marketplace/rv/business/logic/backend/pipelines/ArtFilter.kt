package ru.otus.otuskotlin.marketplace.rv.business.logic.backend.pipelines

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.pipelines.kmp.IOperation
import ru.otus.otuskotlin.marketplace.pipelines.kmp.pipeline
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.operations.CompletePipeline
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.operations.InitializePipeline
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.operations.stubs.ArtFilterStub

object ArtFilter: IOperation<MpBeContext> by pipeline( {
    execute(InitializePipeline)
    execute(ArtFilterStub)
    execute(CompletePipeline)
} )