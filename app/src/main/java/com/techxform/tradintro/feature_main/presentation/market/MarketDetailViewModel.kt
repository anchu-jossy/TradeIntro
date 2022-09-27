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
    private var _createWatchListLiveData = MutableLiveData<BaseResponse<WatchList>>()
    val createWatchListLiveData: LiveData<BaseResponse<WatchList>> = _createWatchListLiveData


    private var _deleteWatchlistLiveData = MutableLiveData<BaseResponse<DeleteWatchListResponse>>()
    val deleteWatchlistLiveData: LiveData<BaseResponse<DeleteWatchListResponse>> = _deleteWatchlistLiveData


    private var _deleteWatchListErrorLiveData = MutableLiveData<Failure>()
    val deleteWatchListErrorLiveData: LiveData<Failure> = _deleteWatchListErrorLiveData

    private var _modifyAlertPriceLiveData = MutableLiveData<BaseResponse<AlertPriceResponse>>()
    val modifyAlertPriceLiveData: LiveData<BaseResponse<AlertPriceResponse>> = _modifyAlertPriceLiveData

    private var _deleteAlertPriceLiveData = MutableLiveData<BaseResponse<DeleteAlertPriceResponse>>()
    val deleteAlertPriceLiveData: LiveData<BaseResponse<DeleteAlertPriceResponse>> = _deleteAlertPriceLiveData

    private var _deleteAlertPriceErrorLiveData = MutableLiveData<Failure>()
    val deleteAlertPriceErrorLiveData: LiveData<Failure> = _deleteAlertPriceErrorLiveData

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



    fun modifyWatchListAlertPrice(watchListId:Int, alertPriceRequest: AlertPriceRequest)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.modifyWatchListAlertPrice(watchListId, alertPriceRequest)) {
                is Result.Success -> {
                    _modifyAlertPriceLiveData.postValue(result.data)
                }
                is Result.Error -> {
                    _modifyAlertPriceErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

    fun deleteAlertPrice(notificationId:Int)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.deleteWatchListAlert(notificationId)) {
                is Result.Success -> {
                    _deleteAlertPriceLiveData.postValue(result.data)
                }
                is Result.Error -> {
                    _deleteAlertPriceErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

}