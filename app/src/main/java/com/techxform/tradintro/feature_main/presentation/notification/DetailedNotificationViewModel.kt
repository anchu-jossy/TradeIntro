package com.techxform.tradintro.feature_main.presentation.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.BaseResponse
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Result
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedNotificationViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {
    private var _readNotificationErrorLiveData = MutableLiveData<Failure>()
    val readNotificationErrorLiveData: LiveData<Failure> = _readNotificationErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private var _readNotificationLiveData = MutableLiveData<BaseResponse<Int>>()
    val readNotificationLiveData: LiveData<BaseResponse<Int>> = _readNotificationLiveData


    fun readNotification(id:Int)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.readNotification(id)) {
                is Result.Success -> {
                    _readNotificationLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _readNotificationErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }




}