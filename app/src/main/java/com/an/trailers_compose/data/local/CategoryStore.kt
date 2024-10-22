package com.an.trailers_compose.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.an.trailers_compose.data.remote.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryStore(
    private val context: Context
) {
    // Create the dataStore and give it a name same as shared preferences
    private val Context.dataStore by preferencesDataStore("category_preferences")

    // Create some keys we will use them to store and retrieve the data
    companion object {
        val MOVIE_CATEGORY_KEY = stringPreferencesKey("movie_category")
    }

    // Store category data
    // refer to the data store and using edit
    // we can store values using the keys
    suspend fun storeMovieCategory(category: Category) {
        context.dataStore.edit {
            it[MOVIE_CATEGORY_KEY] = category.name
        }
    }

    // Create a flow to retrieve category from the preferences
    val movieCategory: Flow<Category?> = context.dataStore.data.map { prefs ->
        prefs[MOVIE_CATEGORY_KEY]?.let { Category.valueOf(it) }
    }
}