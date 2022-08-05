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
            when (val result = repository.alertPriceWL(id, alertPriceRequest)) {
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