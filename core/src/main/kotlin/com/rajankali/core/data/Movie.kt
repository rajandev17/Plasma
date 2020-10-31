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