package com.project.teamsb.di

import com.project.teamsb.utils.SharedPreferencesManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val utilityModule = module {
    single { SharedPreferencesManager(androidContext()) }
}