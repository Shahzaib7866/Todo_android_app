package com.example.todoapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    // Naya todo insert karne ke liye (Agar ID pehle se hui toh error dega)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity)

    // ================= READ =================

    // Saare todos ko list ki shakal mein live (Flow) lene ke liye
    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    fun getAllTodos(): Flow<List<TodoEntity>>

    // Kisi aik specific todo ko uski ID se dhoondne ke liye
    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: Int): TodoEntity?

    // ================= UPDATE =================

    // Pehle se mojood todo ko update karne ke liye
    @Update
    suspend fun update(todo: TodoEntity)

    // Kisi specific todo object ko delete karne ke liye
    @Delete
    suspend fun delete(todo: TodoEntity)

    // Sirf ID ki madad se todo delete karne ke liye
    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteById(id: Int)
}