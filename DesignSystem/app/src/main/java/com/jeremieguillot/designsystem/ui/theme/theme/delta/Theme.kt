package com.jeremieguillot.designsystem.ui.theme.theme.delta
import androidx.compose.material3.lightColorScheme
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DbackgroundLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DerrorContainerLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DerrorLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DinverseOnSurfaceLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DinversePrimaryLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DinverseSurfaceLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DonBackgroundLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DonErrorContainerLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DonErrorLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DonPrimaryContainerLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DonPrimaryLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DonSecondaryContainerLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DonSecondaryLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DonSurfaceLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DonSurfaceVariantLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DonTertiaryContainerLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DonTertiaryLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DoutlineLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DoutlineVariantLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DprimaryContainerLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DprimaryLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DscrimLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DsecondaryContainerLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DsecondaryLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DsurfaceLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DsurfaceVariantLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DtertiaryContainerLight
import com.jeremieguillot.designsystem.ui.theme.theme.delta.DtertiaryLight

val deltaLightScheme = lightColorScheme(
    primary = DprimaryLight,
    onPrimary = DonPrimaryLight,
    primaryContainer = DprimaryContainerLight,
    onPrimaryContainer = DonPrimaryContainerLight,
    secondary = DsecondaryLight,
    onSecondary = DonSecondaryLight,
    secondaryContainer = DsecondaryContainerLight,
    onSecondaryContainer = DonSecondaryContainerLight,
    tertiary = DtertiaryLight,
    onTertiary = DonTertiaryLight,
    tertiaryContainer = DtertiaryContainerLight,
    onTertiaryContainer = DonTertiaryContainerLight,
    error = DerrorLight,
    onError = DonErrorLight,
    errorContainer = DerrorContainerLight,
    onErrorContainer = DonErrorContainerLight,
    background = DbackgroundLight,
    onBackground = DonBackgroundLight,
    surface = DsurfaceLight,
    onSurface = DonSurfaceLight,
    surfaceVariant = DsurfaceVariantLight,
    onSurfaceVariant = DonSurfaceVariantLight,
    outline = DoutlineLight,
    outlineVariant = DoutlineVariantLight,
    scrim = DscrimLight,
    inverseSurface = DinverseSurfaceLight,
    inverseOnSurface = DinverseOnSurfaceLight,
    inversePrimary = DinversePrimaryLight,
)