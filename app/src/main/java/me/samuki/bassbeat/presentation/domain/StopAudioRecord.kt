package me.samuki.bassbeat.presentation.domain

class StopAudioRecord(
    private val audioRecordRepository: AudioRecordRepository
) {
    operator fun invoke() = audioRecordRepository.stopAudioRecord()
}
