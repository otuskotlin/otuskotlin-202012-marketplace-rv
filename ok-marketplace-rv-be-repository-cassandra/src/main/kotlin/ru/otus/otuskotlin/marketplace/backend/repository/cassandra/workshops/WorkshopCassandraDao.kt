package ru.otus.otuskotlin.marketplace.backend.repository.cassandra.workshops

import com.datastax.oss.driver.api.mapper.annotations.*
import com.google.common.util.concurrent.ListenableFuture

@Dao
interface WorkshopByIdCassandraDao {
    @Insert
    @StatementAttributes(consistencyLevel = "ONE")
    fun createAsync(dto: WorkshopByIdCassandraDto): ListenableFuture<Unit>

    @Select
    fun readAsync(id: String): ListenableFuture<WorkshopByIdCassandraDto?>

    /**
     *  В данном случае условие в Update избыточно, так как обновляется вся модель.
     *  Может быть нужно при обновлении отдельных полей
     */
    @Update(customIfClause = "${WorkshopByIdCassandraDto.LOCK_VERSION} = :lock_key")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun updateAsync(dto: WorkshopByIdCassandraDto, @CqlName("lock_key") lockKey: String): ListenableFuture<Boolean>

    /**
     *  При удалении по ключу требуется указание [entityClass], при удалении по всей модели
     *  класс не требуется указывать, он берется из модели
     */
    @Delete(ifExists = true, entityClass = [WorkshopByIdCassandraDto::class])
    fun deleteAsync(id: String): ListenableFuture<Boolean>
}

@Dao
interface WorkshopByTitleCassandraDao {
    @Insert
    @StatementAttributes(consistencyLevel = "ONE")
    fun createAsync(dto: WorkshopByTitleCassandraDto): ListenableFuture<Unit>

    @Select(
        customWhereClause = "${WorkshopByTitleCassandraDto.TITLE_INDEX} LIKE :filter",
    )
    fun filterByTitleAsync(filter: String): ListenableFuture<Collection<WorkshopByTitleCassandraDto>>

    @Delete
    fun deleteAsync(dto: WorkshopByTitleCassandraDto): ListenableFuture<Unit>
}
