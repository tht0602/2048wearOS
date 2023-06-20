package tht.webgames.watchhelloworld.presentation.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel<T : UiState> : ViewModel() {

    abstract val viewModelState: MutableStateFlow<T>

}

interface UiState