package com.example.languageswitcher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languageswitcher.model.Language
import com.example.languageswitcher.model.Languages
import com.example.languageswitcher.repository.LanguageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val languageRepository: LanguageRepository
) : ViewModel() {

    private val _selectedLanguage = MutableStateFlow(Languages.ENGLISH)
    val selectedLanguage: StateFlow<Language> = _selectedLanguage.asStateFlow()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    init {
        observeLanguageChanges()
    }

    private fun observeLanguageChanges() {
        viewModelScope.launch {
            languageRepository.selectedLanguageCode.collect { languageCode ->
                _selectedLanguage.value = Languages.getLanguageByCode(languageCode)
            }
        }
    }

    fun showLanguageSelector() {
        _showBottomSheet.value = true
    }

    fun hideLanguageSelector() {
        _showBottomSheet.value = false
    }

    fun selectLanguage(language: Language) {
        viewModelScope.launch {
            languageRepository.setLanguage(language.code)
            _showBottomSheet.value = false
        }
    }
}