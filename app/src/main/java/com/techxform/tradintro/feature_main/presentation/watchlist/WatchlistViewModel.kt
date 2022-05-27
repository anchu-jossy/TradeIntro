package com.techxform.tradintro.feature_main.presentation.watchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.model.FilterModel
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel(){

    private var _watchlistLiveData = MutableLiveData<BaseResponse<ArrayList<WatchList>>>()
    val watchlistLiveData: LiveData<BaseResponse<ArrayList<WatchList>>> = _watchlistLiveData

    private var _watchlistErrorLiveData = MutableLiveData<Failure>()
    val watchlistErrorLiveData: LiveData<Failure> = _watchlistErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private var _deleteWatchlistLiveData = MutableLiveData<BaseResponse<DeleteWatchListResponse>>()
    val deleteWatchlistLiveData: LiveData<BaseResponse<DeleteWatchListResponse>> = _deleteWatchlistLiveData

    fun watchlist(filterModel: FilterModel)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.watchlist(filterModel)) {
                is Result.Success -> {
                    _watchlistLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _watchlistErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }

    fun removeWatchlist(id: Number)
    {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.deleteWatchList(id)) {
                is Result.Success -> {

                    _deleteWatchlistLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _watchlistErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }
    }
}