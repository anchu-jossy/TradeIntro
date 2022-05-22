package com.techxform.tradintro.feature_main.domain.model

data class FilterModel(
    var searchText: String = "",
    var limit: Int = 10,
    var offset: Int = 0,
    var skip: Int = 0,
    var order:String
    //var fields: Fields
)  {
}

data class Fields(
    var orderId: Boolean = true,
    var orderStockId : Boolean = true,
    var orderUserId: Boolean = true,
    var orderNo : Boolean= true,
    var orderPrice: Boolean = true,
    var totalStockValue : Boolean= true,
    var brokerage: Boolean = true,
    var transactionCharge: Boolean = true,
    var orderTotal: Boolean = true,
    var orderQty: Boolean = true,
    var orderCreatedOn : Boolean= true,
    var orderExecutedOn: Boolean= true,
    var orderType:Boolean = true,
    var orderExecutionType:Boolean = true,
    var orderStatus:Boolean = true,
    var marketStatus:Boolean = true,
    var portfolioStatus:Boolean = true,
    var orderValidity:Boolean = true,
    var order_validity_date:Boolean = true,
    var order_email_status:Boolean = true,
){}