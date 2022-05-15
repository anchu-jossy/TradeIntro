package com.techxform.tradintro.feature_account.presentation.signin

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    private var _loginLiveData = MutableLiveData<BaseResponse<LoginResponse>>()
     val loginLiveData: LiveData<BaseResponse<LoginResponse>> = _loginLiveData

    private var _loginErrorLiveData = MutableLiveData<Failure>()
     val loginErrorLiveData: LiveData<Failure> = _loginErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
     val loadingLiveData: LiveData<Boolean> = _loadingLiveData


    fun login(request: LoginRequest) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.login(request)) {
                is Result.Success -> {  _loginLiveData.postValue(result.data!!) }
                is Result.Error -> { _loginErrorLiveData.postValue(result.exception)}
            }
            _loadingLiveData.postValue(false)
        }

    }

}