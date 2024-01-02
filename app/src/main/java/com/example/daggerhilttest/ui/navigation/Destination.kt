package com.example.daggerhilttest.ui.navigation

interface Destination{
    val route: String
}
object ListOfTask: Destination {
    override val route = "ListOfTask"
}

object AddTask: Destination {
    override val route = "AddTask"
}

object EditTask: Destination {
    override val route = "EditTask"
}