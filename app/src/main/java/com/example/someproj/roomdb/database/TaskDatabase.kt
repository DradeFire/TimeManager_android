package com.example.someproj.roomdb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.someproj.roomdb.model.Task
import com.example.someproj.roomdb.query.TaskDao
import com.example.someproj.consts.Const

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object{
        @Volatile
        var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            val tempInstance = INSTANCE

            if(tempInstance != null)
                return tempInstance

            synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    Const.NAME_OF_DATABASE
                ).build()
                INSTANCE = instance

                return instance
            }

        }
    }
}