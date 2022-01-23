package com.example.someproj.roomdb.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.someproj.consts.Const

@Entity(tableName = Const.NAME_OF_DATABASE_TABLE)
data class Task (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val task: String,
    val time: String
)
