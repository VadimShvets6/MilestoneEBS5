package com.top1shvetsvadim1.domain.repository

import com.top1shvetsvadim1.domain.models.Parameters
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun save(key: Parameters, value: String)
    suspend fun getData(param: Parameters, key: String): Flow<String>
}