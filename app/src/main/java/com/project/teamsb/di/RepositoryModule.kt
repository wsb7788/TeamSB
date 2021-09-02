package com.project.teamsb.di

import com.project.teamsb.data.repository.login.LoginRepository
import org.koin.dsl.module

val repositoryModule = module {
    single{ LoginRepository(get()) }
}