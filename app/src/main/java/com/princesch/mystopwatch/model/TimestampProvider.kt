package com.princesch.mystopwatch.model

interface TimestampProvider {
    fun getMilliseconds(): Long
}
