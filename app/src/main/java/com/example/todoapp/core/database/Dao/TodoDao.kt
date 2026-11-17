package com.example.todoapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.core.database.entity.TodoEntity
import androidx.lifecycle.LiveData

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity)

    @Query("SELECT * FROM Todos ORDER BY createdAt DESC")
    fun getAllTodos(): LiveData<List<TodoEntity>>

    @Query("SELECT * FROM Todos WHERE id = :id")
    suspend fun getTodoById(id: Int): TodoEntity?

    @Update
    suspend fun update(todo: TodoEntity)

    @Delete
    suspend fun delete(todo: TodoEntity)

    @Query("DELETE FROM Todos WHERE id = :id")
    suspend fun deleteById(id: Int)
}