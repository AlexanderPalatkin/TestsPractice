package com.example.testspractice.stubs

import com.example.testspractice.presenter.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class ScheduleProviderStub : SchedulerProvider {
    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }
    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }
}
