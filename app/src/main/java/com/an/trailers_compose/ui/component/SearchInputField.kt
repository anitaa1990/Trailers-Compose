package com.an.trailers_compose.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.an.trailers_compose.R
import com.an.trailers_compose.ui.theme.color_blue
import com.an.trailers_compose.ui.theme.color_cyan
import com.an.trailers_compose.ui.theme.color_silver
import com.an.trailers_compose.ui.theme.color_soft_white

@Composable
fun SearchInputField(
    inputText: String,
    onSearchInputChanged: (String) -> Unit,
    onClearInputClicked: () -> Unit,
    onChevronClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = inputText,
        onValueChange = { newInput: String -> onSearchInputChanged(newInput) },
        modifier = Modifier
            .padding(vertical = 55.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .focusable(),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }),
        leadingIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                tint = color_silver.copy(alpha = 0.6f),
                modifier = Modifier.clickable {
                    keyboardController?.hide()
                    onChevronClicked.invoke()
                },
                contentDescription = ""
            )
        },
        colors = searchFieldColorsStateActive(),
        trailingIcon = if (inputText.isNotEmpty()) {
            {
                Icon(
                    imageVector = Icons.Default.Clear,
                    tint = color_silver,
                    modifier = Modifier.clickable { onClearInputClicked() },
                    contentDescription = "",
                )
            }
        } else {
            null
        },
        placeholder = {
            MyText(
                text = stringResource(id = R.string.search_placeholder),
                style = TextStyle(
                    fontWeight = FontWeight(500),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = TextUnit(-0.01f, TextUnitType.Sp),
                    color = color_silver.copy(alpha = 0.6f),
                )
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        shape = RectangleShape
    )
}
@Composable
private fun searchFieldColorsStateActive() = TextFieldDefaults.colors(
    focusedContainerColor = color_blue,
    unfocusedContainerColor = color_blue,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    unfocusedTextColor = color_soft_white,
    focusedTextColor = color_soft_white,
    disabledTextColor = color_soft_white,
    cursorColor = color_cyan,
    focusedLabelColor = Color.Transparent,
    unfocusedLabelColor = Color.Transparent
)

@Composable
fun MyText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines,
        overflow = overflow,
        style = style,
        textAlign = textAlign,
    )
}

