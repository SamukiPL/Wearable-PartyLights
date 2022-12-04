package me.samuki.bassbeat.presentation.domain

import kotlinx.coroutines.flow.Flow

interface AudioRecordRepository {
    fun observeAudioSpike(): Flow<LightState>
    fun stopAudioRecord()
}
