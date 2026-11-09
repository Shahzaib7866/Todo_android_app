package com.example.todoapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(entities = [TodoEntity::class], version = 1, exportSchema = true)
@Database(entities = [TodoEntity::class], version = 1)
    abstract class TodoDatabase : RoomDatabase() {
        abstract fun todoDao(): TodoDao

        //singleton ka mutlab aik hi instance ho ga, database ka pori app mein
        //companion object ka use hum singleton pattern bnany k liye krty hain
         companion object {
             @Volatile
             private var INSTANCE: TodoDatabase? = null

            fun getDatabase(context: Context): TodoDatabase {
                return INSTANCE ?: synchronized(this) {
                    Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "todo_db"
                    ).fallbackToDestructiveMigration()
                        .build()
                        .also {
                        INSTANCE = it
                    }
                }
            }
                }
            }
//real Android apps mein application-wide ek hi database instance rakhne ke liye yahi Singleton (@Volatile + synchronized + fallbackToDestructiveMigration) approach use ki jati hai.
//fallbackToDestructiveMigration() es ka maqsad hai k agr hum version 1 se barhaty hain tu purani table delete ho kr new bn jahy
//first step is to make a entity class and dao class then database
            //synchronized block is liye use huta hai k 2 threads aik sath database mangyin tu 2 seperate instances na bnyin
//alvis operator kiya huta hai? roman urdu
//Programming (khaas taur par Kotlin) mein Elvis Operator (?:) ek shortcut tareeqa hai null safety handle karne ka. Iska main maqsad yeh hota hai ke agar kisi variable ki value null ho, to aap usay ek default value assign kar saken









