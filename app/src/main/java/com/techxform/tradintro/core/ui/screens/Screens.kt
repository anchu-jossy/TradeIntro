package com.techxform.tradintro.core.ui.screens

sealed class Screens(val route: String){
    object HomeScreen: Screens("home_screen")
}
