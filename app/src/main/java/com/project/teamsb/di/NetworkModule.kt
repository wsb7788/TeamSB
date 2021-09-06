package com.project.teamsb.di

import com.project.teamsb.data.remote.home.HomeService
import com.project.teamsb.data.remote.login.LoginService
import com.project.teamsb.data.remote.main.MainService
import com.project.teamsb.data.remote.nickname.NicknameService
import com.project.teamsb.data.remote.notice.NoticeService
import com.project.teamsb.data.remote.user.UserService
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val PRODUCTION_URL = "http://13.209.10.30:3000/"
const val TEST_URL = " http://13.209.10.30:3030/"
private val base_url: String = TEST_URL

fun getBaseUrl() = base_url

val networkModule: Module = module {
    fun provideRetrofit():Retrofit = Retrofit.Builder()
        .baseUrl(getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun provideLoginService(retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)
    fun provideNicknameService(retrofit: Retrofit): NicknameService =
        retrofit.create(NicknameService::class.java)
    fun provideMainService(retrofit: Retrofit): MainService =
        retrofit.create(MainService::class.java)
    fun provideHomeService(retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)
    fun provideNoticeService(retrofit: Retrofit): NoticeService =
        retrofit.create(NoticeService::class.java)

    single { provideRetrofit() }
    single { provideLoginService(get()) }
    single { provideNicknameService(get()) }
    single { provideMainService(get()) }
    single { provideHomeService(get()) }
    single { provideUserService(get()) }
    single { provideNoticeService(get()) }
}

