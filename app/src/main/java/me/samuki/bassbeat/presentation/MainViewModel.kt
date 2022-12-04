package me.samuki.bassbeat.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.samuki.bassbeat.presentation.domain.LightState
import me.samuki.bassbeat.presentation.domain.ObserveAudioSpike
import me.samuki.bassbeat.presentation.domain.StopAudioRecord

class MainViewModel(
    private val observeAudioSpike: ObserveAudioSpike,
    private val stopAudioRecord: StopAudioRecord
) : ViewModel() {

    var showLight by mutableStateOf<LightState>(LightState.TurnedOff)
        private set

    fun startObserving() {
        viewModelScope.launch {
            observeAudioSpike()
                .collect {
                    showLight = it
                }
        }
    }

    fun stopObserving() {
        stopAudioRecord()
    }
}
