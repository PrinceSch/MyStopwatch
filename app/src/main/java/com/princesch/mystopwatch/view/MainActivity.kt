package com.princesch.mystopwatch.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.princesch.mystopwatch.databinding.ActivityMainBinding
import com.princesch.mystopwatch.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val timestampProvider = object : TimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }

    private val stopwatchListOrchestrator = StopwatchListOrchestrator(
        StopwatchStateHolder(
            StopwatchStateCalculator(
                timestampProvider,
                ElapsedTimeCalculator(timestampProvider)
            ),
            ElapsedTimeCalculator(timestampProvider),
            TimestampMillisecondsFormatter()
        ),
        CoroutineScope(
            Dispatchers.Main
                    + SupervisorJob()
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(
            Dispatchers.Main
                    + SupervisorJob()
        ).launch {
            stopwatchListOrchestrator.ticker.collect {
                binding.textTime.text = it
            }
        }

        with(binding){
            buttonStart.setOnClickListener {
                stopwatchListOrchestrator.start()
            }
            buttonPause.setOnClickListener {
                stopwatchListOrchestrator.pause()
            }
            buttonStop.setOnClickListener {
                stopwatchListOrchestrator.stop()
            }
        }

    }
}