package com.techxform.tradintro.feature_main.presentation.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.BaseResponse
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Notifications
import com.techxform.tradintro.feature_main.data.remote.dto.Result
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val repository: ApiRepository) :
    ViewModel() {

    private var _notificationLiveData = MutableLiveData<BaseResponse<ArrayList<Notifications>>>()
    val notificationLiveData: LiveData<BaseResponse<ArrayList<Notifications>>> =
        _notificationLiveData

    private var _deleteNotificationLiveData =
        MutableLiveData<Int>()
    val deleteNotificationLiveData: LiveData<Int> =
        _deleteNotificationLiveData


    private var _notificationErrorLiveData = MutableLiveData<Failure>()
    val notificationErrorLiveData: LiveData<Failure> = _notificationErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData


    fun notifications(searchModel: SearchModel) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.notifications(searchModel)) {
                is Result.Success -> {
                    _notificationLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _notificationErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

    fun deleteNotifications(pos:Int,notificationId: Int) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.deleteNotification(notificationId)) {
                is Result.Success -> {
                    _deleteNotificationLiveData.postValue(pos)
                }
                is Result.Error -> {
                    _notificationErrorLiveData.postValue(result.exception)
                }
            }

        }
    }

    fun dismissLoading(){
        _loadingLiveData.postValue(false)
    }


}