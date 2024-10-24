package com.an.trailers_compose.ui.list.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.an.trailers_compose.data.remote.model.Category

@Composable
fun ContentCategories(
    selectedCategory: Category,
    categories: List<Category>,
    onCategorySelected: (category: Category) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth()
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        categories.forEach { category ->
            Row(
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 25.dp),
            ) {
                IconButton(
                    onClick = {
                        if (selectedCategory != category) {
                            onCategorySelected(category)
                        }
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = category.iconResId),
                        contentDescription = "",
                        tint = if (selectedCategory == category) Color.Yellow else Color.White
                    )
                }
            }
        }
    }
}
