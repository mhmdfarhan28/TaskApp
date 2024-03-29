package com.example.daggerhilttest.data.di

import com.example.daggerhilttest.viewmodel.MyViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideMyViewModel(
        taskRepository: TaskRepository
    ): MyViewModel {
        return MyViewModel(taskRepository)
    }

}