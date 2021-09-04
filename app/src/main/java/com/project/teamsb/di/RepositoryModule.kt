package com.project.teamsb.di

import com.project.teamsb.data.repository.login.LoginRepository
import com.project.teamsb.data.repository.main.MainRepository
import com.project.teamsb.data.repository.nickname.NicknameRepository
import org.koin.dsl.module

val repositoryModule = module {
    single{ LoginRepository(get()) }
    single{ NicknameRepository(get()) }
    single{ MainRepository(get()) }
}