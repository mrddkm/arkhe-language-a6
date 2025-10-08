@file:Suppress("DEPRECATION")

package com.arkhe.languageswitcher.di

import com.arkhe.languageswitcher.repository.ILanguageRepository
import com.arkhe.languageswitcher.repository.LanguageRepository
import com.arkhe.languageswitcher.repository.MockLanguageRepository
import com.arkhe.languageswitcher.viewmodel.LanguageViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { LanguageRepository(get()) }
    viewModel { LanguageViewModel(androidContext(), get()) }
}

val previewModule = module {
    single<ILanguageRepository> { MockLanguageRepository() }
    viewModel { LanguageViewModel(androidContext(), get()) }
}