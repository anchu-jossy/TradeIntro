package com.techxform.tradintro.feature_main.presentation.change_password

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
class ChangePasswordViewModel @Inject constructor(private val repository: ApiRepository) :
    ViewModel() {

    private var _changePasswordLiveData = MutableLiveData<BaseResponse<LoginResponse>>()
    val changePasswordLiveData: LiveData<BaseResponse<LoginResponse>> = _changePasswordLiveData

    private var _changePasswordErrorLiveData = MutableLiveData<Failure>()
    val changePasswordErrorLiveData: LiveData<Failure> = _changePasswordErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData


    fun changePassword(changePasswordRequest: ChangePasswordRequest) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.changePassword(changePasswordRequest)) {
                is Result.Success -> {
                    _changePasswordLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _changePasswordErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }
}