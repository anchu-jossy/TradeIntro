package com.techxform.tradintro.feature_account.presentation.signin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.core.utils.PreferenceHelper
import com.techxform.tradintro.core.utils.PreferenceHelper.refreshToken
import com.techxform.tradintro.core.utils.PreferenceHelper.token
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {

    private var _loginLiveData = MutableLiveData<BaseResponse<LoginResponse>>()
    val loginLiveData: LiveData<BaseResponse<LoginResponse>> = _loginLiveData

    private var _loginErrorLiveData = MutableLiveData<Failure>()
    val loginErrorLiveData: LiveData<Failure> = _loginErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData


    private var _forgetPassword = MutableLiveData<String>()
    val forgetPasswordLiveData: LiveData<String> = _forgetPassword

    fun login(request: LoginRequest, context: Context) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.login(request)) {
                is Result.Success -> {
                    val pref =  PreferenceHelper.customPreference(context)
                    pref.token = result.data.data.token
                    pref.refreshToken = result.data.data.refreshToken

                    _loginLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _loginErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }

    fun forgetPassword(emailId :String){
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.forgetPassword(emailId)) {
                is Result.Success -> {

                        _forgetPassword.postValue(result.data.toString())

                }
                is Result.Error -> {
                    _loginErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

}