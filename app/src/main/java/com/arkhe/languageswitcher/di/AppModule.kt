package com.arkhe.languageswitcher.di

import com.arkhe.languageswitcher.repository.LanguageRepository
import com.arkhe.languageswitcher.viewmodel.LanguageViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { LanguageRepository(get()) }
    viewModel { LanguageViewModel(get(), get()) }
}