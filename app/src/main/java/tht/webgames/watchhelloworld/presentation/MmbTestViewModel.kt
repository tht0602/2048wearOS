package tht.webgames.watchhelloworld.presentation

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tht.webgames.watchhelloworld.presentation.mvi.BaseViewModel
import tht.webgames.watchhelloworld.presentation.mvi.UiState
import tht.webgames.watchhelloworld.presentation.network.ForexRs
import tht.webgames.watchhelloworld.presentation.network.RetrofitManager

class MmbTestViewModel : BaseViewModel<MmbTestUiState>() {

    override val viewModelState = MutableStateFlow(
        MmbTestUiState(
            isLoading = false,
            data = emptyList()
        )
    )

    fun getForex() {
        viewModelState.update { oldState ->
            oldState.copy(isLoading = true)
        }
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
                    viewModelState.update { oldState ->
                        oldState.copy(isLoading = false, data = forexList)
                    }
                }
            }

            override fun onFailure(call: Call<Collection<ForexRs>>, t: Throwable) {
                // 連線失敗
            }
        })
    }

    fun clearData() {
        viewModelState.update { oldState ->
            oldState.copy(data = listOf())
        }
    }
}

@Immutable
data class MmbTestUiState(
    val isLoading: Boolean,
    val data: List<ForexItem>,
) : UiState {
    override fun toString(): String {
        return "isLoading: $isLoading, data.size: ${data.size}"
    }
}

