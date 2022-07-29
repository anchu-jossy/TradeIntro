package com.techxform.tradintro.feature_main.presentation.myskills

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.BaseResponse
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Result
import com.techxform.tradintro.feature_main.data.remote.dto.UserLevels
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


}
