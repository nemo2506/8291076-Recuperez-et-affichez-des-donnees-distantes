package com.jeremieguillot.designsystem.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.compose.BurgundylightScheme
import com.jeremieguillot.designsystem.ui.theme.theme.bluemarine.bluemarinelightScheme

@Composable
fun DesignSystemTheme(
    content: @Composable () -> Unit
) {


    MaterialTheme(
//        colorScheme = DarkBlueColorScheme,
//        colorScheme = deltaLightScheme,
//        colorScheme = bluemarinelightScheme,
        colorScheme = BurgundylightScheme,
//        colorScheme = SugeImageLightScheme,
        typography = Typography,
        content = content
    )
}