package com.techxform.tradintro.feature_main.presentation.portfolio_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.model.FilterModel
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    private var _portfolioLiveData = MutableLiveData<BaseResponse<PortfolioItem>>()
    val portfolioLiveData: LiveData<BaseResponse<PortfolioItem>> = _portfolioLiveData

    private var _portfolioErrorLiveData = MutableLiveData<Failure>()
    val portfolioErrorLiveData: LiveData<Failure> = _portfolioErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private var _buyStockLiveData = MutableLiveData<BaseResponse<PortfolioItem>>()
    val buyStockLiveData: LiveData<BaseResponse<PortfolioItem>> = _buyStockLiveData

    private var _buyStockErrorLiveData = MutableLiveData<Failure>()
    val buyStockErrorLiveData: LiveData<Failure> = _buyStockErrorLiveData

    private var _sellStockLiveData = MutableLiveData<BaseResponse<PortfolioItem>>()
    val sellStockLiveData: LiveData<BaseResponse<PortfolioItem>> = _sellStockLiveData

    private var _sellStockErrorLiveData = MutableLiveData<Failure>()
    val sellStockErrorLiveData: LiveData<Failure> = _sellStockErrorLiveData



    fun portfolioDetails(id:Int,filterModel: FilterModel) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.portfolioDetails(id, filterModel)) {
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