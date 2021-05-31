package ru.otus.otuskotlin.marketplace.backend.repository.cassandra.workshops

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopModel
import java.util.*

@Entity
data class WorkshopByIdCassandraDto(
    @PartitionKey
    @CqlName(ID)
    val id: String? = null,
    @CqlName(TITLE)
    val title: String? = null,
    @CqlName(DESCRIPTION)
    val description: String? = null,
    @CqlName(TAG_ID_LIST)
    val tagIds: Set<String>? = null,
    @CqlName(LOCK_VERSION)
    val lockVersion: String? = null,
) {
    fun toModel() = MpWorkshopModel(
        id = id?.let { MpWorkshopIdModel(it) }?: MpWorkshopModel.NONE.id,
        title = title?: MpWorkshopModel.NONE.title,
        description = description?: MpWorkshopModel.NONE.description,
        tagIds = tagIds?.toMutableSet()?: MpWorkshopModel.NONE.tagIds,
    )

    companion object {
        const val WORKSHOPS_TABLE_NAME = "workshops_by_id"
        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val TAG_ID_LIST = "tag_ids"
        const val LOCK_VERSION = "lock_version"

        fun of(model: MpWorkshopModel) = of(model, model.id.id)

        fun of(model: MpWorkshopModel, id: String) = WorkshopByIdCassandraDto(
            id = id.takeIf { it != MpWorkshopModel.NONE.id.id },
            title = model.title.takeIf { it != MpWorkshopModel.NONE.title },
            description = model.description.takeIf { it != MpWorkshopModel.NONE.description },
            tagIds = model.tagIds.takeIf { it != MpWorkshopModel.NONE.tagIds },
            lockVersion = UUID.randomUUID().toString(),
        )
    }
}
