package ru.otus.otuskotlin.marketplace.backend.repository.cassandra.arts

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import java.util.*

@Entity
data class ArtByIdCassandraDto(
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
    fun toModel() = MpArtModel(
        id = id?.let { MpArtIdModel(it) }?: MpArtModel.NONE.id,
        title = title?: MpArtModel.NONE.title,
        description = description?: MpArtModel.NONE.description,
        tagIds = tagIds?.toMutableSet()?: MpArtModel.NONE.tagIds,
    )

    companion object {
        const val ARTS_TABLE_NAME = "arts_by_id"
        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val TAG_ID_LIST = "tag_ids"
        const val LOCK_VERSION = "lock_version"

        fun of(model: MpArtModel) = of(model, model.id.id)

        fun of(model: MpArtModel, id: String) = ArtByIdCassandraDto(
            id = id.takeIf { it != MpArtModel.NONE.id.id },
            title = model.title.takeIf { it != MpArtModel.NONE.title },
            description = model.description.takeIf { it != MpArtModel.NONE.description },
            tagIds = model.tagIds.takeIf { it != MpArtModel.NONE.tagIds },
            lockVersion = UUID.randomUUID().toString(),
        )
    }
}
