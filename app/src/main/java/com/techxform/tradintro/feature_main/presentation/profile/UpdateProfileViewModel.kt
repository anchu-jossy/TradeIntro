package com.techxform.tradintro.feature_main.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    private var _userDetailLiveData = MutableLiveData<BaseResponse<UserDetailsResponse>>()
    val userDetailLiveData: LiveData<BaseResponse<UserDetailsResponse>> = _userDetailLiveData



    private var _userDetailErrorLiveData = MutableLiveData<Failure>()
    val portfolioErrorLiveData: LiveData<Failure> = _userDetailErrorLiveData


    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData


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