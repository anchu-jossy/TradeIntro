package com.techxform.tradintro.feature_main.presentation.landing

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.core.utils.PreferenceHelper
import com.techxform.tradintro.core.utils.PreferenceHelper.userFullName
import com.techxform.tradintro.core.utils.PreferenceHelper.userId
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {


    private var _logOutErrorLiveData = MutableLiveData<Failure>()
    val logOutErrorLiveData: LiveData<Failure> = _logOutErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData
    private var _logOutLiveData = MutableLiveData<BaseResponse<Any>>()
    val logOutLiveData: MutableLiveData<BaseResponse<Any>> = _logOutLiveData

    private var _userDetailLiveData = MutableLiveData<BaseResponse<UserDetailsResponse>>()
    val userDetailLiveData: LiveData<BaseResponse<UserDetailsResponse>> = _userDetailLiveData

    fun logOut(request: LogOutRequest) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.logOut(
                request)) {
                is Result.Success -> {
                    _logOutLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _logOutErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }
    fun userDetails(context:Context) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.userDetails()) {
                is Result.Success -> {
                    val pref = PreferenceHelper.customPreference(context)
                    pref.userId = result.data.data.userId!!
                    pref.userFullName = result.data.data.userName
                    _userDetailLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _logOutErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }
}