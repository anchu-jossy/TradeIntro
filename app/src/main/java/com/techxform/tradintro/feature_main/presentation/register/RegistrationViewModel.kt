package com.techxform.tradintro.feature_main.presentation.register

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
class RegistrationViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {
    private var _registerLiveData = MutableLiveData<AddUserResponse>()
    val registerLiveData: LiveData<AddUserResponse> = _registerLiveData

    private var _registerErrorLiveData = MutableLiveData<Failure>()
    val registerErrorLiveData: LiveData<Failure> = _registerErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun register(request: RegisterRequest) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.register(request)) {
                is Result.Success -> {
                   // result.data.data.status
                    _registerLiveData.postValue(result.data)
                }
                is Result.Error -> {
                    _registerErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }




}