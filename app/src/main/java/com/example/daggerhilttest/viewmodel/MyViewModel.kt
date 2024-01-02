package com.example.daggerhilttest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daggerhilttest.data.di.TaskRepository
import com.example.daggerhilttest.model.Task
import com.example.daggerhilttest.ui.navigation.FilterCriteria
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private var _targetTasks = MutableStateFlow(1)
    val targetTasks: StateFlow<Int> = _targetTasks

    private val _filteredTasks = MutableStateFlow<List<Task>>(emptyList())
    val filteredTasks: StateFlow<List<Task>> = _filteredTasks

    // New StateFlow for filter criteria
    private val _filterCriteria = MutableStateFlow(FilterCriteria.SHOW_ALL)
    val filterCriteria: StateFlow<FilterCriteria> = _filterCriteria

    // New StateFlow for filter criteria
    private val _titleScreen = MutableStateFlow("All tasks")
    val titleScreen: StateFlow<String> = _titleScreen

    init {
        loadTask()
    }

    private fun loadTask() {
        viewModelScope.launch {
            taskRepository.getTask().collect() {
                _tasks.value = it
                updateFilteredTasks()
            }
        }
    }

    private fun updateFilteredTasks() {
        val filteredList = when (_filterCriteria.value) {
            FilterCriteria.SHOW_ALL -> _tasks.value
            FilterCriteria.SHOW_COMPLETED -> _tasks.value.filter { it.completed }
            FilterCriteria.SHOW_NOT_COMPLETED -> _tasks.value.filter { !it.completed }
        }
        _filteredTasks.value = filteredList
    }

    // Function to update the filter criteria
    fun setCompletedStatus(criteria: FilterCriteria) {
        _filterCriteria.value = criteria
        updateFilteredTasks()
    }

    fun setTargetTask(id: Int) {
        _targetTasks.value = id
    }

    fun setTitleScreen(title: String) {
        _titleScreen.value = title
    }

    fun getTaskById(id: Int): Task? {
        return _tasks.value.getOrNull(id)
    }
    fun getFilteredTaskSize(criteria: FilterCriteria): Int {
        val filteredList = when (criteria) {
            FilterCriteria.SHOW_ALL -> _tasks.value
            FilterCriteria.SHOW_COMPLETED -> _tasks.value.filter { it.completed }
            FilterCriteria.SHOW_NOT_COMPLETED -> _tasks.value.filter { !it.completed }
        }
        return filteredList.size
    }

    fun addTask(title: String, description: String, completed: Boolean) = viewModelScope.launch {
        val newTaskId = _tasks.value.lastOrNull()?.id?.plus(1) ?: 1
        taskRepository.addTask(Task(newTaskId,title, description,completed))
    }

    fun editTask(id: Int, title: String, description: String, completed: Boolean) = viewModelScope.launch {
        taskRepository.editTask(Task(id,title,description,completed))
    }

    fun deleteTask(id: Int) = viewModelScope.launch {
        taskRepository.deleteTaskById(id)
    }


}