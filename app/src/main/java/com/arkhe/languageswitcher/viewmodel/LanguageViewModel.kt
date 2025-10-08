package com.arkhe.languageswitcher.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.languageswitcher.R
import com.arkhe.languageswitcher.model.Language
import com.arkhe.languageswitcher.model.LanguageState
import com.arkhe.languageswitcher.model.Languages
import com.arkhe.languageswitcher.repository.LanguageRepository
import com.arkhe.languageswitcher.utils.LanguageManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LanguageViewModel(
    context: Context,
    private val languageRepository: LanguageRepository
) : ViewModel() {

    private val appContext: Context = context.applicationContext

    private val _languageState = MutableStateFlow(LanguageState())
    val languageState: StateFlow<LanguageState> = _languageState.asStateFlow()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    init {
        observeLanguageChanges()
    }

    private fun observeLanguageChanges() {
        viewModelScope.launch {
            languageRepository.selectedLanguageCode.collect { languageCode ->
                val language = Languages.getLanguageByCode(languageCode)
                val localizedStrings = loadLocalizedStrings(languageCode)

                _languageState.value = _languageState.value.copy(
                    currentLanguage = language,
                    localizedStrings = localizedStrings
                )
            }
        }
    }

    private fun loadLocalizedStrings(languageCode: String): Map<String, String> {
        return mapOf(
            "app_name" to LanguageManager.getLocalizedString(
                appContext,
                R.string.app_name,
                languageCode
            ),
            "app_title" to LanguageManager.getLocalizedString(
                appContext,
                R.string.app_title,
                languageCode
            ),
            "select_language" to LanguageManager.getLocalizedString(
                appContext,
                R.string.select_language,
                languageCode
            ),
            "current_language" to LanguageManager.getLocalizedString(
                appContext,
                R.string.current_language,
                languageCode
            ),
            "welcome_message" to LanguageManager.getLocalizedString(
                appContext,
                R.string.welcome_message,
                languageCode
            ),
            "description" to LanguageManager.getLocalizedString(
                appContext,
                R.string.description,
                languageCode
            ),
            "change_language" to LanguageManager.getLocalizedString(
                appContext,
                R.string.change_language,
                languageCode
            )
        )
    }

    fun getLocalizedString(key: String): String {
        return _languageState.value.localizedStrings[key] ?: ""
    }

    fun showLanguageSelector() {
        _showBottomSheet.value = true
    }

    fun hideLanguageSelector() {
        _showBottomSheet.value = false
    }

    fun selectLanguage(language: Language) {
        viewModelScope.launch {
            val currentLanguage = _languageState.value.currentLanguage

            // Only change if language is different
            if (currentLanguage.code != language.code) {
                // Show loading state
                _languageState.value = _languageState.value.copy(isChangingLanguage = true)
                _showBottomSheet.value = false

                // Save language preference
                languageRepository.setLanguage(language.code)

                // Simulate loading time for better UX (optional)
                delay(800) // 800ms loading

                // Load new localized strings
                val newLocalizedStrings = loadLocalizedStrings(language.code)

                // Update state with new language and strings
                _languageState.value = _languageState.value.copy(
                    currentLanguage = language,
                    localizedStrings = newLocalizedStrings,
                    isChangingLanguage = false
                )
            } else {
                _showBottomSheet.value = false
            }
        }
    }
}