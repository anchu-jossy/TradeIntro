package com.techxform.tradintro.feature_main.presentation.report

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
import javax.inject.Inject

@HiltViewModel
class OrderBookViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {
    private var _portfolioLiveData = MutableLiveData<BaseResponse<ArrayList<PortfolioItem>>>()
    val portfolioLiveData: LiveData<BaseResponse<ArrayList<PortfolioItem>>> = _portfolioLiveData
    private var _updatePortfolioLiveData = MutableLiveData<BaseResponse<PortfolioItem>>()
    val updatePortfolioLiveData: LiveData<BaseResponse<PortfolioItem>> = _updatePortfolioLiveData
    private var _deletePortfolioLiveData = MutableLiveData<BaseResponse<Any>>()
    val deletePortfolioLiveData: LiveData<BaseResponse<Any>> = _deletePortfolioLiveData

    private var _portfolioErrorLiveData = MutableLiveData<Failure>()
    val portfolioErrorLiveData: LiveData<Failure> = _portfolioErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun portfolioList(searchModel: SearchModel) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.portfolio(searchModel)) {
                is Result.Success -> {
                    result.data.data.sortByDescending { it.orderExecutedOn }
                    _portfolioLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _portfolioErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }
    fun updatePortfolio(id: Int, req: UpdatePortfolioRequest) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.updatePortfolio(id,req)) {
                is Result.Success -> {
                    _updatePortfolioLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _portfolioErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }
    fun deletePortfolio(id: Int) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.deletePortfolio(id)) {
                is Result.Success -> {
                    _deletePortfolioLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _portfolioErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }
}