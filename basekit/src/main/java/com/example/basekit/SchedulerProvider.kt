package com.example.basekit

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun ioSchedulerProvider(): Scheduler
    fun uiSchedulerProvider(): Scheduler
}