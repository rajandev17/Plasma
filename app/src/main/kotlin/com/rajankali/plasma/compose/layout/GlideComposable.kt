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

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.FrameManager
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.stateFor
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.ContextAmbient
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.bumptech.glide.request.transition.Transition
import com.rajankali.core.extensions.matchParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// reference https://github.com/mvarnagiris/compose-glide-image/blob/master/compose-glide-image/src/main/java/com/koduok/compose/glideimage/GlideImage.kt
@Composable
fun GlideImage(
    modifier: Modifier = Modifier.matchParent(),
    model: Any,
    onImageReady: (() -> Unit)? = null,
    customize: RequestBuilder<Bitmap>.() -> RequestBuilder<Bitmap> = { this }
) {
    Box(modifier = modifier) {
        WithConstraints {
            val image = stateFor<ImageAsset?>(null) { null }
            val drawable = stateFor<Drawable?>(null) { null }
            val context = ContextAmbient.current

            onCommit(model) {
                val glide = Glide.with(context)
                var target: CustomTarget<Bitmap>? = null
                val job = CoroutineScope(Dispatchers.Main).launch {
                    target = object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                            image.value = null
                            drawable.value = placeholder
                        }

                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            FrameManager.ensureStarted()
                            image.value = resource.asImageAsset()
                            onImageReady?.invoke()
                        }
                    }

                    val width = if (constraints.maxWidth > 0 && constraints.maxWidth < Int.MAX_VALUE) {
                        constraints.maxWidth
                    } else {
                        SIZE_ORIGINAL
                    }

                    val height = if (constraints.maxHeight > 0 && constraints.maxHeight < Int.MAX_VALUE) {
                        constraints.maxHeight
                    } else {
                        SIZE_ORIGINAL
                    }

                    glide.asBitmap().load(model)
                            .override(width, height)
                            .let(customize)
                            .into(target!!)
                }

                onDispose {
                    image.value = null
                    drawable.value = null
                    glide.clear(target)
                    job.cancel()
                }
            }

            val theImage = image.value
            val theDrawable = drawable.value
            if (theImage != null) {
                Image(asset = theImage)
            } else if (theDrawable != null) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawIntoCanvas { theDrawable.draw(it.nativeCanvas) }
                }
            }
        }
    }
}
