package ru.netology.nmedia.dao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.db.AppDb

@InstallIn(SingletonComponent::class)
@Module
object PostWorkDaoModule {
    @Provides
    fun providePostWorkDao(db: AppDb): PostWorkDao = db.postWorkDao()
}