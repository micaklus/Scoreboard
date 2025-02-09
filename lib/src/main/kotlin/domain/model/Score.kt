/**
 * @file Score.kt
 * This file contains the implementation of the `Score` class, which is part of the `org.sportradar.domain.model` package.
 *
 * The `Score` class models the score for a sports match, ensuring that it remains within a valid range, and provides
 * functionality to add scores together.
 */

package org.sportradar.domain.model

/**
 * Represents the score in a sports match.
 *
 * @property value The score value, which must be between 0 and 2000 (inclusive).
 *                 Values must reflect realistic scores for a soccer match.
 * @throws IllegalArgumentException If the score is negative or exceeds 2000.
 *
 * Example:
 * ```
 * val score1 = Score(3)
 * val score2 = Score(1)
 * val total = score1 + score2 // Outputs Score(4)
 * ```
 */
data class Score(val value: Int) {
    init {
        require(value >= 0) { "Score cannot be negative" }
        require(value <= 2000) { "Too big score for a soccer match" }
    }

    /**
     * Adds this score to another score and returns a new `Score` object.
     *
     * @param other The other `Score` to add.
     * @return A new `Score` representing the sum of the two scores.
     */
    operator fun plus(other: Score): Score {
        return Score(this.value + other.value)
    }
}

