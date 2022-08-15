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

    private var _portfolioLiveData = MutableLiveData<BaseResponse<PortfolioItem>>()
    val portfolioLiveData: LiveData<BaseResponse<PortfolioItem>> = _portfolioLiveData

    private var _portfolioErrorLiveData = MutableLiveData<Failure>()
    val portfolioErrorLiveData: LiveData<Failure> = _portfolioErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData
    private var _walletSummaryLiveData = MutableLiveData<BaseResponse<WalletSummaryResponse>>()
    val walletSummaryLiveData: LiveData<BaseResponse<WalletSummaryResponse>> = _walletSummaryLiveData
    private var _marketDetailLiveData = MutableLiveData<BaseResponse<Stock>>()
    val marketDetailLiveData: LiveData<BaseResponse<Stock>> = _marketDetailLiveData

    private var _userDetailLiveData = MutableLiveData<BaseResponse<UserDetailsResponse>>()
    val userDetailLiveData: LiveData<BaseResponse<UserDetailsResponse>> = _userDetailLiveData

    private var _updateWalletLiveData = MutableLiveData<UpdateWalletResponse>()
    val updateWalletLiveData: LiveData<UpdateWalletResponse> =
        _updateWalletLiveData
    private var _walletErrorLiveData = MutableLiveData<Failure>()
    val walletErrorLiveData: LiveData<Failure> = _walletErrorLiveData

    fun portfolioDetails(id:Int,filterModel: FilterModel) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.portfolioDetails(id, filterModel)) {
                is Result.Success -> {
                    _portfolioLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _portfolioErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }

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
    fun marketDetail(marketId:Int)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.marketDetails(marketId)) {
                is Result.Success -> {
                    _marketDetailLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _portfolioErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }


    fun updateWallet(updateWalletRequest: UpdateWalletRequest) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.updateWallet(updateWalletRequest)) {
                is Result.Success -> {
                    _updateWalletLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _walletErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }


    fun userDetails() {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.userDetails()) {
                is Result.Success -> {
                    _userDetailLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _portfolioErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }
}