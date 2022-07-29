package com.techxform.tradintro.feature_main.domain.model

data class SearchModel(
    var searchText: String? = null,
    var limit: Int = 10,
    var offset: Int = 0,
    var skip: Int = 0,
    var type: String = "",
    var readStatus: Int = 0,
    var notificationStatus: Int = 0,
    var orderExecutionType: String = "1,0" ,
    var orderStatus: String = "0",
    var portfolioStatus: String = "0",
) {
}