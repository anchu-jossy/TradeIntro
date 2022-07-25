package com.techxform.tradintro.feature_main.presentation.referal

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
class MyReferalViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {
    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData


    private var _addUserLiveData = MutableLiveData<AddUserResponse>()
    val addUseLiveData: LiveData<AddUserResponse> =
        _addUserLiveData

    private var _walletErrorLiveData = MutableLiveData<Failure>()
    val walletErrorLiveData: LiveData<Failure> = _walletErrorLiveData

    private var _updateUserListtLiveData = MutableLiveData<BaseResponse<ArrayList<InviteData>>>()
    val updateUserListLiveData: LiveData<BaseResponse<ArrayList<InviteData>>> =
        _updateUserListtLiveData
    fun addUser(addUserRequest: AddUserRequest) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.addUser(addUserRequest)) {
                is Result.Success -> {
                    _addUserLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _walletErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

    fun getUserInviteList() {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.findUserInviteList()) {
                is Result.Success -> {
                    _updateUserListtLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _walletErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }
}