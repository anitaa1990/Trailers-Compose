package com.an.trailers_compose.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun TopBarTitle(@StringRes text: Int) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = text),
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onSecondaryContainer
    )
}