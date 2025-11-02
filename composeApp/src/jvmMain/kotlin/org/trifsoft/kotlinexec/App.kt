package org.trifsoft.kotlinexec

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.io.FileWriter

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {
    val spacing = 10.dp
    val textPadding = PaddingValues(spacing)
    val sideSpacing = with(LocalDensity.current) { spacing.toPx() }
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()
    var output by remember { mutableStateOf<Output>(Output.Empty) }
    var text by remember {
        mutableStateOf(
            TextFieldValue()
        )
    }
    var cursorPos: (Int)-> Rect by remember { mutableStateOf({ Rect(0f,0f,0f,0f) }) }
    var textSize by remember { mutableStateOf<IntSize?>(null) }
    LaunchedEffect(text.selection) {
        val cursorPosition = cursorPos(text.selection.end)
        val leftRelativePosition = cursorPosition.left - horizontalScrollState.value
        val rightRelativePosition = cursorPosition.right - horizontalScrollState.value
        val topRelativePosition = cursorPosition.top - verticalScrollState.value
        val bottomRelativePosition = cursorPosition.bottom - verticalScrollState.value
        textSize?.let { (width, height) ->
            if(leftRelativePosition < sideSpacing) {
                horizontalScrollState.scrollBy(leftRelativePosition - sideSpacing)
            }
            else if(rightRelativePosition > width - sideSpacing){
                horizontalScrollState.scrollBy(rightRelativePosition + sideSpacing - width)
            }
            if(topRelativePosition < 0) {
                verticalScrollState.scrollBy(topRelativePosition)
            }
            else if(bottomRelativePosition > height) {
                verticalScrollState.scrollBy(bottomRelativePosition - height)
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
                    .onSizeChanged {
                        textSize = it
                    }
                    .horizontalScroll(horizontalScrollState)
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    textStyle = TextStyle(
                        lineHeight = 30.sp,
                        fontSize = 24.sp
                    ),
                    onTextLayout = { textLayout ->
                        cursorPos = { textLayout.getCursorRect(it) }
                    },
                    modifier = Modifier
                        .verticalScroll(verticalScrollState)
                )
            }
            Box(
                Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color.Magenta)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing),
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1f)
                    .fillMaxHeight()
                    .padding(textPadding)
            ) {
                Text(
                    text = output.text,
                    color = output.color,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                Button (
                    shape = RoundedCornerShape(spacing),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF009900),
                        contentColor = Color.White
                    ),
                    onClick = {
                        val writeStream = FileWriter("output.kts")
                        writeStream.write(text.text)
                        writeStream.close()
                        output = Output.Running
                        val process = ProcessBuilder("kotlinc", "-script", "output.kts")
                            .start()
                        val future = process.onExit()
                        future.thenAccept { pc ->
                            if(pc.exitValue() == 0) {
                                output = Output.Result.OK(
                                    pc.inputStream
                                        .bufferedReader()
                                        .readLines()
                                        .reduceOrNull { s1,s2 -> s1+"\n"+s2 } ?: ""
                                )
                            }
                            else {
                                output = Output.Result.Error(
                                    pc.errorStream
                                        .bufferedReader()
                                        .readLines()
                                        .reduceOrNull { s1,s2 -> s1+"\n"+s2 } ?: "",
                                    pc.exitValue()
                                )
                            }
                        }
                    }
                ) {
                    Text("Run")
                }
            }
        }
    }
}