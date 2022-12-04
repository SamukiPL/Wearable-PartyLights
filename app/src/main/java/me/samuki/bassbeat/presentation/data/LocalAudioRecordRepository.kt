package me.samuki.bassbeat.presentation.data

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.job
import me.samuki.bassbeat.presentation.domain.AudioRecordRepository
import me.samuki.bassbeat.presentation.domain.LightState
import java.nio.ByteBuffer
import kotlin.math.abs
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class LocalAudioRecordRepository : AudioRecordRepository {
    private var audioRecorderJob: Job? = null

    private val audioSource get() = MediaRecorder.AudioSource.DEFAULT
    private val sampleRateHz get() = 44100
    private val channelConfig get() = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat get() = AudioFormat.ENCODING_PCM_16BIT

    private val bufferSize
        get() = AudioRecord.getMinBufferSize(
            sampleRateHz,
            channelConfig,
            audioFormat
        ) * 2

    private var maxSum = 0
    private val sumPercentage = 0.9

    override fun observeAudioSpike(): Flow<LightState> = flow {
        audioRecorderJob = currentCoroutineContext().job

        try {
            val audioRecord = AudioRecord(
                audioSource,
                sampleRateHz,
                channelConfig,
                audioFormat,
                bufferSize
            )
            audioRecord.startRecording()
            currentCoroutineContext().job.invokeOnCompletion {
                audioRecord.stop()
                audioRecord.release()
            }

            val buffer = ByteBuffer.allocateDirect(bufferSize)
            while (true) {
                delay(25.milliseconds)
                audioRecord.read(buffer, bufferSize)
                val array = buffer.array()
                val sum = array.sumOf { abs(it.toInt()) }
                if (maxSum < sum) maxSum = sum

                if ((maxSum * sumPercentage) < sum) {
                    emit(LightState.TurnedOn(Random.nextColor()))
                } else {
                    emit(LightState.TurnedOff)
                }
            }
        } catch (ignored: SecurityException) {
        }
    }

    override fun stopAudioRecord() {
        audioRecorderJob?.cancel()
    }

    private fun Random.nextColor() = when (nextInt(0, 9)) {
        1 -> Color(0xFF9F0733)
        2 -> Color(0xFFBF40BF)
        3 -> Color(0XFFF69835)
        4 -> Color(0XFF8EF93D)
        5 -> Color(0XFFFFE925)
        6 -> Color(0XFF6495ED)
        7 -> Color(0xFF70E0D0)
        8 -> Color(0XFFEE82EE)
        else -> Color.White
    }
}
