package com.github.af2905.coroutineflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.af2905.coroutineflow.extension.join
import com.github.af2905.coroutineflow.extension.toUpperCase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.internal.ChannelFlow
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var formatter = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
    private val scope = CoroutineScope(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //stringToUpperCase()
        //getFlowStrings()
        //collectStrings()
        //getNumbers()
        getNumbersChannelFlow()

    }

    private fun stringToUpperCase() {
        val flowStrings = flow {
            emit("a")
            emit("b")
            emit("c")
        }

        scope.launch { flowStrings.toUpperCase().collect { log(it) } }
    }

    private fun getFlowStrings() {
        val flowStrings = flowOf("a", "b", "c", "d", "e", "f")

        val stringValues = flow {
            emit("start")
            emitAll(flowStrings)
            emit("end")
        }

        scope.launch {
            stringValues.collect {
                log(it)
            }
        }
    }

    private fun collectStrings() {
        val flowStrings = flow {
            emit("abc")
            emit("def")
            emit("ghi")
        }
        scope.launch {
            val result = flowStrings.join()
            log("collectStrings, result: $result")
        }
    }

    @ExperimentalCoroutinesApi
    private fun getNumbers() {
        val flow = flow {

            coroutineScope {
                val channel = produce {
                    launch {
                        delay(1000)
                        send(1)
                    }
                    launch {
                        delay(1000)
                        send(2)
                    }
                    launch {
                        delay(1000)
                        send(3)
                    }
                }

                channel.consumeEach { emit(it) }
            }
        }
        scope.launch { flow.collect { log("$it") } }
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    private fun getNumbersChannelFlow() {
        val flow = channelFlow {
            launch {
                delay(1000)
                send(1)
            }
            launch {
                delay(1000)
                send(2)
            }
            launch {
                delay(1000)
                send(3)
            }
        }
        scope.launch { flow.collect { log("$it") } }
    }

    private fun log(text: String) {
        Log.d("TAG", "${formatter.format(Date())} $text [${Thread.currentThread().name}]")
    }
}