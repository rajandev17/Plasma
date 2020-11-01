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

package com.rajankali.core.network

import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

var isConnectedToNetwork = true

suspend fun <T : Any> handleApi(
    call: suspend () -> Response<T>,
    errorMessage: String = "Problem Fetching data at the moment!"
): ApiResult<T> {
    try {
        val response = call()
        if (response.isSuccessful) {
            isConnectedToNetwork = true
            response.body()?.let {
                return Success(it)
            }
        }
        response.errorBody()?.let {
            return try {
                val errorString  = it.string()
                val errorObject = JSONObject(errorString)
                Failure(errorObject.getString("status_message")?:errorMessage)
            } catch (ignored: JsonSyntaxException) {
                Failure(ignored.message)
            }
        }
        return Failure(errorMessage = errorMessage)
    } catch (e: Exception) {
        if (e is IOException) {
            isConnectedToNetwork = false
        }
        return Failure(e.message?:errorMessage)
    }
}