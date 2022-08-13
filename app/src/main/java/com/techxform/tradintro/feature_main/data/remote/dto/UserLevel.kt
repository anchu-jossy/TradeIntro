import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2022 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class UserLevel (

	@SerializedName("user_level_id") val user_level_id : Int,
	@SerializedName("user_level_name") val user_level_name : String,
	@SerializedName("user_level_image") val user_level_image : String,
	@SerializedName("user_level_points") val user_level_points : Int,
	@SerializedName("user_level_feature") val user_level_feature : String,
	@SerializedName("earn_points") val earn_points : String,
	@SerializedName("level_position") val level_position : Int,
	@SerializedName("login_point") val login_point : Int,
	@SerializedName("max_login_point") val max_login_point : Int,
	@SerializedName("recharge_pts") val recharge_pts : Int,
	@SerializedName("max_recharge_pts") val max_recharge_pts : Int,
	@SerializedName("recharge_unit") val recharge_unit : Int,
	@SerializedName("pts_per_buy") val pts_per_buy : Int,
	@SerializedName("max_pts_per_buy") val max_pts_per_buy : Int,
	@SerializedName("pts_per_unit_buy_trade_value") val pts_per_unit_buy_trade_value : Int,
	@SerializedName("unit_buy_trade_value") val unit_buy_trade_value : Int,
	@SerializedName("max_pts_buy_trade_value") val max_pts_buy_trade_value : Int,
	@SerializedName("pts_per_stock") val pts_per_stock : Int,
	@SerializedName("max_pts_per_stock") val max_pts_per_stock : Int,
	@SerializedName("pts_per_referal") val pts_per_referal : Int,
	@SerializedName("max_pts_per_referal") val max_pts_per_referal : Int,
	@SerializedName("pts_per_watchlist") val pts_per_watchlist : Int,
	@SerializedName("max_pts_per_watchlist") val max_pts_per_watchlist : Int
)