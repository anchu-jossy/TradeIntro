package com.techxform.tradintro.feature_main.presentation.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class PortfolisViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    private var _portfolioLiveData = MutableLiveData<BaseResponse<ArrayList<PortfolioItem>>>()
    val portfolioLiveData: LiveData<BaseResponse<ArrayList<PortfolioItem>>> = _portfolioLiveData

    private var _portfolioDashboardLiveData = MutableLiveData<BaseResponse<PortfolioDashboard>>()
    val portfolioDashboardLiveData: LiveData<BaseResponse<PortfolioDashboard>> =
        _portfolioDashboardLiveData

    private var _portfolioErrorLiveData = MutableLiveData<Failure>()
    val portfolioErrorLiveData: LiveData<Failure> = _portfolioErrorLiveData

    private var _portfolioDashboardErrorLiveData = MutableLiveData<Failure>()
    val portfolioDashboardErrorLiveData: LiveData<Failure> = _portfolioDashboardErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private lateinit var job: Job
    private lateinit var previousJob: Job

    fun portfolioList(searchModel: SearchModel) {

        if(::job.isInitialized && job.isActive)
            runBlocking {
                job.cancelAndJoin()
            }
        job = viewModelScope.launch(Dispatchers.Default) {
            delay(1000L)
            //_loadingLiveData.postValue(true)
            when (val result = repository.portfolio(searchModel)) {
                is Result.Success -> {
                    _portfolioLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _portfolioErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }


    }
//TODO: merge both API
    fun portfolioDashboard() {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.portfolioDashboard()) {
                is Result.Success -> {
                    _portfolioDashboardLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _portfolioDashboardErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }


    }


}