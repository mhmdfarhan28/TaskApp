package com.example.daggerhilttest.data.di

import com.example.daggerhilttest.data.local.TaskDao
import com.example.daggerhilttest.data.remote.NetworkService
import com.example.daggerhilttest.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
){
    fun getTask(): Flow<List<Task>> {
        return taskDao.getTask()
    }

    fun getTaskById(id: Int): Flow<Task> {
        return taskDao.getTaskById(id)
    }

    suspend fun addTask(task: Task){
        taskDao.addTask(task)
    }

    suspend fun editTask(task: Task){
        taskDao.editTask(task)
    }

    suspend fun deleteTaskById(id: Int){
        taskDao.deleteTaskById(id)
    }

}