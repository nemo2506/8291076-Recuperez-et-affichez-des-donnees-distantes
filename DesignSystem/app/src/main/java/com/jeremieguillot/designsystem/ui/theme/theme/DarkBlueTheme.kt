package com.jeremieguillot.designsystem.ui.theme.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val DarkBlue = Color(0xFF003865)
private val SlateGray = Color(0xFF5D737E)
private val SteelBlue = Color(0xFF4F6D7A)
private val LightGray = Color(0xFFF5F5F5)
private val WhiteSurface = Color(0xFFFFFFFF)
private val DarkText = Color(0xFF000000)
private val Error = Color(0xFFB00020)
private val OnError = Color(0xFFFFFFFF)
private val ErrorContainer = Color(0xFFF2B8B5)
private val OnErrorContainer = Color(0xFF601410)
private val Outline = Color(0xFFB0BEC5)
private val OutlineVariant = Color(0xFF90A4AE)
private val Scrim = Color(0xFF000000)
private val InverseSurface = Color(0xFF303030)
private val InverseOnSurface = Color(0xFFEDEDED)
private val InversePrimary = Color(0xFFD0BCFF)

val DarkBlueColorScheme = lightColorScheme(
    primary = DarkBlue,
    onPrimary = WhiteSurface,
    primaryContainer = DarkBlue.copy(alpha = 0.8f),
    onPrimaryContainer = WhiteSurface,
    inversePrimary = InversePrimary,
    secondary = SlateGray,
    onSecondary = WhiteSurface,
    secondaryContainer = SlateGray.copy(alpha = 0.8f),
    onSecondaryContainer = WhiteSurface,
    tertiary = SteelBlue,
    onTertiary = WhiteSurface,
    tertiaryContainer = SteelBlue.copy(alpha = 0.8f),
    onTertiaryContainer = WhiteSurface,
    background = LightGray,
    onBackground = DarkText,
    surface = WhiteSurface,
    onSurface = DarkText,
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkText,
    surfaceTint = DarkBlue,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    outline = Outline,
    outlineVariant = OutlineVariant,
    scrim = Scrim,
)