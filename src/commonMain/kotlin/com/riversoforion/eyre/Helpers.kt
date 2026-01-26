package com.riversoforion.eyre

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock.System.now
import kotlin.time.Instant

fun currentTime(): LocalDateTime {
    return now().toLocalDateTime(TimeZone.currentSystemDefault())
}

fun utcTime(): LocalDateTime {
    return now().toLocalDateTime(TimeZone.UTC)
}

fun Instant.toUtc(): LocalDateTime {
    return toLocalDateTime(TimeZone.UTC)
}

fun LocalDateTime.utcToLocal(): LocalDateTime {
    return toInstant(TimeZone.UTC).toLocalDateTime(TimeZone.currentSystemDefault())
}
