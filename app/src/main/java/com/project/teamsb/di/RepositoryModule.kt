package com.project.teamsb.di

import com.project.teamsb.data.repository.home.HomeRepository
import com.project.teamsb.data.repository.login.LoginRepository
import com.project.teamsb.data.repository.main.MainRepository
import com.project.teamsb.data.repository.nickname.NicknameRepository
import com.project.teamsb.data.repository.notice.NoticeRepository
import com.project.teamsb.data.repository.user.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single{ LoginRepository(get()) }
    single{ NicknameRepository(get()) }
    single{ MainRepository(get()) }
    single{ HomeRepository(get()) }
    single{ UserRepository(get()) }
    single{ NoticeRepository(get()) }
}