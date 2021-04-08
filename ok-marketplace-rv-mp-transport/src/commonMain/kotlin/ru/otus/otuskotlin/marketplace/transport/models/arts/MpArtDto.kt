package ru.otus.otuskotlin.marketplace.transport.models.arts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.marketplace.transport.models.common.*

@Serializable
data class MpArtDto(
    override val id: String? = null,
    override val title: String? = null,
    override val description: String? = null,
    override val tagIds: Set<String>? = null,
) : IMpThingDto

@Serializable
data class MpArtCreateDto(
    override val title: String? = null,
    override val description: String? = null,
    override val tagIds: Set<String>? = null,
): IMpCreateThingDto

@Serializable
data class MpArtUpdateDto(
    override val title: String? = null,
    override val description: String? = null,
    override val tagIds: Set<String>? = null,
    override val id: String? = null,
): IMpUpdateThingDto

// Requests

@Serializable
@SerialName("MpRequestArtCreate")
data class MpRequestArtCreate(
    override val requestId: String? = null,
    override val onRequestResponseId: String? = null,
    override val timeStart: String? = null,
    override val debug: IMpDebug? = null,
    val createData: MpArtCreateDto? = null,
)  : IMpRequest, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}
@Serializable
@SerialName("MpRequestArtRead")
data class MpRequestArtRead(
    override val requestId: String? = null,
    override val onRequestResponseId: String? = null,
    override val timeStart: String? = null,
    override val debug: IMpDebug? = null,
    val artId: String? = null,
) : IMpRequest, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}

@Serializable
@SerialName("MpRequestArtUpdate")
data class MpRequestArtUpdate(
    override val requestId: String? = null,
    override val onRequestResponseId: String? = null,
    override val timeStart: String? = null,
    override val debug: IMpDebug? = null,
    val updateData: MpArtUpdateDto? = null,
) : IMpRequest, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}

@Serializable
@SerialName("MpRequestArtDelete")
data class MpRequestArtDelete(
    override val requestId: String? = null,
    override val onRequestResponseId: String? = null,
    override val timeStart: String? = null,
    override val debug: IMpDebug? = null,
    val artId: String? = null,
) : IMpRequest, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}

//Responses

@Serializable
@SerialName("MpResponseArtCreate")
data class MpResponseArtCreate(
    override val responseId: String? = null,
    override val onRequestId: String? = null,
    override val debug: IMpDebug? = null,
    override val responseStatus: MpResponseStatusDto? = null,
    override val errors: List<MpErrorDto>? = null,
    val art: MpArtDto? = null,
)  : IMpResponse, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}
@Serializable
@SerialName("MpResponseArtRead")
data class MpResponseArtRead(
    override val responseId: String? = null,
    override val onRequestId: String? = null,
    override val debug: IMpDebug? = null,
    override val responseStatus: MpResponseStatusDto? = null,
    override val errors: List<MpErrorDto>? = null,
    val art: MpArtDto? = null,
) : IMpResponse, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}

@Serializable
@SerialName("MpResponseArtUpdate")
data class MpResponseArtUpdate(
    override val responseId: String? = null,
    override val onRequestId: String? = null,
    override val debug: IMpDebug? = null,
    override val responseStatus: MpResponseStatusDto? = null,
    override val errors: List<MpErrorDto>? = null,
    val art: MpArtDto? = null,
) : IMpResponse, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}

@Serializable
@SerialName("MpResponseArtDelete")
data class MpResponseArtDelete(
    override val responseId: String? = null,
    override val onRequestId: String? = null,
    override val debug: IMpDebug? = null,
    override val responseStatus: MpResponseStatusDto? = null,
    override val errors: List<MpErrorDto>? = null,
    val art: MpArtDto? = null,
    val deleted: Boolean? = null,
) : IMpResponse, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}
