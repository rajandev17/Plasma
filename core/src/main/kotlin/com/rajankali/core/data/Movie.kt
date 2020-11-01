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

package com.rajankali.core.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rajankali.core.network.NetworkModule
import com.rajankali.core.utils.movieDisplayDateFormat
import com.rajankali.core.utils.movieResponseDateFormat
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.text.ParseException
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
@Entity(tableName = "movie")
data class Movie(
    var userId: Long = 0,
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title", alternate = ["name", "title"])
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date", alternate = ["first_air_date"])
    val releaseDate: String?,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
) : Parcelable{

    @Ignore
    @SerializedName("genre_ids")
    @IgnoredOnParcel
    val genreIds: List<Int> = ArrayList()

    @IgnoredOnParcel
    val posterImage: String
        get() = String.format(NetworkModule.POSTER_PATH, posterPath)

    @IgnoredOnParcel
    val isValidMedia: Boolean
    get() = mediaType.toLowerCase(Locale.ENGLISH) == "tv" || mediaType.toLowerCase(Locale.ENGLISH) == "movie"

    @IgnoredOnParcel
    val thumbnail: String
        get() = String.format(NetworkModule.THUMB_NAIL_PATH, posterPath)

    @IgnoredOnParcel
    val date: String get() {
        return try { releaseDate?.let {  movieResponseDateFormat.parse(releaseDate)?.let { movieDisplayDateFormat.format(it) }?:"N/A" }?:""}catch (pe: ParseException) { ""}
    }
}