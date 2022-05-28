package com.techxform.tradintro.feature_main.presentation.equality_place_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.model.FilterModel
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

    fun walletSummary(name: String)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.walletSummary(name)) {
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

}