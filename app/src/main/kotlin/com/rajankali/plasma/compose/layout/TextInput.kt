/*
 * MIT License
 *
 * Copyright (c) 2020 rajandev17
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.rajankali.plasma.compose.layout

/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.foundation.BaseTextField
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.isFocused
import androidx.compose.ui.focusObserver
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@ExperimentalFocus
@ExperimentalFoundationApi
@Composable
fun SearchInput(hint: String, textState: MutableState<TextFieldValue>, onValueChange: (String) -> Unit) {
    // Used to decide if the keyboard should be shown
    var textFieldFocusState by remember { mutableStateOf(false) }
    UserInputText(
        textFieldValue = textState.value,
        onTextChanged = {
            textState.value = it
            onValueChange.invoke(it.text)
        },
        // Only show the keyboard if there's no input selector and text field has focus
        keyboardShown = textFieldFocusState,
        // Close extended selector if text field receives focus
        onTextFieldFocused = { focused ->
            textFieldFocusState = focused
        },
        hint = hint,
        focusState = textFieldFocusState
    )
}

val KeyboardShownKey = SemanticsPropertyKey<Boolean>("KeyboardShownKey")
var SemanticsPropertyReceiver.keyboardShownProperty by KeyboardShownKey

@ExperimentalFocus
@ExperimentalFoundationApi
@Composable
private fun UserInputText(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    keyboardShown: Boolean,
    hint: String = "",
    onTextFieldFocused: (Boolean) -> Unit,
    focusState: Boolean
) {
    // Grab a reference to the keyboard controller whenever text input starts
    var keyboardController by remember { mutableStateOf<SoftwareKeyboardController?>(null) }

    // Show or hide the keyboard
    onCommit(keyboardController, keyboardShown) { // Guard side-effects against failed commits
        keyboardController?.let {
            if (keyboardShown) it.showSoftwareKeyboard() else it.hideSoftwareKeyboard()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .preferredHeight(48.dp)
            .semantics {
                keyboardShownProperty = keyboardShown
            },
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier.preferredHeight(45.dp).weight(1f).align(Alignment.Bottom)
        ) {
            var lastFocusState by remember { mutableStateOf(FocusState.Inactive) }
            BaseTextField(
                value = textFieldValue,
                onValueChange = { onTextChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
                    .align(Alignment.CenterStart)
                    .focusObserver { state ->
                        if (lastFocusState != state) {
                            onTextFieldFocused(state.isFocused)
                        }
                        lastFocusState = state
                    },
                keyboardType = keyboardType,
                imeAction = ImeAction.Search,
                textStyle = MaterialTheme.typography.body2,
                onTextInputStarted = { controller -> keyboardController = controller }
            )
            val disableContentColor =
                AmbientEmphasisLevels.current.disabled.applyEmphasis(MaterialTheme.colors.onSurface)
            if (textFieldValue.text.isEmpty() && !focusState) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp),
                    text = hint,
                    style = MaterialTheme.typography.body2.copy(color = disableContentColor)
                )
            }
        }
    }
}
