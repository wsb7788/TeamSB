package com.project.teamsb.data.repository.notice

import com.project.teamsb.data.remote.notice.NoticeResponse
import com.project.teamsb.data.remote.notice.NoticeService
import com.project.teamsb.data.repository.BaseRepository

class NoticeRepository(private val noticeService: NoticeService): BaseRepository() {
    suspend fun noticeList(page: Int): NoticeResponse{
        return apiRequest { noticeService.noticeList(page) }
    }
    suspend fun noticeTopList(): NoticeResponse{
        return apiRequest { noticeService.noticeTopList() }
    }
}