package me.samuki.bassbeat.presentation.di

import me.samuki.bassbeat.presentation.MainViewModel
import me.samuki.bassbeat.presentation.data.LocalAudioRecordRepository
import me.samuki.bassbeat.presentation.domain.AudioRecordRepository
import me.samuki.bassbeat.presentation.domain.ObserveAudioSpike
import me.samuki.bassbeat.presentation.domain.StopAudioRecord
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single<AudioRecordRepository> { LocalAudioRecordRepository() }
    factory { ObserveAudioSpike(get()) }
    factory { StopAudioRecord(get()) }
    viewModel { MainViewModel(get(), get()) }
}
