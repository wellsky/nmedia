package ru.netology.nmedia.repository

import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FBModule {

    @Provides
    @Singleton
    fun provideFBMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Provides
    @Singleton
    fun provideGoogleApiAvail(): GoogleApiAvailability = GoogleApiAvailability.getInstance()
}