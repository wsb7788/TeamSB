package com.project.teamsb

import android.app.Application

import com.project.teamsb.di.networkModule
import com.project.teamsb.di.repositoryModule
import com.project.teamsb.di.utilityModule
import com.project.teamsb.di.viewModelModule
import com.project.teamsb.post.App

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ApplicationClass : Application(){
    companion object {
        lateinit var instance: ApplicationClass
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin{
            /*if (DEBUG) {
                androidLogger()
            } else {*/
            androidLogger()
//        }
            androidContext(this@ApplicationClass)
            modules(
                utilityModule,
                viewModelModule,
                networkModule,
                repositoryModule
            )
        }
    }
}