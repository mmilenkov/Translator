package org.selostudios.translator.android.translation.presentation.componenets

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.selostudios.translator.core.presentation.UiLanguage

@Composable
fun SmallLanguageIcon(
    language: UiLanguage,
    modifier: Modifier = Modifier
) {
   AsyncImage(
       model = language.res,
       contentDescription = language.language.languageName,
       modifier = modifier.size(25.dp)
   )
}