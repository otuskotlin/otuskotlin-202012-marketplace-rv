package ru.otus.otuskotlin.marketplace.backend.repository.cassandra.arts

import com.datastax.oss.driver.api.mapper.annotations.*
import com.google.common.util.concurrent.ListenableFuture

@Dao
interface ArtByIdCassandraDao {
    @Insert
    @StatementAttributes(consistencyLevel = "ONE")
    fun createAsync(dto: ArtByIdCassandraDto): ListenableFuture<Unit>

    @Select
    fun readAsync(id: String): ListenableFuture<ArtByIdCassandraDto?>

    @Update(customIfClause = "${ArtByIdCassandraDto.LOCK_VERSION} = :lock_key")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun updateAsync(dto: ArtByIdCassandraDto, @CqlName("lock_key") lockKey: String): ListenableFuture<Boolean>

    @Delete(ifExists = true, entityClass = [ArtByIdCassandraDto::class])
    fun deleteAsync(id: String): ListenableFuture<Boolean>
}

@Dao
interface ArtByTitleCassandraDao {
    @Insert
    @StatementAttributes(consistencyLevel = "ONE")
    fun createAsync(dto: ArtByTitleCassandraDto): ListenableFuture<Unit>

    @Select(
        customWhereClause = "${ArtByTitleCassandraDto.TITLE_INDEX} LIKE :filter",
    )
    fun filterByTitleAsync(filter: String): ListenableFuture<Collection<ArtByTitleCassandraDto>>

    @Delete
    fun deleteAsync(dto: ArtByTitleCassandraDto): ListenableFuture<Unit>
}
