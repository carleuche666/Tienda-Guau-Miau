package com.vivitasol.carcasamvvm.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.UserSessionPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class SplashState {
    data object Loading : SplashState()
    data class Finished(val isLoggedIn: Boolean) : SplashState()
}

class SplashViewModel(context: Context) : ViewModel() {
    private val _uiState = MutableStateFlow<SplashState>(SplashState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val isLoggedIn = UserSessionPrefs.getIsLoggedInFlow(context).first()
            _uiState.value = SplashState.Finished(isLoggedIn)
        }
    }
}

class SplashViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SplashViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}