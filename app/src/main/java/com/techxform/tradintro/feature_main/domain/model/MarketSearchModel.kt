package com.techxform.tradintro.feature_main.domain.model

data class MarketSearchModel(
    var searchText: String = "",
    var limit: Int = 10,
    var offset: Int = 0,
    var skip: Int = 0
) {
}