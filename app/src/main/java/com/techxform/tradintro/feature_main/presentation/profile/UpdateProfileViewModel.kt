package com.techxform.tradintro.feature_main.presentation.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.model.PaymentType
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(private val repository: ApiRepository) :
    ViewModel() {

    var profileImageUri: Uri? = null
    private var _userDetailLiveData = MutableLiveData<BaseResponse<UserDetailsResponse>>()
    val userDetailLiveData: LiveData<BaseResponse<UserDetailsResponse>> = _userDetailLiveData

    private var _deleteAccountLiveData = MutableLiveData<BaseResponse<Any>>()
    val deleteAccountLiveData: LiveData<BaseResponse<Any>> = _deleteAccountLiveData


    private var _userDetailErrorLiveData = MutableLiveData<Failure>()
    val portfolioErrorLiveData: LiveData<Failure> = _userDetailErrorLiveData
    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData
    private var _walletSummaryLiveData = MutableLiveData<BaseResponse<WalletSummaryResponse>>()
    val walletSummaryLiveData: LiveData<BaseResponse<WalletSummaryResponse>> =
        _walletSummaryLiveData
    private var _updateWalletLiveData = MutableLiveData<UpdateWalletResponse>()
    val updateWalletLiveData: LiveData<UpdateWalletResponse> =
        _updateWalletLiveData
    private var _walletErrorLiveData = MutableLiveData<Failure>()
    val walletErrorLiveData: LiveData<Failure> = _walletErrorLiveData


    var taxAmount = 0

    var otherCharges = 0

    fun userDetails() {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.userDetails()) {
                is Result.Success -> {
                    _userDetailLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _userDetailErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }


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

    fun taxes() {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.taxes()) {
                is Result.Success -> {
                    if (result.data.data.isEmpty()) {
                        taxAmount = 0
                        otherCharges = 0
                    } else {
                        if (result.data.data.size == 1) {
                            taxAmount = result.data.data[0].tax_value
                        } else {
                            for (tax in result.data.data) {
                                if (tax.tax_name.contains("tax", true)) {
                                    taxAmount = tax.tax_value
                                } else {
                                    otherCharges = tax.tax_value
                                }
                            }

                        }
                    }
                }
                is Result.Error -> {
                    taxAmount = 0
                    otherCharges = 0
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

    fun editUser(editUserProfileReq: EditUserProfileReq) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.editProfile(editUserProfileReq)) {
                is Result.Success -> {
                    _userDetailLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _userDetailErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

    fun deleteUser() {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.deleteProfile()) {
                is Result.Success -> {
                    _deleteAccountLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _userDetailErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

}