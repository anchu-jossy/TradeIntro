package com.techxform.tradintro.feature_main.data.remote.dto

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    object JsonParsing : Failure()
    data class FeatureFailure(val message: String):Failure()

    /** * Extend this class for feature specific failures.*/
    //abstract class FeatureFailure(message: String) : Failure()
}