package tht.webgames.watchhelloworld.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tht.webgames.watchhelloworld.presentation.mvi.BaseViewModel
import tht.webgames.watchhelloworld.presentation.mvi.Reducer
import tht.webgames.watchhelloworld.presentation.mvi.UiEvent
import tht.webgames.watchhelloworld.presentation.mvi.UiState
import tht.webgames.watchhelloworld.presentation.network.ForexRs
import tht.webgames.watchhelloworld.presentation.network.RetrofitManager

class MmbTestViewModel : BaseViewModel<MmbTestUiState, MmbTestUiEvent>() {

    private val reducer = MainReducer(MmbTestUiState.initial())

    override val state: StateFlow<MmbTestUiState>
        get() = reducer.state

    init {
        viewModelScope.launch() {
            getForex()
        }
    }

    fun getForex() {
        sendEvent(MmbTestUiEvent.QueryingForex)
        val call: Call<Collection<ForexRs>> = RetrofitManager.service.queryForex()

        call.enqueue(object : Callback<Collection<ForexRs>> {
            override fun onResponse(
                call: Call<Collection<ForexRs>>,
                response: Response<Collection<ForexRs>>
            ) {
                // 連線成功
                response.body()?.last()?.let {
                    // 把拿到的資料塞進 list
                    val forexList = listOf(
                        ForexItem(
                            name = "美金",
                            exchange = ForexUtils.floatToString(it.getUSD())
                        ),
                        ForexItem(
                            name = "人民",
                            exchange = ForexUtils.floatToString(it.getRMB())
                        ),
                        ForexItem(
                            name = "歐元",
                            exchange = ForexUtils.floatToString(it.getEUR())
                        ),
                        ForexItem(
                            name = "日圓",
                            exchange = ForexUtils.floatToString(it.getJPY())
                        ),
                        ForexItem(
                            name = "澳幣",
                            exchange = ForexUtils.floatToString(it.getAUD())
                        ),
                    )
                    sendEvent(MmbTestUiEvent.QueryingForexSuccess(forexList))
                }
            }

            override fun onFailure(call: Call<Collection<ForexRs>>, t: Throwable) {
                // 連線失敗
            }
        })
    }

    private fun sendEvent(event: MmbTestUiEvent) {
        reducer.sendEvent(event)
    }

    private class MainReducer(initial: MmbTestUiState) :
        Reducer<MmbTestUiState, MmbTestUiEvent>(initial) {
        override fun reduce(oldState: MmbTestUiState, event: MmbTestUiEvent) {
            when (event) {
                is MmbTestUiEvent.QueryingForex -> {
                    setState(oldState.copy(isLoading = true, data = listOf()))
                }

                is MmbTestUiEvent.QueryingForexSuccess -> {
                    setState(
                        oldState.copy(
                            isLoading = false,
                            data = event.list,
                            isShowAddDialog = false
                        )
                    )
                }

                else -> Unit
            }
        }
    }
}

@Immutable
sealed class MmbTestUiEvent : UiEvent {
    object QueryingForex : MmbTestUiEvent()
    data class QueryingForexSuccess(val list: List<ForexItem>) : MmbTestUiEvent()
}

@Immutable
data class MmbTestUiState(
    val isLoading: Boolean,
    val data: List<ForexItem>,
    val isShowAddDialog: Boolean,
) : UiState {

    companion object {
        fun initial() = MmbTestUiState(
            isLoading = true,
            data = emptyList(),
            isShowAddDialog = false
        )
    }

    override fun toString(): String {
        return "isLoading: $isLoading, data.size: ${data.size}, isShowAddDialog: $isShowAddDialog"
    }
}

