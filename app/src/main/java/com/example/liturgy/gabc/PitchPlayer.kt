package com.example.liturgy.gabc

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

/**
 * Gregorian chant pitch synthesizer providing authentic pitch pipe reference tones (A4 = 440Hz / 432Hz).
 * Used by cantors and choirs to find starting notes for modes I to VIII.
 */
object PitchPlayer {

    private var currentJob: Job? = null
    private var activeTrack: AudioTrack? = null

    // Reference note frequencies (Equal temperament / Solesmes tuning)
    val notes = listOf(
        "Do" to 261.63,
        "Ré" to 293.66,
        "Mi" to 329.63,
        "Fa" to 349.23,
        "Sol" to 392.00,
        "La (440Hz)" to 440.00,
        "Si" to 493.88,
        "Do'" to 523.25
    )

    /**
     * Plays a pure sine pitch tone for a specified duration with soft attack/decay envelopes.
     */
    fun playTone(frequencyHz: Double, durationMs: Long = 1800, onFinished: () -> Unit = {}) {
        stopTone()

        currentJob = CoroutineScope(Dispatchers.Default).launch {
            try {
                val sampleRate = 44100
                val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
                val samples = ShortArray(numSamples)
                val twoPiF = 2.0 * PI * frequencyHz

                // Generate sine wave with soft fade-in (50ms) and fade-out (150ms)
                val attackSamples = (sampleRate * 0.05).toInt()
                val releaseSamples = (sampleRate * 0.15).toInt()
                val releaseStart = numSamples - releaseSamples

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    var amplitude = 0.75

                    if (i < attackSamples) {
                        amplitude *= (i.toDouble() / attackSamples)
                    } else if (i > releaseStart) {
                        amplitude *= ((numSamples - i).toDouble() / releaseSamples)
                    }

                    val sampleValue = (sin(twoPiF * t) * amplitude * Short.MAX_VALUE).toInt()
                    samples[i] = sampleValue.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                val minBufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(maxOf(minBufferSize, samples.size * 2))
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                activeTrack = audioTrack
                audioTrack.write(samples, 0, samples.size)
                audioTrack.play()

                delay(durationMs)
                stopTone()
                onFinished()
            } catch (_: Exception) {
                stopTone()
                onFinished()
            }
        }
    }

    fun stopTone() {
        try {
            currentJob?.cancel()
            currentJob = null
            activeTrack?.let {
                if (it.playState == AudioTrack.PLAYSTATE_PLAYING) {
                    it.stop()
                }
                it.release()
            }
            activeTrack = null
        } catch (_: Exception) {
            activeTrack = null
        }
    }
}
