package com.project.teamsb.di

import com.project.teamsb.ui.home.HomeViewModel
import com.project.teamsb.ui.login.LoginViewModel
import com.project.teamsb.ui.main.MainViewModel
import com.project.teamsb.ui.nickname.NicknameViewModel
import com.project.teamsb.ui.splash.SplashViewModel
import com.project.teamsb.ui.user.UserViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(),get()) }
    viewModel { NicknameViewModel(get(),get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { MainViewModel(get(),get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { UserViewModel(get(),get()) }
}