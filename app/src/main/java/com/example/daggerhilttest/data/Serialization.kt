/*package com.example.daggerhilttest.data

import com.example.daggerhilttest.model.Task
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskNetwork(
    @SerialName("tasks")
    val tasks: List<TasksNetwork>
)


@Serializable
data class TasksNetwork(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String
) {
    fun toTask() = Task(
        id,
        title,
        description,
    )

}*/