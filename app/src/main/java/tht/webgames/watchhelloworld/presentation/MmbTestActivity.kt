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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import tht.webgames.watchhelloworld.presentation.network.ForexRs
import tht.webgames.watchhelloworld.presentation.theme.WatchHelloWorldTheme

class MmbTestActivity : ComponentActivity() {
    private val mmbTestPresenter: MmbTestContract.Presenter = MmbTestPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLogger()

        setContent {
            WearApp(mmbTestPresenter, lifecycleScope)
        }
        //window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun initLogger() {
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}

@Composable
fun WearApp(presenter: MmbTestContract.Presenter, lifecycleScope: LifecycleCoroutineScope) {
    var forexList: List<ForexItem> = listOf()
    val navController = rememberNavController()

    WatchHelloWorldTheme {
        Scaffold(
            content = {
                // 頁面管理功能，先去 Greeting
                NavHost(navController = navController, startDestination = "Greeting") {
                    composable("Greeting") { Greeting(/*...*/) }
                    composable("ForexList") { ForexList(forexList) }
                }
            }
        )

        DisposableEffect(presenter) {
            presenter.setViewImpl(
                object : MmbTestContract.View {
                    override fun onForexSuccess(response: ForexRs) {
                        // 把拿到的資料塞進 list
                        forexList = listOf(
                            ForexItem(
                                name = "美金",
                                exchange = ForexUtils.floatToString(response.getUSD())
                            ),
                            ForexItem(
                                name = "人民",
                                exchange = ForexUtils.floatToString(response.getRMB())
                            ),
                            ForexItem(
                                name = "歐元",
                                exchange = ForexUtils.floatToString(response.getEUR())
                            ),
                            ForexItem(
                                name = "日圓",
                                exchange = ForexUtils.floatToString(response.getJPY())
                            ),
                            ForexItem(
                                name = "澳幣",
                                exchange = ForexUtils.floatToString(response.getAUD())
                            ),
                        )

                        // 換頁
                        navController.navigate("ForexList")
                    }
                }
            )
            presenter.getForex(lifecycleScope)
            onDispose {
                // 釋放掉 callback
                presenter.setViewImpl(null)
            }
        }
    }
}

object ForexUtils {
    fun floatToString(float: Float) = String.format("%.4f", float)
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun Greeting() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TimeText()
        Text(
            text = "安安",
            fontSize = 60.sp,
            maxLines = 1,
            softWrap = false,
            color = Color(0xFFBBBBBB),
        )

    }
}

@Composable
fun ForexList(forexListItems: List<ForexItem>) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(forexListItems.size) { index ->
            Card(
                onClick = { /**/ },
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
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