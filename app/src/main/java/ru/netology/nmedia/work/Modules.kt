//package ru.netology.nmedia.dao
//
//import androidx.work.WorkerParameters
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import ru.netology.nmedia.db.AppDb
//import ru.netology.nmedia.work.RefreshPostsWorker
//
//@InstallIn(SingletonComponent::class)
//@Module
//object WorkersModules {
//
//    @Provides
//    fun provideRefreshPostWorker(params: WorkerParameters): RefreshPostsWorker
//}