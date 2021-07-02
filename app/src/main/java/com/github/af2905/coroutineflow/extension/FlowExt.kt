package com.github.af2905.coroutineflow.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.util.*


fun Flow<String>.toUpperCase(): Flow<String> =
    flow {
        collect {
            emit(it.toUpperCase(Locale.ROOT))
        }
    }

suspend fun Flow<String>.join(): String {
    val sb = StringBuilder()

    collect { sb.append(it).append(",") }

    return sb.toString()
}