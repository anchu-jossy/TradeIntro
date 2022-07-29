package com.techxform.tradintro.feature_main.presentation.landing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.BaseResponse
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.LogOutRequest
import com.techxform.tradintro.feature_main.data.remote.dto.Result
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

}