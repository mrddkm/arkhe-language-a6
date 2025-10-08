package com.arkhe.languageswitcher.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.languageswitcher.di.appModule
import com.arkhe.languageswitcher.ui.components.LanguageBottomSheet
import com.arkhe.languageswitcher.ui.theme.LanguageSwitcherTheme
import com.arkhe.languageswitcher.viewmodel.LanguageViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: LanguageViewModel = koinViewModel()
) {
    val languageState by viewModel.languageState.collectAsState()
    val showBottomSheet by viewModel.showBottomSheet.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = viewModel.getLocalizedString("app_title"))
                }
            )
        }
    ) { paddingValues ->

        AnimatedContent(
            targetState = languageState.isChangingLanguage,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "language_change_animation"
        ) { isLoading ->

            if (isLoading) {
                // Loading Screen
                LoadingScreen()
            } else {
                // Main Content
                MainContent(
                    viewModel = viewModel,
                    paddingValues = paddingValues
                )
            }
        }
    }

    if (showBottomSheet) {
        LanguageBottomSheet(
            selectedLanguage = languageState.currentLanguage,
            onLanguageSelected = viewModel::selectLanguage,
            onDismiss = viewModel::hideLanguageSelector
        )
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Changing language...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MainContent(
    viewModel: LanguageViewModel,
    paddingValues: androidx.compose.foundation.layout.PaddingValues
) {
    val languageState by viewModel.languageState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Translate,
            contentDescription = "Language Icon",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = viewModel.getLocalizedString("welcome_message"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = viewModel.getLocalizedString("description"),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = viewModel.getLocalizedString("current_language"),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = languageState.currentLanguage.nativeName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.showLanguageSelector() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !languageState.isChangingLanguage
        ) {
            Icon(
                imageVector = Icons.Filled.Translate,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = viewModel.getLocalizedString("change_language"))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    val previewContext = LocalContext.current
    KoinApplication(
        application = {
            androidContext(previewContext)
            modules(appModule)
        }
    ) {
        LanguageSwitcherTheme {
            MainScreen()
        }
    }
}