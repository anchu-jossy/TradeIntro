package com.techxform.tradintro.feature_main.presentation.myskills

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
class MySkillsViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    private var _userLevelsLiveData = MutableLiveData<BaseResponse<UserLevels>>()
    val userLevelsLiveData: LiveData<BaseResponse<UserLevels>> = _userLevelsLiveData

    private var _userLevelsErrorLiveData = MutableLiveData<Failure>()
    val userLevelsErrorLiveData: LiveData<Failure> = _userLevelsErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData
    private var _userDetailErrorLiveData = MutableLiveData<Failure>()
    val portfolioErrorLiveData: LiveData<Failure> = _userDetailErrorLiveData


    private var _userLevelsHistoryLiveData = MutableLiveData<BaseResponse<ArrayList<Level>>>()
    val userLevelsHistoryLiveData: LiveData<BaseResponse<ArrayList<Level>>> =
        _userLevelsHistoryLiveData

    private var myLevel: Int = 0
    private var myCurrentLevel: Levels? = null;
    fun userLevels() {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.userLevels()) {
                is Result.Success -> {
                    result.data.data.myLevel?.let {
                        myLevel = it
                    }

                    for (level in result.data.data.levels) {
                        if (level.levelPosition == myLevel) {
                            level.userLevel = myLevel
                            myCurrentLevel = level
                            result.data.data.levels.remove(level)
                            break
                        }
                    }

                    _userLevelsLiveData.postValue(result.data)
                }
                is Result.Error -> {
                    _userLevelsErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }

    fun userLevelsHistory(searchModel: SearchModel) {
        _loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            when (val result = repository.userPointsHistory(searchModel)) {
                is Result.Success -> {
                    _userLevelsHistoryLiveData.postValue(result.data!!)
                }
                is Result.Error -> {
                    _userLevelsErrorLiveData.postValue(result.exception)
                }
            }
            _loadingLiveData.postValue(false)
        }

    }

    fun getCurrentLevel(): Levels? {
        return myCurrentLevel;
    }


}
