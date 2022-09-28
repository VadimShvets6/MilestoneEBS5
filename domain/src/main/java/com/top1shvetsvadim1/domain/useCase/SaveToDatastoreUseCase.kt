package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.domain.models.Parameters
import com.top1shvetsvadim1.domain.repository.SettingsRepository
import javax.inject.Inject

class SaveToDatastoreUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(param: Parameters, value: String) = repository.save(param, value)
}