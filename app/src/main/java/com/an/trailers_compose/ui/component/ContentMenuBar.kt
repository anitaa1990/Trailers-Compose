package com.an.trailers_compose.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ContentMenuBar(
    @DrawableRes resId: Int,
    onMenuSelected:() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 55.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.End
    ) {
        // Tv option
        IconButton(
            onClick = { onMenuSelected() }
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = resId),
                contentDescription = "",
                tint = Color.White
            )
        }

        Spacer(Modifier.weight(1f))
        // Search icon
        Icon(
            modifier = Modifier.size(25.dp),
            imageVector = Icons.Default.Search,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}