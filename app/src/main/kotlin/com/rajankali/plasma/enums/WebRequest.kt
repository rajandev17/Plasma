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

package com.rajankali.plasma.enums

enum class WebRequest(val key: Int, val url : String, val title: String) {
    STACK_OVERFLOW(1, "https://stackoverflow.com/users/3159267/rajan-ks", "StackOverflow | Rajan Ks"),
    LINKED_IN(2, "https://www.linkedin.com/in/rajan-ks/", "LinkedIn | Rajan Ks"),
    GITHUB(3, "https://github.com/rajandev17", "GitHub | Rajan Ks"),
    MEDIUM(4, "https://medium.com/@rajanks", "Medium | Rajan Ks"),
    PLASMA(5, "https://rajandev17.github.io/Plasma/", "https://rajandev17.github.io/Plasma/");

    companion object{
        operator fun get(key: Int): WebRequest{
            return when(key){
                STACK_OVERFLOW.key -> STACK_OVERFLOW
                LINKED_IN.key -> LINKED_IN
                GITHUB.key -> GITHUB
                MEDIUM.key -> MEDIUM
                PLASMA.key -> PLASMA
                else -> STACK_OVERFLOW
            }
        }
    }


}