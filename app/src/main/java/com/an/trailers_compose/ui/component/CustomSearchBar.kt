package com.an.trailers_compose.ui.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.an.trailers_compose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar() {
    var text by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        // Adding this line to remove top padding between the SearchBar and the TopAppBar.
        windowInsets = WindowInsets(top = 0.dp),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(top = 20.dp, bottom = 20.dp),
        inputField = {
            SearchBarDefaults.InputField(
                query = text,
                onQueryChange = { text = it },
                onSearch = { expanded = false },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text(stringResource(id = R.string.search_hint_movies)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            )
        },
        shape = RoundedCornerShape(5.dp),
        expanded = expanded,
        shadowElevation = 10.dp,
        colors = SearchBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            dividerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        onExpandedChange = { expanded = it },
    ) {  }
}