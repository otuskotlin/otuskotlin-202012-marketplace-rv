package ru.otus.otuskotlin

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import ru.otus.otuskotlin.marketplace.transport.models.arts.*
import ru.otus.otuskotlin.marketplace.transport.models.common.MpMessage
import ru.otus.otuskotlin.marketplace.transport.models.workshops.*

val jsonConfig: Json by lazy {
    Json {
        prettyPrint = true
        serializersModule = SerializersModule {
            polymorphic(MpMessage::class) {
                subclass(MpRequestArtCreate::class)
                subclass(MpRequestArtRead::class)
                subclass(MpRequestArtDelete::class)
                subclass(MpRequestArtUpdate::class)
                subclass(MpRequestArtList::class)
                subclass(MpResponseArtCreate::class)
                subclass(MpResponseArtRead::class)
                subclass(MpResponseArtDelete::class)
                subclass(MpResponseArtUpdate::class)
                subclass(MpResponseArtList::class)

                subclass(MpRequestWorkshopCreate::class)
                subclass(MpRequestWorkshopRead::class)
                subclass(MpRequestWorkshopDelete::class)
                subclass(MpRequestWorkshopUpdate::class)
                subclass(MpRequestWorkshopList::class)
                subclass(MpResponseWorkshopCreate::class)
                subclass(MpResponseWorkshopRead::class)
                subclass(MpResponseWorkshopDelete::class)
                subclass(MpResponseWorkshopUpdate::class)
                subclass(MpResponseWorkshopList::class)
            }

        }
        classDiscriminator = "type"
    }
}