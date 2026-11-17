package com.example.todoapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.data.database.dao.TodoDao
import com.example.todoapp.core.database.entity.TodoEntity

@Database(entities = [TodoEntity::class], version = 2, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
