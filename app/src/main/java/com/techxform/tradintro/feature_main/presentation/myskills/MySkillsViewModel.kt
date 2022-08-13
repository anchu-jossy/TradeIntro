package com.techxform.tradintro.feature_main.presentation.myskills

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
class MySkillsViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    private var _userLevelsLiveData = MutableLiveData<BaseResponse<UserLevels>>()
    val userLevelsLiveData: LiveData<BaseResponse<UserLevels>> = _userLevelsLiveData

    private var _userLevelsErrorLiveData = MutableLiveData<Failure>()
    val userLevelsErrorLiveData: LiveData<Failure> = _userLevelsErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private var _userDetailLiveData = MutableLiveData<BaseResponse<UserDetailsResponse>>()
    val userDetailLiveData: LiveData<BaseResponse<UserDetailsResponse>> = _userDetailLiveData

    private var _userDetailErrorLiveData = MutableLiveData<Failure>()
    val portfolioErrorLiveData: LiveData<Failure> = _userDetailErrorLiveData
    fun userLevels()
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.userLevels()) {
                is Result.Success -> {
                    _userLevelsLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _userLevelsErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }
    fun userDetails() {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.userDetails()) {
                is Result.Success -> {
                    _userDetailLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _userDetailErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }

}
