package ru.netology.nmedia.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UserRepositoryModule {
    @Singleton
    @Binds
    fun bindUserRepository(impl: UsersRepositoryServerImpl): UsersRepository
}