package tht.webgames.watchhelloworld.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import tht.webgames.watchhelloworld.presentation.theme.WatchHelloWorldTheme

class MmbTestActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WearApp()
        }
        //window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

@Composable
fun WearApp() {
    val mmbTestViewModel: MmbTestViewModel = koinViewModel()
    WatchHelloWorldTheme {
        val state by mmbTestViewModel.viewModelState.collectAsState()
        if (state.data.isEmpty()) {
            Greeting(
                state,
                onGreetingEnd = { mmbTestViewModel.getForex() }
            )
        } else {
            ForexList(
                state,
                onItemClick = { mmbTestViewModel.clearData() }
            )
        }
    }
}

object ForexUtils {
    fun floatToString(float: Float) = String.format("%.4f", float)
}

@Composable
fun Greeting(uiState: MmbTestUiState, onGreetingEnd: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TimeText()

        LaunchedEffect(true) {
            // 這裡是 coroutine scope
            delay(2000L)
            // 兩秒鐘後開始讀取
            onGreetingEnd.invoke()
        }

        if (uiState.isLoading) {
            Text(
                text = "Loading",
                fontSize = 40.sp,
                maxLines = 1,
                softWrap = false,
                color = Color(0xFFBBBBBB),
            )
        } else {
            Text(
                text = "安安",
                fontSize = 60.sp,
                maxLines = 1,
                softWrap = false,
                color = Color(0xFFBBBBBB),
            )
        }
    }
}

@Composable
fun ForexList(uiState: MmbTestUiState, onItemClick: () -> Unit) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        val forexListItems = uiState.data
        items(forexListItems.size) { index ->
            Card(
                onClick = { onItemClick.invoke() },
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = forexListItems[index].name,
                        fontSize = 25.sp,
                        maxLines = 1,
                        color = Color(0xFFBBBBBB),
                    )
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = forexListItems[index].exchange,
                        fontSize = 20.sp,
                        maxLines = 1,
                        color = Color(0xFFBBBBBB),
                    )
                }
            }
        }
    }
}

data class ForexItem(
    val name: String,
    val exchange: String
)