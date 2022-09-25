package com.techxform.tradintro.feature_main.presentation.watchlistview

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
class WatchlistViewViewModel @Inject constructor(private val repository: ApiRepository) :
    ViewModel() {
    private var _watchlistViewLiveData = MutableLiveData<BaseResponse<WatchList>>()
    val watchlistViewLiveData: LiveData<BaseResponse<WatchList>> = _watchlistViewLiveData

    private var _watchlistViewErrorLiveData = MutableLiveData<Failure>()
    val watchlistViewErrorLiveData: LiveData<Failure> = _watchlistViewErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private var _modifyAlertPriceLiveData = MutableLiveData<BaseResponse<AlertPriceResponse>>()
    val modifyAlertPriceLiveData: LiveData<BaseResponse<AlertPriceResponse>> = _modifyAlertPriceLiveData

    private var _modifyAlertPriceErrorLiveData = MutableLiveData<Failure>()
    val modifyAlertPriceErrorLiveData: LiveData<Failure> = _modifyAlertPriceErrorLiveData

    private var _buyStockLiveData = MutableLiveData<BaseResponse<PortfolioItem>>()
    val buyStockLiveData: LiveData<BaseResponse<PortfolioItem>> = _buyStockLiveData

    private var _buyStockErrorLiveData = MutableLiveData<Failure>()
    val buyStockErrorLiveData: LiveData<Failure> = _buyStockErrorLiveData

    private var _sellStockLiveData = MutableLiveData<BaseResponse<PortfolioItem>>()
    val sellStockLiveData: LiveData<BaseResponse<PortfolioItem>> = _sellStockLiveData

    private var _sellStockErrorLiveData = MutableLiveData<Failure>()
    val sellStockErrorLiveData: LiveData<Failure> = _sellStockErrorLiveData

    fun watchlistDetails(watchlistId: Int) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.watchlistDetail(watchlistId)) {
                is Result.Success -> {
                    _watchlistViewLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _watchlistViewErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

    fun setAlertPrice(id:Int, alertPriceRequest: AlertPriceRequest)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.modifyWatchListAlertPrice(id, alertPriceRequest)) {
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

    fun buyStock(marketId: Int, buySellStockReq: BuySellStockReq)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.buyStock(marketId, buySellStockReq)) {
                is Result.Success -> {
                    _buyStockLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _buyStockErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

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


}