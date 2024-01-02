package com.example.daggerhilttest.data.remote

import android.content.Context
import com.example.daggerhilttest.model.Task
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow


class NetworkService(private val client: HttpClient) {
    /*private val httpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun fetchTasks(): Flow<List<Task>> {
        return client
            .get("https://jsonplaceholder.typicode.com/users")
    }*/
}