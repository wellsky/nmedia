package ru.netology.nmedia.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostRemoteKeyDao
import ru.netology.nmedia.dao.PostWorkDao
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.PostRemoteKeyEntity
import ru.netology.nmedia.entity.PostWorkEntity

@Database(entities = [PostEntity::class, PostWorkEntity::class, PostRemoteKeyEntity::class], version = 3, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postWorkDao(): PostWorkDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
}