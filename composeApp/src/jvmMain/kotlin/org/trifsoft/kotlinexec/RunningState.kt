package org.trifsoft.kotlinexec

import androidx.compose.ui.graphics.Color

sealed class RunningState(val text: String, val textColor: Color) {
    data object Running: RunningState("Running...", Color.Black)
    sealed class Ended(code: Int, color: Color): RunningState("Process ended with code $code", color) {
        data object OK: Ended(0, Color(0xFF008000))
        class Error(code: Int): Ended(code, Color.Red)
    }
}