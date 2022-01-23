package com.example.someproj.roomdb.query

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.someproj.roomdb.model.Task

@Dao
interface TaskDao  {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Task>>
}