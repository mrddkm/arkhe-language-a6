@file:Suppress("DEPRECATION")

package com.arkhe.languageswitcher.di

import com.arkhe.languageswitcher.repository.ILanguageRepository
import com.arkhe.languageswitcher.repository.LanguageRepository
import com.arkhe.languageswitcher.repository.MockLanguageRepository
import com.arkhe.languageswitcher.viewmodel.LanguageViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Production module - uses real DataStore implementation
 */
val appModule = module {
    single<ILanguageRepository> { LanguageRepository(get()) }
    viewModel { LanguageViewModel(androidContext(), get()) }
}

/**
 * Preview module - uses mock implementation without DataStore
 */
val previewModule = module {
    single<ILanguageRepository> { MockLanguageRepository() }
    viewModel { LanguageViewModel(androidContext(), get()) }
}