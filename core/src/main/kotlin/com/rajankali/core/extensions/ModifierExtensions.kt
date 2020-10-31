package com.rajankali.core.extensions

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier

fun Modifier.matchParent() = this.fillMaxHeight().fillMaxWidth()