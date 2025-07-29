package com.example.languageswitcher.di

import com.example.languageswitcher.repository.LanguageRepository
import com.example.languageswitcher.viewmodel.LanguageViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { LanguageRepository(get()) }
    viewModel { LanguageViewModel(get(), get()) }
}