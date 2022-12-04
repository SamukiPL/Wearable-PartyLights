package me.samuki.bassbeat.presentation.domain

import androidx.compose.ui.graphics.Color

sealed interface LightState {
    object TurnedOff : LightState
    data class TurnedOn(
        val color: Color
    ) : LightState
}
