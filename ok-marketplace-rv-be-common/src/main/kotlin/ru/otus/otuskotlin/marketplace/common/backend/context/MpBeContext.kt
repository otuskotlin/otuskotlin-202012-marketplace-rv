package ru.otus.otuskotlin.marketplace.common.backend.context

import ru.otus.otuskotlin.marketplace.common.backend.models.*
import java.time.Instant

data class MpBeContext(
    var status: MpBeContextStatus = MpBeContextStatus.NONE,
    var errors: MutableList<IMpError> = mutableListOf(),
    var stubCase: MpStubCase = MpStubCase.NONE,
    var timeStarted: Instant = Instant.MIN,
    var responseId: String = "",
    var onRequest: String = "",
    var frameworkErrors: MutableList<Throwable> = mutableListOf(),

    var requestArtId: MpArtIdModel = MpArtIdModel.NONE,
    var requestArt: MpArtModel = MpArtModel.NONE,
    var requestArtFilter: MpArtFilterModel = MpArtFilterModel.NONE,
    var requestWorkshopId: MpWorkshopIdModel = MpWorkshopIdModel.NONE,
    var requestWorkshop: MpWorkshopModel = MpWorkshopModel.NONE,
    var requestWorkshopFilter: MpWorkshopFilterModel = MpWorkshopFilterModel.NONE,

    var responseArt: MpArtModel = MpArtModel.NONE,
    var responseWorkshop: MpWorkshopModel = MpWorkshopModel.NONE,
    var responseArts: MutableList<MpArtModel> = mutableListOf(),
    var responseWorkshops: MutableList<MpWorkshopModel> = mutableListOf(),
)