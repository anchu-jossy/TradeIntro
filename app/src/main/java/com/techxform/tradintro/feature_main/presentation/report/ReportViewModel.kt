package com.techxform.tradintro.feature_main.presentation.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.BaseResponse
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.PortfolioItem
import com.techxform.tradintro.feature_main.data.remote.dto.Result
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel  @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    private var _portfolioLiveData = MutableLiveData<BaseResponse<ArrayList<PortfolioItem>>>()
    val portfolioLiveData: LiveData<BaseResponse<ArrayList<PortfolioItem>>> = _portfolioLiveData

    private var _portfolioErrorLiveData = MutableLiveData<Failure>()
    val portfolioErrorLiveData: LiveData<Failure> = _portfolioErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun historicalReport(searchModel: SearchModel) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.historicalReport(searchModel)) {
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

    fun reportCurrent(searchModel: SearchModel) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.reportCurrent(searchModel)) {
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

}