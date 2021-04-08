package ru.otus.otuskotlin.marketplace.transport.models.workshops

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpArtDto
import ru.otus.otuskotlin.marketplace.transport.models.common.*

@Serializable
data class MpWorkshopDto(
    override val id: String? = null,
    override val title: String? = null,
    override val description: String? = null,
    override val tagIds: Set<String>? = null,
    val arts: MutableSet<MpArtDto>? = null,
) : IMpThingDto

@Serializable
data class MpWorkshopCreateDto(
    override val title: String? = null,
    override val description: String? = null,
    override val tagIds: Set<String>? = null,
): IMpCreateThingDto

@Serializable
data class MpWorkshopUpdateDto(
    override val title: String? = null,
    override val description: String? = null,
    override val tagIds: Set<String>? = null,
    override val id: String? = null,
    val arts: MutableSet<MpArtDto>? = null,
): IMpUpdateThingDto

// Requests

@Serializable
@SerialName("MpRequestWorkshopCreate")
data class MpRequestWorkshopCreate(
    override val requestId: String? = null,
    override val onRequestResponseId: String? = null,
    override val timeStart: String? = null,
    override val debug: IMpDebug? = null,
    val createData: MpWorkshopCreateDto? = null,
)  : IMpRequest, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}
@Serializable
@SerialName("MpRequestWorkshopRead")
data class MpRequestWorkshopRead(
    override val requestId: String? = null,
    override val onRequestResponseId: String? = null,
    override val timeStart: String? = null,
    override val debug: IMpDebug? = null,
    val workshopId: String? = null,
) : IMpRequest, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}

@Serializable
@SerialName("MpRequestWorkshopUpdate")
data class MpRequestWorkshopUpdate(
    override val requestId: String? = null,
    override val onRequestResponseId: String? = null,
    override val timeStart: String? = null,
    override val debug: IMpDebug? = null,
    val updateData: MpWorkshopUpdateDto? = null,
) : IMpRequest, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}

@Serializable
@SerialName("MpRequestWorkshopDelete")
data class MpRequestWorkshopDelete(
    override val requestId: String? = null,
    override val onRequestResponseId: String? = null,
    override val timeStart: String? = null,
    override val debug: IMpDebug? = null,
    val workshopId: String? = null,
) : IMpRequest, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}

//Responses

@Serializable
@SerialName("MpResponseWorkshopCreate")
data class MpResponseWorkshopCreate(
    override val responseId: String? = null,
    override val onRequestId: String? = null,
    override val debug: IMpDebug? = null,
    override val responseStatus: MpResponseStatusDto? = null,
    override val errors: List<MpErrorDto>? = null,
    val workshop: MpWorkshopDto? = null,
)  : IMpResponse, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}
@Serializable
@SerialName("MpResponseWorkshopRead")
data class MpResponseWorkshopRead(
    override val responseId: String? = null,
    override val onRequestId: String? = null,
    override val debug: IMpDebug? = null,
    override val responseStatus: MpResponseStatusDto? = null,
    override val errors: List<MpErrorDto>? = null,
    val workshop: MpWorkshopDto? = null,
) : IMpResponse, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}

@Serializable
@SerialName("MpResponseWorkshopUpdate")
data class MpResponseWorkshopUpdate(
    override val responseId: String? = null,
    override val onRequestId: String? = null,
    override val responseStatus: MpResponseStatusDto? = null,
    override val errors: List<MpErrorDto>? = null,
    override val debug: IMpDebug? = null,
    val workshop: MpWorkshopDto? = null,
) : IMpResponse, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}

@Serializable
@SerialName("MpResponseWorkshopDelete")
data class MpResponseWorkshopDelete(
    override val responseId: String? = null,
    override val onRequestId: String? = null,
    override val responseStatus: MpResponseStatusDto? = null,
    override val errors: List<MpErrorDto>? = null,
    override val debug: IMpDebug? = null,
    val workshop: MpWorkshopDto? = null,
    val deleted: Boolean? = null,
) : IMpResponse, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}
