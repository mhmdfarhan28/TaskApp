package com.example.daggerhilttest.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val completed: Boolean
)
