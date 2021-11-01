package ru.netology.nmedia.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PostRepositoryModule {
    @Singleton
    @Binds
    fun bindPostRepository(impl: PostRepositoryServerImpl): PostRepository
}