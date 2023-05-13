package com.udb.defensa.di

import android.content.Context
import com.udb.defensa.data.SettingsDataStore
import com.udb.defensa.presentation.viewmodels.AuthViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext appContext: Context): SettingsDataStore {
        return SettingsDataStore(appContext)
    }

    @Provides
    fun provideAuthViewModel(
        @ApplicationContext appContext: Context,
        settingsDataStore: SettingsDataStore
    ): AuthViewModel {
        return AuthViewModel(appContext, settingsDataStore)
    }
}