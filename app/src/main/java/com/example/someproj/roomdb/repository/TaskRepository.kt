package com.example.someproj.roomdb.repository

import androidx.lifecycle.LiveData
import com.example.someproj.roomdb.database.TaskDatabase
import com.example.someproj.roomdb.model.Task
import javax.inject.Inject

class TaskRepository @Inject constructor(var database: TaskDatabase) {

    var readAllData: LiveData<List<Task>> = database.taskDao().readAllData()

    suspend fun addTask(task: Task){
        database.taskDao().addTask(task)
    }

    suspend fun deleteTask(task: Task){
        database.taskDao().deleteTask(task)
    }

}