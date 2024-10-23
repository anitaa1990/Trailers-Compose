package com.an.trailers_compose.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.an.trailers_compose.R
import com.an.trailers_compose.ui.theme.statusColor

@Composable
fun ExpandableCard(
    content: @Composable () -> Unit
) {
    var expandedState by remember { mutableStateOf(false) }
    val textState by animateIntAsState(
        targetValue = if (expandedState) R.string.read_less else R.string.read_more, label = ""
    )

    TextButton(
        onClick = { expandedState = !expandedState },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize(animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            ))
    ) {
        Text(
            modifier = Modifier.weight(1f).wrapContentHeight(),
            textAlign = TextAlign.End,
            text = stringResource(id = textState),
            style = MaterialTheme.typography.bodySmall.copy(
                color = statusColor,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline
            )
        )
    }

    if (expandedState) {
        content()
    }
}

@Composable
@Preview
fun ExpandableCardPreview() {
    ExpandableCard { }
}
