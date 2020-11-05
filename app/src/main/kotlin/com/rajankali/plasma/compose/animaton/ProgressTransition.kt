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

package com.rajankali.plasma.compose.animaton

import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.transitionDefinition
import com.rajankali.plasma.ui.plamsaGradient

val colorPropKey = ColorPropKey()

val ProgressColorTransition = transitionDefinition<Int> {
    state(0){
        this[colorPropKey] = plamsaGradient[0]
    }
    state(1){
        this[colorPropKey] = plamsaGradient[2]
    }
    transition(fromState = 0, toState = 1){
        colorPropKey using repeatable(
            iterations = AnimationConstants.Infinite,
            animation = keyframes {
                durationMillis = 300
                plamsaGradient[1] at 100
            }
        )
    }
}