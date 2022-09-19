package com.techxform.tradintro.feature_account.presentation.signin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.core.utils.PreferenceHelper
import com.techxform.tradintro.core.utils.PreferenceHelper.password
import com.techxform.tradintro.core.utils.PreferenceHelper.refreshToken
import com.techxform.tradintro.core.utils.PreferenceHelper.token
import com.techxform.tradintro.core.utils.PreferenceHelper.username
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

    private var _forgetPasswordLiveData = MutableLiveData<Any>()
    val forgetPasswordLiveData: LiveData<Any> = _forgetPasswordLiveData

    private var _resendEmailLiveData = MutableLiveData<Any>()
    val resendEmailLiveData: LiveData<Any> = _resendEmailLiveData

    private var _forgetPasswordErrorLiveData = MutableLiveData<Failure>()
    val forgetPasswordErrorLiveData: LiveData<Failure> = _forgetPasswordErrorLiveData

    private var _resentEmailErrorLiveData = MutableLiveData<Failure>()
    val resentEmailErrorLiveData: LiveData<Failure> = _resentEmailErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun loginRequestFromPref(context: Context, callback: (loginRequest: LoginRequest) -> Unit) {
        val pref = PreferenceHelper.customPreference(context)
        callback(LoginRequest(pref.username!!, pref.password!!))
    }

    fun login(request: LoginRequest, context: Context) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.login(request)) {
                is Result.Success -> {
                    if (result.data.data.token.isEmpty()) {
                        _loginErrorLiveData.postValue(Failure.FeatureFailure("VerifyEmail"))
                    } else {
                        val pref = PreferenceHelper.customPreference(context)
                        pref.token = result.data.data.token
                        pref.username = request.username
                        pref.password = request.password
                        pref.refreshToken = result.data.data.refreshToken
                        _loginLiveData.postValue(result.data)
                    }
                }
                is Result.Error -> {
                    _loginErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }

    fun forgetPassword(email: String) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.forgetPassword(email)) {
                is Result.Success -> {
                    _forgetPasswordLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _forgetPasswordErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

    fun resentEmail(email: String) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.resendEmail(email)) {
                is Result.Success -> {
                    _resendEmailLiveData.postValue(result.data)
                }
                is Result.Error -> {
                    _resentEmailErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }
}