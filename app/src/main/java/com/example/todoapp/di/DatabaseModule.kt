package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.database.dao.TodoDao
import com.example.todoapp.data.database.database.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    object DatabaseModule {
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): TodoDatabase =
            Room.databaseBuilder(context, TodoDatabase::class.java, "todo_db").build()

        @Provides
        fun provideDao(db: TodoDatabase): TodoDao = db.todoDao()
    }

}