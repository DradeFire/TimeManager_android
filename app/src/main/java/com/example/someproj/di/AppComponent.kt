package com.example.someproj.di

import com.example.someproj.MainStartActivity
import com.example.someproj.roomdb.database.TaskDatabase
import com.example.someproj.roomdb.query.TaskDao
import com.example.someproj.roomdb.repository.TaskRepository
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.app.Application
import com.example.someproj.viewmodel.DataModel
import com.example.someproj.fragments.task_manager.ListTaskFragment
import com.example.someproj.fragments.time_manager.TimeManagerFragment
import com.example.someproj.fragments.timer.TimerFragment
import dagger.BindsInstance
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class App: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .context(this)
            .database(database = TaskDatabase.getDatabase(this))
            .build()
    }
}

@Singleton
@Component(modules = [AllModules::class])
interface AppComponent {

    fun inject(activity: MainStartActivity)
    fun inject(dataModel: DataModel)
    fun inject(listTaskFragment: ListTaskFragment)
    fun inject(timeManagerFragment: TimeManagerFragment)
    fun inject(timerFragment: TimerFragment)

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun context(application: Application): Builder

        @BindsInstance
        fun database(database: TaskDatabase): Builder

        fun build(): AppComponent
    }

}

@Module(includes = [MainActivityModule::class, TimeManagerFragmentModule::class])
@InstallIn(SingletonComponent::class)
class AllModules

@Module
@InstallIn(SingletonComponent::class)
class MainActivityModule


@Module
@InstallIn(SingletonComponent::class)
class TimeManagerFragmentModule {

    @Provides
    @Singleton
    fun provideCalendar(): Calendar = Calendar.getInstance()

}

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    @Singleton
    fun dataBaseModule(application: Application): TaskDatabase {
        return TaskDatabase.getDatabase(application)
    }

    @Provides
    @Singleton
    fun provideTaskDao(
        database: TaskDatabase
    ): TaskDao =
        database.taskDao()

    @Provides
    @Singleton
    fun provideTaskRepository(
        database: TaskDatabase
    ): TaskRepository =
        TaskRepository(database)
}

