package org.trifsoft.kotlinexec

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {
    val textPadding = PaddingValues(10.dp)
    val sideSpacing = with(LocalDensity.current) { 10.dp.toPx() }
    val horizontalScrollState = rememberScrollState()
    var text by remember {
        mutableStateOf(
            TextFieldValue(
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata\nLorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata\nLorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata\nLorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata\nLorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata"
            )
        )
    }
    var cursorPos: (Int)-> Rect by remember { mutableStateOf({ Rect(0f,0f,0f,0f) }) }
    var width by remember { mutableIntStateOf(-1) }
    LaunchedEffect(text.selection) {
        val cursorPosition = cursorPos(text.selection.end)
        val leftRelativePosition = cursorPosition.left - horizontalScrollState.value
        val rightRelativePosition = cursorPosition.right - horizontalScrollState.value
        if(width != -1 && (rightRelativePosition > width - sideSpacing || leftRelativePosition < sideSpacing)) {
            if(leftRelativePosition < sideSpacing) {
                horizontalScrollState.scrollBy(leftRelativePosition - sideSpacing)
            }
            else {
                horizontalScrollState.scrollBy(rightRelativePosition + sideSpacing - width)
            }
        }
    }
    MaterialTheme {
        Row(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(textPadding)
                    .onSizeChanged { width = it.width }
                    .horizontalScroll(horizontalScrollState)
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    onTextLayout = { textLayout ->
                        cursorPos = { textLayout.getCursorRect(it) }
                    },
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                )
            }
            Box(
                Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color.Magenta)
            )
            Text(
                text = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolo",
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(textPadding)
            )
        }
    }
}