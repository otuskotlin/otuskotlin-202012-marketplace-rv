package ru.otus.otuskotlin.marketplace.backend.repository.cassandra.arts

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import java.time.Instant

@Entity
data class ArtByTitleCassandraDto(
    @PartitionKey(0)
    @CqlName(TITLE)
    val title: String? = null,
    @ClusteringColumn(0)
    @CqlName(TIMESTAMP)
    val timestamp: Instant? = null,
    @ClusteringColumn(1)
    @CqlName(ID)
    val id: String? = null,
    @ClusteringColumn(2)
    @CqlName(TITLE_INDEX)
    val titleIndex: String? = null,
) {

    companion object {
        const val ARTS_TITLE_TABLE_NAME = "arts_by_title"
        const val ID = "id"
        const val TITLE = "title"
        const val TITLE_INDEX = "title_index"
        const val TIMESTAMP = "timestamp"

        fun of(model: MpArtModel) = of(model, model.id.id)

        fun of(model: MpArtModel, id: String) = ArtByTitleCassandraDto(
            id = id.takeIf { it != MpArtModel.NONE.id.id },
            title = model.title.takeIf { it != MpArtModel.NONE.title },
            titleIndex = model.title.takeIf { it != MpArtModel.NONE.title },
            timestamp = Instant.now()
        )
    }
}
