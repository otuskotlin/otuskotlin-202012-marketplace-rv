package ru.otus.otuskotlin.marketplace.backend.repository.cassandra.arts

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace
import com.datastax.oss.driver.api.mapper.annotations.DaoTable
import com.datastax.oss.driver.api.mapper.annotations.Mapper

@Mapper
interface ArtCassandraMapper {

    @DaoFactory
    fun artByIdDao(
        @DaoKeyspace keyspace: String,
        @DaoTable table: String
    ): ArtByIdCassandraDao

    @DaoFactory
    fun artByTitleDao(
        @DaoKeyspace keyspace: String,
        @DaoTable table: String
    ): ArtByTitleCassandraDao
}
