package me.samuki.bassbeat.presentation.domain

class ObserveAudioSpike(
    private val audioRecordRepository: AudioRecordRepository
) {
    operator fun invoke() = audioRecordRepository.observeAudioSpike()
}
