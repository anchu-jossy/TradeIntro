package com.techxform.tradintro.feature_main.presentation.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class PortfoliosViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    private var _portfolioLiveData = MutableLiveData<BaseResponse<ArrayList<PortfolioItem>>>()
    val portfolioLiveData: LiveData<BaseResponse<ArrayList<PortfolioItem>>> = _portfolioLiveData

    private var _portfolioTransactionLiveData = MutableLiveData<BaseResponse<ArrayList<PortfolioItem>>>()
    val portfolioTransactionLiveData: LiveData<BaseResponse<ArrayList<PortfolioItem>>> = _portfolioTransactionLiveData


    private var selectedPortfolioItem: PortfolioItem? = null


    private var _portfolioDashboardLiveData = MutableLiveData<BaseResponse<PortfolioDashboard>>()
    val portfolioDashboardLiveData: LiveData<BaseResponse<PortfolioDashboard>> =
        _portfolioDashboardLiveData

    private var _portfolioDashboardOfStockLiveData = MutableLiveData<BaseResponse<StockDashboard>>()
    val portfolioDashboardOfStockLiveData: LiveData<BaseResponse<StockDashboard>> =
        _portfolioDashboardOfStockLiveData

    private var _portfolioErrorLiveData = MutableLiveData<Failure>()
    val portfolioErrorLiveData: LiveData<Failure> = _portfolioErrorLiveData

    private var _portfolioDashboardErrorLiveData = MutableLiveData<Failure>()
    val portfolioDashboardErrorLiveData: LiveData<Failure> = _portfolioDashboardErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private var _loadingSearchLiveData = MutableLiveData<Boolean>()
    val loadingSearchLiveData: LiveData<Boolean> = _loadingSearchLiveData

    private var _modifyAlertPriceLiveData = MutableLiveData<BaseResponse<AlertPriceResponse>>()
    val modifyAlertPriceLiveData: LiveData<BaseResponse<AlertPriceResponse>> = _modifyAlertPriceLiveData

    private var _modifyAlertPriceErrorLiveData = MutableLiveData<Failure>()
    val modifyAlertPriceErrorLiveData: LiveData<Failure> = _modifyAlertPriceErrorLiveData

    private var _deleteAlertPriceLiveData = MutableLiveData<BaseResponse<DeleteAlertPriceResponse>>()
    val deleteAlertPriceLiveData: LiveData<BaseResponse<DeleteAlertPriceResponse>> = _deleteAlertPriceLiveData

    private var _deleteAlertPriceErrorLiveData = MutableLiveData<Failure>()
    val deleteAlertPriceErrorLiveData: LiveData<Failure> = _deleteAlertPriceErrorLiveData

    private lateinit var job: Job

    fun clearTransactionList(){
        _portfolioTransactionLiveData.postValue(null)
    }

    fun modifyPortfolioAlertPrice(orderId:Int, alertPriceRequest: AlertPriceRequest)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.modifyPortfolioAlertPrice(orderId, alertPriceRequest)) {
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
            when (val result = repository.deletePortfolioAlert(notificationId)) {
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


    fun portfolioListV2(searchModel: SearchModel, showLoading: Boolean) {

        _loadingSearchLiveData.postValue(showLoading)
        if (::job.isInitialized && job.isActive)
            runBlocking {
                job.cancelAndJoin()
            }
        job = viewModelScope.launch(Dispatchers.Default) {
            delay(500L)
            //_loadingLiveData.postValue(true)
            when (val result = repository.portfolioV2(searchModel)) {
                is Result.Success -> {
                    _portfolioLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _portfolioErrorLiveData.postValue(result.exception)
                }
            }

        }


    }


    fun portfolioListV2Transaction(searchModel: SearchModel, showLoading: Boolean) {

        _loadingSearchLiveData.postValue(showLoading)
        if (::job.isInitialized && job.isActive)
            runBlocking {
                job.cancelAndJoin()
            }
        job = viewModelScope.launch(Dispatchers.Default) {
            delay(500L)
            //_loadingLiveData.postValue(true)
            when (val result = repository.portfolioV2(searchModel)) {
                is Result.Success -> {
                    _portfolioTransactionLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _portfolioErrorLiveData.postValue(result.exception)
                }
            }

        }


    }

    fun portfolioDashboardV2() {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.portfolioDashboardV2()) {
                is Result.Success -> {
                    _portfolioDashboardLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _portfolioDashboardErrorLiveData.postValue(result.exception)
                }
            }

        }
    }

    /*   fun portfolioDashboard() {
           _loadingLiveData.postValue(true)
           viewModelScope.launch(Dispatchers.Default) {
               when (val result = repository.portfolioDashboard()) {
                   is Result.Success -> {
                       _portfolioDashboardLiveData.postValue(result.data)
                   }
                   is Result.Error -> {
                       _portfolioDashboardErrorLiveData.postValue(result.exception)
                   }
               }
           }
       }*/

    fun portfolioDashboardOfStock(stockId: Int) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.portfolioDashboardOfStockV2(stockId)) {
                is Result.Success -> {
                    _portfolioDashboardOfStockLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _portfolioDashboardErrorLiveData.postValue(result.exception)
                }
            }

        }
    }

    fun dismissLoading() {
        _loadingLiveData.postValue(false)
        _loadingSearchLiveData.postValue(false)
    }

    fun setSelectedPortfolioItem(portfolioItem: PortfolioItem?) {
        selectedPortfolioItem = portfolioItem
    }

    fun clearPortfolioList() {
        _portfolioLiveData.value?.data?.clear()
    }

    fun getSelectedPortfolio(): PortfolioItem? {
        return selectedPortfolioItem
    }


    fun isStockSelected(): Boolean {
        return selectedPortfolioItem != null
    }


}