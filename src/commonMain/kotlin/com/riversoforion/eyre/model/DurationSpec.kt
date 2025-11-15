package com.riversoforion.eyre.model

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private const val SECOND = 1L
private const val MINUTE = 60L
private const val HOUR = MINUTE * 60
private const val DAY = HOUR * 24

data class DurationSpec(val seconds: Long) {

    fun toDuration(): Duration {
        return seconds.toDuration(DurationUnit.SECONDS)
    }

    companion object {

        fun parse(spec: String): DurationSpec {
            val s = spec.trim().lowercase()
            val regex = Regex("^([0-9]+)([smhd])$")
            val match = regex.matchEntire(s)
                        ?: throw IllegalArgumentException("Invalid duration format: '$spec' (expected e.g. 1h, 30m)")

            val value = match.groupValues[1].toLong()
            val multiplier = when (val unit = match.groupValues[2][0]) {
                's'  -> SECOND
                'm'  -> MINUTE
                'h'  -> HOUR
                'd'  -> DAY
                else -> throw IllegalArgumentException("Unsupported duration unit: $unit")
            }
            return DurationSpec(value * multiplier)
        }

        @Suppress("detekt:SwallowedException")
        private fun parseSilently(spec: String): DurationSpec? {
            return try {
                parse(spec)
            } catch (_: IllegalArgumentException) {
                null
            }
        }

        fun fromDuration(duration: Duration): DurationSpec {
            val seconds = duration.inWholeSeconds
            return DurationSpec(seconds)
        }
    }

    override fun toString(): String = "${seconds}s"

    override fun equals(other: Any?): Boolean = when (other) {
        is DurationSpec -> seconds == other.seconds
        is Duration     -> seconds == other.inWholeSeconds
        is String       -> parseSilently(other)?.seconds == seconds
        else            -> false
    }

    override fun hashCode(): Int {
        return seconds.hashCode()
    }
}
