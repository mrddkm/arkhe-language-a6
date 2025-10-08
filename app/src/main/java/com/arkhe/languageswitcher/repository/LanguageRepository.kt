package com.arkhe.languageswitcher.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.arkhe.languageswitcher.model.Languages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "language_settings")

/**
 * Interface for language repository to enable mocking in previews
 */
interface ILanguageRepository {
    val selectedLanguageCode: Flow<String>
    suspend fun setLanguage(languageCode: String)
}

/**
 * Real implementation using DataStore
 */
class LanguageRepository(private val context: Context) : ILanguageRepository {

    private val languageCodeKey = stringPreferencesKey("language_code")

    override val selectedLanguageCode: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[languageCodeKey] ?: Languages.ENGLISH.code
        }

    override suspend fun setLanguage(languageCode: String) {
        context.dataStore.edit { preferences ->
            preferences[languageCodeKey] = languageCode
        }
    }
}

/**
 * Mock implementation for preview - doesn't require DataStore
 */
class MockLanguageRepository : ILanguageRepository {
    private val languageFlow = MutableStateFlow(Languages.ENGLISH.code)

    override val selectedLanguageCode: Flow<String> = languageFlow

    override suspend fun setLanguage(languageCode: String) {
        languageFlow.value = languageCode
    }
}