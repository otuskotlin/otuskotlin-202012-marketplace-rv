package ru.otus.otuskotlin.marketplace.backend.repository.cassandra.workshops

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopModel
import java.time.Instant

@Entity
data class WorkshopByTitleCassandraDto(
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
        const val WORKSHOPS_TITLE_TABLE_NAME = "workshops_by_title"
        const val ID = "id"
        const val TITLE = "title"
        const val TITLE_INDEX = "title_index"
        const val TIMESTAMP = "timestamp"

        fun of(model: MpWorkshopModel) = of(model, model.id.id)

        fun of(model: MpWorkshopModel, id: String) = WorkshopByTitleCassandraDto(
            id = id.takeIf { it != MpWorkshopModel.NONE.id.id },
            title = model.title.takeIf { it != MpWorkshopModel.NONE.title },
            titleIndex = model.title.takeIf { it != MpWorkshopModel.NONE.title },
            timestamp = Instant.now()
        )
    }
}
