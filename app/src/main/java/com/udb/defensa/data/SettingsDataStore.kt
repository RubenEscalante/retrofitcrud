package com.udb.defensa.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.udb.defensa.presentation.viewmodels.AuthViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataStore @Inject constructor(context: Context) {
    val Context.dataStore by preferencesDataStore(name = "CHANNEL_TOKEN")
    var pref = context.dataStore

    companion object {
        var token = stringPreferencesKey("TOKEN")
    }

    private val _token = pref.data.map {
        it[token]
    }
    val tokeny: Flow<String?> = _token

    suspend fun setToken(_token: String) {
        pref.edit {
            it[token] = _token
        }
    }

    suspend fun clearToken() {
        pref.edit {
            it.remove(token)
        }
    }

    fun getToken() = pref.data.map { it[token] ?: "" }
}