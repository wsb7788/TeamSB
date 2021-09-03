package com.project.teamsb.di

import com.project.teamsb.ui.login.LoginViewModel
import com.project.teamsb.ui.nickname.NicknameViewModel
import com.project.teamsb.ui.splash.SplashViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(),get()) }
    viewModel { NicknameViewModel(get(),get()) }
    viewModel { SplashViewModel(get()) }
}