package com.techxform.tradintro.feature_main.presentation.equality_place_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.model.FilterModel
import com.techxform.tradintro.feature_main.domain.model.PaymentType
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EqualityPlaceOrderViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    private var _sellStockLiveData = MutableLiveData<BaseResponse<PortfolioItem>>()
    val sellStockLiveData: LiveData<BaseResponse<PortfolioItem>> = _sellStockLiveData

    private var _portfolioErrorLiveData = MutableLiveData<Failure>()
    val portfolioErrorLiveData: LiveData<Failure> = _portfolioErrorLiveData

    private var _buyStockLiveData = MutableLiveData<BaseResponse<PortfolioItem>>()
    val buyStockLiveData: LiveData<BaseResponse<PortfolioItem>> = _buyStockLiveData
    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData
    private var _walletSummaryLiveData = MutableLiveData<BaseResponse<WalletSummaryResponse>>()
    val walletSummaryLiveData: LiveData<BaseResponse<WalletSummaryResponse>> = _walletSummaryLiveData

    private var _userDetailLiveData = MutableLiveData<BaseResponse<UserDetailsResponse>>()
    val userDetailLiveData: LiveData<BaseResponse<UserDetailsResponse>> = _userDetailLiveData

    private var _walletErrorLiveData = MutableLiveData<Failure>()
    val walletErrorLiveData: LiveData<Failure> = _walletErrorLiveData



    fun walletSummary(type:PaymentType)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.walletSummary(type)) {
                is Result.Success -> {

                    _walletSummaryLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _portfolioErrorLiveData.postValue(result.exception)
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
                    _portfolioErrorLiveData.postValue(result.exception)
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
                    _portfolioErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }


}