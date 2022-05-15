package com.techxform.tradintro.feature_main.data.remote.dto

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure : Failure()
}