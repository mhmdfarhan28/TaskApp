package com.example.daggerhilttest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.daggerhilttest.ui.navigation.AddTask
import com.example.daggerhilttest.ui.navigation.EditTask
import com.example.daggerhilttest.ui.navigation.ListOfTask
import com.example.daggerhilttest.ui.navigation.filteredMenuItems
import com.example.daggerhilttest.ui.screens.AddTaskScreen
import com.example.daggerhilttest.ui.screens.EditTaskScreen
import com.example.daggerhilttest.ui.screens.TaskListScreen
import com.example.daggerhilttest.ui.theme.DaggerHiltTestTheme
import com.example.daggerhilttest.viewmodel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyActivity : ComponentActivity() {

    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DaggerHiltTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MyNavigation(viewModel, navController)
                }
            }
        }
    }
}

@Composable
fun MyNavigation(
    viewModel: MyViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = ListOfTask.route
    ){
        composable(route = ListOfTask.route){
            TaskListScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = AddTask.route){
            AddTaskScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = EditTask.route){
            EditTaskScreen(navController = navController, viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(
    navController: NavController,
    viewModel: MyViewModel,
    screenContent: @Composable () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedMenuItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                filteredMenuItems.forEachIndexed { index, menuItem ->
                    NavigationDrawerItem(
                        label = {
                            Text(text = menuItem.title)
                        },
                        selected = index == selectedMenuItemIndex,
                        onClick = {
                            selectedMenuItemIndex = index
                            viewModel.setCompletedStatus(menuItem.completedTask)
                            viewModel.setTitleScreen(menuItem.title)
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = { 
                            Icon(
                                imageVector = if(index == selectedMenuItemIndex) {
                                    menuItem.selectedIcon
                                } else menuItem.unselectedIcon,
                                contentDescription = menuItem.title
                            )
                        },
                        badge = {
                            Text(text = viewModel.getFilteredTaskSize(menuItem.completedTask).toString())
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
            filteredMenuItems
        },
        drawerState = drawerState,
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(navController, scrollBehavior, viewModel){
                    scope.launch {
                        drawerState.open()
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                LargeFloatingActionButton(onClick = {
                    navController.navigate(AddTask.route) }
                )
                {
                    Icon(Icons.Filled.Add, "Floating action button.")
                }
            }

        ) { innerPadding ->
            Column(
                // consume insets as scaffold doesn't do it by default
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ){
                screenContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    viewModel: MyViewModel,
    onIconButtonClick: () -> Unit
) {
    /*var title by remember {
        mutableStateOf("")
    }
    LaunchedEffect(viewModel.titleScreen) {
        viewModel.titleScreen.collectLatest { newTitle ->
            title = newTitle
        }
    }*/
    // Use remember to store the title separately
    var title by remember { mutableStateOf("") }

    // Observe the title using collectLatest
    LaunchedEffect(viewModel) {
        viewModel.titleScreen.collectLatest { newTitle ->
            title = newTitle
        }
    }

    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            when (title) {
                "Add task", "Edit task" -> {
                    IconButton(onClick = {
                        viewModel.setTitleScreen("All tasks")
                        navController.navigateUp() }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
                else -> {
                    IconButton(onClick = onIconButtonClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}