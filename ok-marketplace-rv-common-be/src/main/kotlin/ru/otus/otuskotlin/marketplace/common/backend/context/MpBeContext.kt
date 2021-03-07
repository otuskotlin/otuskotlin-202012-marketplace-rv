package ru.otus.otuskotlin.marketplace.common.backend.context

import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopModel

data class MpBeContext(
    var requestArtId: MpArtIdModel = MpArtIdModel.NONE,
    var requestArt: MpArtModel = MpArtModel.NONE,
    var requestWorkshopId: MpWorkshopIdModel = MpWorkshopIdModel.NONE,
    var requestWorkshop: MpWorkshopModel = MpWorkshopModel.NONE,

    var responseArt: MpArtModel = MpArtModel.NONE,
    var responseWorkshop: MpWorkshopModel = MpWorkshopModel.NONE,
)