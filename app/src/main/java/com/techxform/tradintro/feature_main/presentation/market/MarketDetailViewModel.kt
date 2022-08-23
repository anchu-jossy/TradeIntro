package com.techxform.tradintro.feature_main.presentation.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketDetailViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    private var _marketDetailLiveData = MutableLiveData<BaseResponse<Stock>>()
    val marketDetailLiveData: LiveData<BaseResponse<Stock>> = _marketDetailLiveData

    private var _marketErrorLiveData = MutableLiveData<Failure>()
    val marketErrorLiveData: LiveData<Failure> = _marketErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private var _deleteWatchlistLiveData = MutableLiveData<BaseResponse<DeleteWatchListResponse>>()
    val deleteWatchlistLiveData: LiveData<BaseResponse<DeleteWatchListResponse>> = _deleteWatchlistLiveData

    private var _createWatchListLiveData = MutableLiveData<BaseResponse<WatchList>>()
    val createWatchListLiveData: LiveData<BaseResponse<WatchList>> = _createWatchListLiveData


    private var _buyStockErrorLiveData = MutableLiveData<Failure>()
    val buyStockErrorLiveData: LiveData<Failure> = _buyStockErrorLiveData

    private var _sellStockLiveData = MutableLiveData<BaseResponse<PortfolioItem>>()
    val sellStockLiveData: LiveData<BaseResponse<PortfolioItem>> = _sellStockLiveData

    private var _sellStockErrorLiveData = MutableLiveData<Failure>()
    val sellStockErrorLiveData: LiveData<Failure> = _sellStockErrorLiveData

    private var _deleteWatchListErrorLiveData = MutableLiveData<Failure>()
    val deleteWatchListErrorLiveData: LiveData<Failure> = _deleteWatchListErrorLiveData

    private var _modifyAlertPriceLiveData = MutableLiveData<BaseResponse<AlertPriceResponse>>()
    val modifyAlertPriceLiveData: LiveData<BaseResponse<AlertPriceResponse>> = _modifyAlertPriceLiveData

    private var _modifyAlertPriceErrorLiveData = MutableLiveData<Failure>()
    val modifyAlertPriceErrorLiveData: LiveData<Failure> = _modifyAlertPriceErrorLiveData


    fun marketDetail(marketId:Int)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.marketDetails(marketId)) {
                is Result.Success -> {
                    _marketDetailLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _marketErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

    fun createWatchList(watchlistId: CreateWatchListRequest) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.createWatchList(watchlistId)) {
                is Result.Success -> {
                    _createWatchListLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _marketErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }


    fun removeWatchlist(id: Int)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.deleteWatchList(id)) {
                is Result.Success -> {

                    _deleteWatchlistLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _deleteWatchListErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

//
//    fun buyStock(marketId: Int, buySellStockReq: BuySellStockReq)
//    {
//        _loadingLiveData.postValue(true)
//        viewModelScope.launch(Dispatchers.Default) {
//            when (val result = repository.buyStock(marketId, buySellStockReq)) {
//                is Result.Success -> {
//                    _buyStockLiveData.postValue(result.data!!)
//                }
//                is Result.Error -> {
//                    _buyStockErrorLiveData.postValue(result.exception)
//                }
//            }
//            _loadingLiveData.postValue(false)
//        }
//    }

    fun sellStock(marketId: Int, buySellStockReq: BuySellStockReq)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.sellStock(marketId, buySellStockReq)) {
                is Result.Success -> {
                    _sellStockLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _sellStockErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

    fun modifyAlertPrice(stockId:Int, alertPriceRequest: AlertPriceRequest)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.alertPrice(stockId, alertPriceRequest)) {
                is Result.Success -> {
                    _modifyAlertPriceLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _modifyAlertPriceErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

}