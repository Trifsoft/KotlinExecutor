package org.trifsoft.kotlinexec

import androidx.compose.ui.graphics.Color

sealed class Output(
    val text: String,
    val color: Color
) {
    data object Running: Output("Running...", Color.Green)
    data object Empty: Output("", Color.Unspecified)
    sealed class Result(text: String, color: Color, val code: Int):
        Output("$text\n\nProcess finished with exit code $code", color) {
            class OK(text: String): Result(text, Color.Black, 0)
            class Error(text: String, code: Int): Result(text, Color.Red, code)
        }
}