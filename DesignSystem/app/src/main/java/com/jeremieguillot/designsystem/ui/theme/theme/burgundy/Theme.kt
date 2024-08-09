package com.example.compose
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val BurgundylightScheme = lightColorScheme(
    primary = BuprimaryLight,
    onPrimary = BuonPrimaryLight,
    primaryContainer = BuprimaryContainerLight,
    onPrimaryContainer = BuonPrimaryContainerLight,
    secondary = BusecondaryLight,
    onSecondary = BuonSecondaryLight,
    secondaryContainer = BusecondaryContainerLight,
    onSecondaryContainer = BuonSecondaryContainerLight,
    tertiary = ButertiaryLight,
    onTertiary = BuonTertiaryLight,
    tertiaryContainer = ButertiaryContainerLight,
    onTertiaryContainer = BuonTertiaryContainerLight,
    error = BuerrorLight,
    onError = BuonErrorLight,
    errorContainer = BuerrorContainerLight,
    onErrorContainer = BuonErrorContainerLight,
    background = BubackgroundLight,
    onBackground = BuonBackgroundLight,
    surface = BusurfaceLight,
    onSurface = BuonSurfaceLight,
    surfaceVariant = BusurfaceVariantLight,
    onSurfaceVariant = BuonSurfaceVariantLight,
    outline = BuoutlineLight,
    outlineVariant = BuoutlineVariantLight,
    scrim = BuscrimLight,
    inverseSurface = BuinverseSurfaceLight,
    inverseOnSurface = BuinverseOnSurfaceLight,
    inversePrimary = BuinversePrimaryLight,
)