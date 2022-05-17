package com.techxform.tradintro.feature_main.data.remote.dto

sealed class Result<out R>{
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Failure) : Result<Nothing>()
   // class Loading(boolean: Boolean) : Result<Nothing>()
}


