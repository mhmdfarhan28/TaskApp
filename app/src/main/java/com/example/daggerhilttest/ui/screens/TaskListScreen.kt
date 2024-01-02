package com.example.daggerhilttest.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.daggerhilttest.Greeting
import com.example.daggerhilttest.ui.navigation.EditTask
import com.example.daggerhilttest.viewmodel.MyViewModel

@Composable
fun TaskListScreen(
    navController: NavController,
    viewModel: MyViewModel
) {
    // val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val tasks  = viewModel.filteredTasks.collectAsState().value

    val listState = rememberLazyListState()

    Greeting(
        navController = navController,
        viewModel = viewModel
    ) {
        LazyColumn(
            state = listState
        ) {
            items(tasks) { task ->
                var currentTaskTitle by remember { mutableStateOf(task.title) }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (task.completed) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(CardDefaults.shape)
                        .clickable {
                            viewModel.setTargetTask(task.id)
                            navController.navigate(EditTask.route)
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = { viewModel.editTask(task.id,task.title,task.description,!task.completed) },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                Icon(
                                    imageVector = if (task.completed) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                                    contentDescription = "Favorite",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Column {
                                Text(
                                    text = task.title,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                )
                                Text(
                                    text = task.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                )
                            }
                        }
                    }
                    /*IconButton(
                        onClick = { viewModel.deleteTask(task.id) }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }*/
                }
            }
        }
    }
}