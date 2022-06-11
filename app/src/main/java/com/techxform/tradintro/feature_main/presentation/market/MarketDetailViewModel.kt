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

    private var _updateWatchListLiveData = MutableLiveData<BaseResponse<UpdateData>>()
    val updateWatchListLiveData: LiveData<BaseResponse<UpdateData>> = _updateWatchListLiveData

    private var _createWatchListLiveData = MutableLiveData<BaseResponse<WatchList>>()
    val createWatchListLiveData: LiveData<BaseResponse<WatchList>> = _createWatchListLiveData

    private var _buyStockLiveData = MutableLiveData<BaseResponse<PortfolioItem>>()
    val buyStockLiveData: LiveData<BaseResponse<PortfolioItem>> = _buyStockLiveData

    private var _buyStockErrorLiveData = MutableLiveData<Failure>()
    val buyStockErrorLiveData: LiveData<Failure> = _buyStockErrorLiveData

    private var _sellStockLiveData = MutableLiveData<BaseResponse<PortfolioItem>>()
    val sellStockLiveData: LiveData<BaseResponse<PortfolioItem>> = _sellStockLiveData

    private var _sellStockErrorLiveData = MutableLiveData<Failure>()
    val sellStockErrorLiveData: LiveData<Failure> = _sellStockErrorLiveData

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
    fun updateWatchList(id: Number, amount: Number) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.updateWatchList(id,UpdateWatchListRequest(amount))) {
                is Result.Success -> {
                    _updateWatchListLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _marketErrorLiveData.postValue(result.exception)
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