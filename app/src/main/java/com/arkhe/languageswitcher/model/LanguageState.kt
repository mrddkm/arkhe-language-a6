package com.arkhe.languageswitcher.model

data class LanguageState(
    val currentLanguage: Language = Languages.ENGLISH,
    val isChangingLanguage: Boolean = false,
    val localizedStrings: Map<String, String> = emptyMap()
)