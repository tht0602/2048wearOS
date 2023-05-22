package tht.webgames.watchhelloworld.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.*
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
            WearApp(mmbTestPresenter)
        }
        //window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun initLogger() {
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    @OptIn(ExperimentalWearMaterialApi::class)
    @Composable
    fun WearApp(presenter: MmbTestContract.Presenter) {
        WatchHelloWorldTheme {
            var forexList by remember { mutableStateOf(listOf<ForexItem>()) }

            if (forexList.isEmpty()) {
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
            } else {
                ScalingLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(forexList.size) { index ->
                        Card(
                            onClick = { /**/ },
                        ) {
                            Row(Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = forexList[index].name,
                                    fontSize = 25.sp,
                                    maxLines = 1,
                                    color = Color(0xFFBBBBBB),
                                )
                                Text(
                                    text = forexList[index].exchange,
                                    fontSize = 20.sp,
                                    maxLines = 1,
                                    color = Color(0xFFBBBBBB),
                                )
                            }
                        }
                    }
                }
            }

            DisposableEffect(presenter) {
                presenter.setViewImpl(
                    object : MmbTestContract.View {
                        override fun onForexSuccess(response: ForexRs) {
                            forexList = listOf(
                                ForexItem(
                                    name = "美金  ",
                                    exchange = String.format("%.4f", response.getUSD())
                                ),
                                ForexItem(
                                    name = "人民  ",
                                    exchange = String.format("%.4f", response.getRMB())
                                ),
                                ForexItem(
                                    name = "歐元  ",
                                    exchange = String.format("%.4f", response.getEUR())
                                ),
                                ForexItem(
                                    name = "日圓  ",
                                    exchange = String.format("%.4f", response.getJPY())
                                ),
                                ForexItem(
                                    name = "澳幣  ",
                                    exchange = String.format("%.4f", response.getAUD())
                                ),
                                ForexItem(
                                    name = "美金  ",
                                    exchange = String.format("%.4f", response.getUSD())
                                ),
                                ForexItem(
                                    name = "美金  ",
                                    exchange = String.format("%.4f", response.getUSD())
                                ),
                                ForexItem(
                                    name = "美金  ",
                                    exchange = String.format("%.4f", response.getUSD())
                                ),
                            )
                        }
                    }
                )
                presenter.getForex(lifecycleScope)
                onDispose {
                    presenter.setViewImpl(null)
                }
            }
        }
    }

    data class ForexItem(
        val name: String,
        val exchange: String
    )
}