package com.rajankali.core.data

import com.google.gson.annotations.SerializedName

data class CreditResponse(@SerializedName("cast") val cast: List<Cast>)