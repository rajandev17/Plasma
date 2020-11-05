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

import android.annotation.SuppressLint
import androidx.compose.animation.DpPropKey
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.IntPropKey
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.dp
import com.rajankali.plasma.compose.animaton.MainTransitionState.END
import com.rajankali.plasma.compose.animaton.MainTransitionState.START

val titleIndexPropKey = IntPropKey()
val titleOffsetPropKey = DpPropKey()
val alphaPropKey = FloatPropKey()

enum class MainTransitionState{
    START, END
}

@SuppressLint("Range")
val mainFragmentTransition = transitionDefinition<MainTransitionState> {
    state(START){
        this[titleIndexPropKey] = 0
        this[titleOffsetPropKey] = 0.dp
        this[alphaPropKey] = 0F
    }
    state(END){
        this[titleIndexPropKey] = 6
        this[titleOffsetPropKey] = (-150).dp
        this[alphaPropKey] = 1F
    }

    transition {
        titleIndexPropKey using keyframes {
            durationMillis = 600
            1 at 100
            2 at 200
            3 at 300
            4 at 400
            5 at 500
        }
        titleOffsetPropKey using tween(durationMillis = 500, delayMillis = 1100, easing = FastOutLinearInEasing)
        alphaPropKey using tween(durationMillis = 1000, delayMillis = 1600, easing = LinearOutSlowInEasing)
    }

}