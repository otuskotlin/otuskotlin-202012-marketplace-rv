package ru.otus.otuskotlin.marketplace.backend.repository.cassandra.workshops

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace
import com.datastax.oss.driver.api.mapper.annotations.DaoTable
import com.datastax.oss.driver.api.mapper.annotations.Mapper

@Mapper
interface WorkshopCassandraMapper {

    @DaoFactory
    fun workshopByIdDao(
        @DaoKeyspace keyspace: String,
        @DaoTable table: String
    ): WorkshopByIdCassandraDao

    @DaoFactory
    fun workshopByTitleDao(
        @DaoKeyspace keyspace: String,
        @DaoTable table: String
    ): WorkshopByTitleCassandraDao
}
