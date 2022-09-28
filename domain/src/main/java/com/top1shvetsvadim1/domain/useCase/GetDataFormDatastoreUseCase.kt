package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.UseCase
import com.top1shvetsvadim1.domain.models.Parameters
import com.top1shvetsvadim1.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDataFormDatastoreUseCase @Inject constructor(
    private val repository: SettingsRepository
) : UseCase<ParametersRemote, Flow<String>> {
    override suspend fun invoke(params: ParametersRemote): Flow<String> {
        return repository.getData(params.param, params.key)
    }
}

data class ParametersRemote(
    val param: Parameters,
    val key: String
)