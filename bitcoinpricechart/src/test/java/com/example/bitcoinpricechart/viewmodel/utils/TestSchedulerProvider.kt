package com.example.bitcoinpricechart.viewmodel.utils

import com.example.basekit.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * SchedulerProvider for unit testing
 */
class TestSchedulerProvider : SchedulerProvider {
    override fun ioSchedulerProvider(): Scheduler = Schedulers.trampoline()
    override fun uiSchedulerProvider(): Scheduler = Schedulers.trampoline()
}