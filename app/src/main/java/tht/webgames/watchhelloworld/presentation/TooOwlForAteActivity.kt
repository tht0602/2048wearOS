/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package tht.webgames.watchhelloworld.presentation

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import tht.webgames.watchhelloworld.presentation.theme.WatchHelloWorldTheme

class TooOwlForAteActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val game2048 = Game2048()
        game2048.startGame()
        setContent {
            WearApp(game2048)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun WearApp(
    game2048: Game2048
) {
    var offsetX = 0f
    var offsetY = 0f
    val triggerDistance = (45.dp).value
    WatchHelloWorldTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            var direction by remember { //沒人使用的話不會監聽
                mutableStateOf(Direction.NONE)
            }

            Log.d("game2048", "direction = $direction ${game2048.array.contentDeepToString()}")

            fun onDrag(dragAmount: Offset) {
                offsetX += dragAmount.x
                offsetY += dragAmount.y
                if (direction == Direction.NONE) {
                    when {
                        offsetY > triggerDistance -> {
                            direction = Direction.DOWN
                            game2048.moveCardsVertical(true)
                        }
                        offsetY < -triggerDistance -> {
                            direction = Direction.UP
                            game2048.moveCardsVertical()
                        }
                        offsetX > triggerDistance -> {
                            direction = Direction.RIGHT
                            game2048.moveCardsHorizon(true)
                        }
                        offsetX < -triggerDistance -> {
                            direction = Direction.LEFT
                            game2048.moveCardsHorizon()
                        }
                    }
                }
            }

            fun onDragEnd() {
                offsetX = 0f
                offsetY = 0f
                direction = Direction.NONE
            }

            val pointerInputScope: suspend PointerInputScope.() -> Unit = {
                detectDragGestures(
                    onDrag = { _, offset -> onDrag(offset) },
                    onDragEnd = { onDragEnd() }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF333333))
                    .pointerInput(Unit, pointerInputScope),
                contentAlignment = Alignment.Center
            ) {
                TimeText()
                TextViews2048(game2048.array, pointerInputScope)
            }
        }
    }
}

@Composable
fun TextViews2048(
    array: Array<Array<Int>>,
    pointerInputScope: suspend PointerInputScope.() -> Unit
) {
    LazyColumn {
        items(array) { rowArray ->
            LazyRow {
                items(rowArray) { item ->
                    Box(
                        modifier = Modifier
                            .width(25.dp)
                            .height(25.dp)
                            .pointerInput(Unit, pointerInputScope),
                        contentAlignment = Alignment.Center
                    ) {
                        fun textSize(text: String): TextUnit {
                            return when (text.length) {
                                5 -> 7.sp
                                4 -> 9.sp
                                3 -> 12.sp
                                else -> 15.sp
                            }
                        }

//                        val maxTextSize = 50.sp
//                        val minTextSize = 10.sp
//                        var textSize by remember { mutableStateOf(maxTextSize) }
                        Text(
                            text = item.toString(),
                            fontSize = textSize(item.toString()),
//                            fontSize = textSize,
                            maxLines = 1,
                            softWrap = false,
                            color = Color(0xFFBBBBBB),
                            onTextLayout = { textLayoutResult ->
                                Log.d(
                                    "text",
                                    "width = ${textLayoutResult.multiParagraph.width}, it.size = ${textLayoutResult.size}, over = ${textLayoutResult.didOverflowWidth}"
                                )
//                                if (textLayoutResult.hasVisualOverflow && textSize > minTextSize) {
//                                    textSize = (textSize.value - 1.0F).sp
//                                }
                            }
                        )
                    }
                }
            }
        }
    }
}