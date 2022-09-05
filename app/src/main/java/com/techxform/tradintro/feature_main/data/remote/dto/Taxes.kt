package com.techxform.tradintro.feature_main.data.remote.dto

data class TaxModel(
    val tax_id: Int,
    val tax_name: String,
    val tax_status: Int,
    val tax_value: Int
)