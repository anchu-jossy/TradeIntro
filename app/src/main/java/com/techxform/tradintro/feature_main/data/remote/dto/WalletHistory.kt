package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WalletHistory(
    @SerializedName("wallet_id") val walletId: Int?,
    @SerializedName("wallet_user_id") val walletUserId: Int,
    @SerializedName("wallet_admin_user_id") val walletAdminUserId: Int?,
    @SerializedName("wallet_member_user_id") val walletMemberUserId: Int?,
    @SerializedName("transaction_type") val transactionType: String,
    @SerializedName("payment_order_id") val paymentOrderId: String?,
    @SerializedName("wallet_money") val walletMoney: Float?,
    @SerializedName("gst") val gst: Float?,
    @SerializedName("other_tax") val otherTax: Float?,
    @SerializedName("net_amount") val netAmount: Float?,
    @SerializedName("wallet_trade_value") val walletTradeValue: Float?,
    @SerializedName("voucher_code_id") val voucherCodeId: Int?,
    @SerializedName("remarks") val remarks: String?,
    @SerializedName("allocate_email_status") val allocateEmailStatus: Int?,
    @SerializedName("wallet_created_on") val walletCreatedOn: String?,
) {
}