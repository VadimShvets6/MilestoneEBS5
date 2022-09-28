package com.top1shvetsvadim1.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.top1shvetsvadim1.coreui.RemoteManager.KEY_BUTTON_COLOR
import com.top1shvetsvadim1.coreui.RemoteManager.KEY_TEXT
import com.top1shvetsvadim1.domain.models.Parameters
import com.top1shvetsvadim1.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingRepositoryImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : SettingsRepository {

    override suspend fun save(key: Parameters, value: String) {
        when (key) {
            Parameters.Text -> {
                context.dataStore.edit { settings ->
                    settings[stringPreferencesKey(KEY_TEXT)] = value
                }
            }
            Parameters.Color -> {
                context.dataStore.edit { settings ->
                    settings[stringPreferencesKey(KEY_BUTTON_COLOR)] = value
                }
            }
        }
    }

    override suspend fun getData(param: Parameters, key: String): Flow<String> {
        return when (param) {
            Parameters.Text -> get(param, key)
            Parameters.Color -> get(param, key)
        }
    }

    inline fun <reified T> get(param: Parameters, key: String): Flow<T> {
        return when (param) {
            Parameters.Text -> getValue(param, key)
            Parameters.Color -> getValue(param, key)
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> getValue(param: Parameters, key: String): Flow<T> {
        return when (param) {
            Parameters.Text -> getString(key)
            Parameters.Color -> getString(key)
        } as Flow<T>
    }

    fun getString(key: String): Flow<String> {
        val preferences = context.dataStore.data
        return preferences.map {
            it.let {
                it[stringPreferencesKey(key)]
            } ?: "BUY NOW"
        }
    }


}