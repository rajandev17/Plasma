package com.rajankali.plasma.utils

import com.rajankali.core.data.PagedResponse
import com.rajankali.plasma.data.model.LatestData

fun <T> PagedResponse<T?>.toData(data: List<T> = emptyList()) =
    LatestData<T>((data + results).filterNotNull())