package com.rajankali.plasma.enums

enum class MediaType(val type: String) {
    TV("tv"),
    MOVIE("movie"),
    ALL("all");

    companion object{
        operator fun get(type: String): MediaType{
            return when(type){
                TV.type -> TV
                MOVIE.type -> MOVIE
                ALL.type -> ALL
                else -> ALL
            }
        }
    }
}