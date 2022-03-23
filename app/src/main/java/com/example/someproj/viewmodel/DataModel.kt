package com.example.someproj.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.someproj.di.App
import com.example.someproj.di.AppComponent
import com.example.someproj.roomdb.database.TaskDatabase
import com.example.someproj.roomdb.model.Task
import com.example.someproj.roomdb.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

val Context.appComponent: AppComponent
    get() = when(this){
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }

class DataModel @ViewModelInject constructor(application: Application): AndroidViewModel(application) {

    var second_time: Int? = null
    var second_running: Boolean? = null
    var second_startPause: String? = null
    var second_startPauseImg: Drawable? = null

    var countDown_timerInMills: Long? = null
    var countDown_isTurn: Boolean? = null
    var countDown_canStart: Boolean? = null
    var countDown_startStopbt: String? = null

    var readAllData: LiveData<List<Task>>
    @Inject
    lateinit var repository: TaskRepository
    @Inject
    lateinit var db: TaskDatabase

    val calendar: MutableLiveData<Calendar> by lazy {
        MutableLiveData<Calendar>()
    }
    val message: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    init {
        application.appComponent.inject(this)
        readAllData = repository.readAllData
    }

    fun addTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }
}