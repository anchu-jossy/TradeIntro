package com.techxform.tradintro.feature_main.presentation.recharge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.BaseResponse
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Result
import com.techxform.tradintro.feature_main.data.remote.dto.WalletSummaryResponse
import com.techxform.tradintro.feature_main.domain.model.PaymentType
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RechargeViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {
    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private var _walletSummaryLiveData = MutableLiveData<BaseResponse<WalletSummaryResponse>>()
    val walletSummaryLiveData: LiveData<BaseResponse<WalletSummaryResponse>> = _walletSummaryLiveData

    private var _walletErrorLiveData = MutableLiveData<Failure>()
    val walletErrorLiveData: LiveData<Failure> = _walletErrorLiveData

    fun walletSummary(type: PaymentType) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.walletSummary(type)) {
                is Result.Success -> {
                    _walletSummaryLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _walletErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

}