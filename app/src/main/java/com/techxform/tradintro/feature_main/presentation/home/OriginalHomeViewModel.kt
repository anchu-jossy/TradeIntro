package com.techxform.tradintro.feature_main.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.BaseResponse
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Result
import com.techxform.tradintro.feature_main.data.remote.dto.UserDashboard
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OriginalHomeViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {
    private var _userDashboardLiveData = MutableLiveData<BaseResponse<UserDashboard>>()
    val userDashboardLiveData: LiveData<BaseResponse<UserDashboard>> = _userDashboardLiveData


    private var _userDashboardErrorLiveData = MutableLiveData<Failure>()
    val userDashboardErrorLiveData: LiveData<Failure> = _userDashboardErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData


    fun userDashboard()
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.usersDashboard()) {
                is Result.Success -> {
                    _userDashboardLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _userDashboardErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }



}