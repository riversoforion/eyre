package com.riversoforion.eyre.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs
import kotlinx.datetime.LocalDate
import kotlin.time.Duration
import kotlin.time.DurationUnit.HOURS
import kotlin.time.DurationUnit.MINUTES
import kotlin.time.DurationUnit.SECONDS
import kotlin.time.toDuration

class DurationSpecTests : DescribeSpec(
    {
        // These tests should be using Data Driven Tests, but there is currently an issue with emitting test events in
        // Kotlin/Native. Once that issue is fixed, these can be converted to real Data Driven Tests.
        // See https://github.com/kotest/kotest/issues/5108
        describe("DurationSpec") {
            describe("constructor") {
                val testCases =
                        listOf(
                            ConstructValidCase(52),
                            ConstructValidCase(50_000),
                            ConstructValidCase(50_000_000),
                        )

                it("sets seconds correctly") {
                    for ((seconds) in testCases) {
                        DurationSpec(seconds) should haveSeconds(seconds)
                    }
                }
            }

            describe("parse") {
                val validTestCases =
                        listOf(
                            ParseValidCase("3s", 3),
                            ParseValidCase("90m", 90 * 60),
                            ParseValidCase("12h", 12 * 60 * 60),
                            ParseValidCase("22d", 22 * 24 * 60 * 60),
                        )

                it("handles valid duration specs correctly") {
                    for ((spec, expected) in validTestCases) {
                        DurationSpec.parse(spec) should haveSeconds(expected)
                    }
                }

                val invalidTestCases =
                        listOf(
                            ParseInvalidCase("foo"),
                            ParseInvalidCase("3"),
                            ParseInvalidCase("m"),
                            ParseInvalidCase(""),
                            ParseInvalidCase("   "),
                            ParseInvalidCase("%"),
                        )

                it("throws exception for invalid duration specs") {
                    for ((spec) in invalidTestCases) {
                        shouldThrow<IllegalArgumentException> { DurationSpec.parse(spec) }
                    }
                }
            }

            describe("fromDuration") {
                val validTestCases = listOf(
                    FromDurationValidCase(24.toDuration(HOURS)),
                    FromDurationValidCase(5.toDuration(MINUTES)),
                    FromDurationValidCase(30.toDuration(SECONDS)),
                )

                it("sets seconds correctly") {
                    for ((duration) in validTestCases) {
                        DurationSpec.fromDuration(duration) should haveSeconds(duration.inWholeSeconds)
                    }
                }
            }

            describe("toDuration") {
                it("creates Duration correctly") {
                    DurationSpec(123).toDuration() shouldBe 123.toDuration(SECONDS)
                    DurationSpec(28_800).toDuration() shouldBe 8.toDuration(HOURS)
                }
            }

            describe("equalsAndHashCode") {
                it("compares DurationSpec instances correctly") {
                    val first = DurationSpec.parse("8h")
                    val second = DurationSpec.parse("28800s")
                    val third = DurationSpec.parse("3m")

                    first shouldBe second
                    first shouldHaveSameHashCodeAs second
                    first shouldNotBe third
                    first shouldNotHaveSameHashCodeAs third
                }

                it("compares Duration instances correctly") {
                    val durationSpec = DurationSpec.parse("28800s")
                    val eightHours = 8.toDuration(HOURS)
                    val sixMinutes = 6.toDuration(MINUTES)

                    durationSpec shouldBe eightHours
                    durationSpec shouldNotBe sixMinutes
                }

                it("compares String instances correctly") {
                    val durationSpec = DurationSpec.parse("28800s")

                    durationSpec shouldBe "8h"
                    durationSpec shouldBe "28800s"
                    durationSpec shouldNotBe "28801s"
                    durationSpec shouldNotBe "invalid"
                }

                it("compares other types correctly") {
                    val durationSpec = DurationSpec.parse("28800s")

                    durationSpec shouldNotBe 8
                    durationSpec shouldNotBe 8.0
                    durationSpec shouldNotBe LocalDate(2025, 6, 2)
                }
            }

            describe("toString") {
                it("creates a correct string representation") {
                    DurationSpec.parse("5s").toString() shouldBe "5s"
                    DurationSpec.parse("8h").toString() shouldBe "28800s"
                }
            }
        }
    }
)

data class ConstructValidCase(val seconds: Long)
data class ParseValidCase(val spec: String, val expected: Long)
data class ParseInvalidCase(val spec: String)
data class FromDurationValidCase(val duration: Duration)

fun haveSeconds(seconds: Long) = Matcher<DurationSpec> { value ->
    MatcherResult(
        value.seconds == seconds,
        { "duration was ${value.seconds}s, but expected ${seconds}s" },
        { "duration should not be ${seconds}s" },
    )
}
