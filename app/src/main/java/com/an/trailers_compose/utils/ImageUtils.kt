package com.an.trailers_compose.utils

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette

object ImageUtils {
    fun parseColorSwatch(color: Palette.Swatch?): Color {
        return if (color != null) {
            Color(parseColor("#${Integer.toHexString(color.rgb)}"))
        } else {
            Color.Black
        }.copy(alpha = 0.6f)
    }
}