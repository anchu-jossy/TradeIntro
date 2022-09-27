package com.techxform.tradintro.feature_main.presentation.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.BaseResponse
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Result
import com.techxform.tradintro.feature_main.data.remote.dto.Stock
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {


    private var _marketListLiveData = MutableLiveData<BaseResponse<ArrayList<Stock>>>()
    val marketListLiveData: LiveData<BaseResponse<ArrayList<Stock>>> = _marketListLiveData

    private var _marketErrorLiveData = MutableLiveData<Failure>()
    val marketErrorLiveData: LiveData<Failure> = _marketErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private var _loadingSearchLiveData = MutableLiveData<Boolean>()
    val loadingSearchLiveData: LiveData<Boolean> = _loadingSearchLiveData


    private lateinit var job: Job


    fun marketList(searchModel: SearchModel,showLoading:Boolean) {
        _loadingSearchLiveData.postValue(showLoading)

        if (::job.isInitialized && job.isActive)
            runBlocking {
                job.cancelAndJoin()
            }
        job = viewModelScope.launch(Dispatchers.Default) {
            if (!searchModel.searchText.isNullOrEmpty())
                delay(500L)
            _loadingLiveData.postValue(!showLoading)
            when (val result = repository.marketList(searchModel)) {
                is Result.Success -> {
                    _marketListLiveData.postValue(result.data)
                }
                is Result.Error -> {
                    _marketErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }

    fun dismissLoading() {
        _loadingLiveData.postValue(false)
        _loadingSearchLiveData.postValue(false)
    }


}