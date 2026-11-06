package com.example.todoapp.di

import com.example.todoapp.data.repository.TodoRepositoryImpl
import com.example.todoapp.domain.repository.RepoInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTodoRepository(impl: TodoRepositoryImpl): RepoInterface
}


