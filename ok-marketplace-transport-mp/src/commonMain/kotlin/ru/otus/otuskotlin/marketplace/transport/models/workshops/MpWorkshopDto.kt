package ru.otus.otuskotlin.marketplace.transport.models.workshops

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.marketplace.transport.models.common.*

@Serializable
data class MpShopDto(
    override val id: String? = null,
    override val title: String? = null,
    override val description: String? = null,
    override val tagIds: Set<String>? = null,
) : IMpThingDto

@Serializable
data class MpShopCreateDto(
    override val title: String? = null,
    override val description: String? = null,
    override val tagIds: Set<String>? = null,
): IMpCreateThingDto

@Serializable
data class MpShopUpdateDto(
    override val title: String? = null,
    override val description: String? = null,
    override val tagIds: Set<String>? = null,
    override val id: String? = null,
): IMpUpdateThingDto

// Requests

@Serializable
@SerialName("MpRequestShopCreate")
data class MpRequestShopCreate(
    override val requestId: String? = null,
    override val onRequestResponseId: String? = null,
    override val timeStart: String? = null,
    override val debug: IMpDebug? = null,
    val createData: MpShopCreateDto? = null,
)  : IMpRequest, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}
@Serializable
@SerialName("MpRequestShopRead")
data class MpRequestShopRead(
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
@SerialName("MpRequestShopUpdate")
data class MpRequestShopUpdate(
    override val requestId: String? = null,
    override val onRequestResponseId: String? = null,
    override val timeStart: String? = null,
    override val debug: IMpDebug? = null,
    val updateData: MpShopUpdateDto? = null,
) : IMpRequest, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}

@Serializable
@SerialName("MpRequestShopDelete")
data class MpRequestShopDelete(
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
@SerialName("MpResponseShopCreate")
data class MpResponseShopCreate(
    override val requestId: String? = null,
    override val onRequestResponseId: String? = null,
    override val timeStart: String? = null,
    override val debug: IMpDebug? = null,
    val workshop: MpShopDto? = null,
)  : IMpRequest, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}
@Serializable
@SerialName("MpResponseShopRead")
data class MpResponseShopRead(
    override val requestId: String? = null,
    override val onRequestResponseId: String? = null,
    override val timeStart: String? = null,
    override val debug: IMpDebug? = null,
    val workshop: MpShopDto? = null,
) : IMpRequest, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}

@Serializable
@SerialName("MpResponseShopUpdate")
data class MpResponseShopUpdate(
    override val requestId: String? = null,
    override val onRequestResponseId: String? = null,
    override val timeStart: String? = null,
    override val debug: IMpDebug? = null,
    val workshop: MpShopDto? = null,
) : IMpRequest, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}

@Serializable
@SerialName("MpResponseShopDelete")
data class MpResponseShopDelete(
    override val requestId: String? = null,
    override val onRequestResponseId: String? = null,
    override val timeStart: String? = null,
    override val debug: IMpDebug? = null,
    val workshop: MpShopDto? = null,
    val deleted: Boolean? = null,
) : IMpRequest, MpMessage() {

    @Serializable
    data class Debug(
        override val mode: MpWorkModeDto?
    ) : IMpDebug
}
