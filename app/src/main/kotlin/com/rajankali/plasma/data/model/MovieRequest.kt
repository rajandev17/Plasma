package com.rajankali.plasma.data.model

import com.rajankali.plasma.enums.MediaType
import com.rajankali.plasma.enums.TimeWindow
import kotlinx.coroutines.ExperimentalCoroutinesApi

data class MovieRequest @ExperimentalCoroutinesApi constructor(val mediaType: MediaType,val timeWindow: TimeWindow)