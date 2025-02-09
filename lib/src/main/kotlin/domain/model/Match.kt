/**
 * @file Match.kt
 * This file contains the implementation of the `Match` class, which is part of the `org.sportradar.domain.model` package.
 *
 * The `Match` class models a sports match, including the teams participating, their scores, and an optional reason for the match's status.
 */

package org.sportradar.domain.model

/**
 * Represents a sports match between two teams.
 *
 * @property id The unique identifier for the match.
 * @property homeTeam The home team participating in the match.
 * @property awayTeam The away team participating in the match.
 * @property homeScore The score of the home team (default is 0).
 * @property awayScore The score of the away team (default is 0).
 * @property reason (Optional) The reason associated with the match (e.g., cancellation or delay).
 * @property totalScore A computed property that returns the sum of the home and away scores.
 *
 * Example:
 * ```
 * val homeTeam = Team("Liverpool")
 * val awayTeam = Team("Chelsea")
 * val match = Match(1, homeTeam, awayTeam, Score(3), Score(2))
 * println(match.totalScore) // Output: Score(5)
 * ```
 */
data class Match(
    val id: Long,
    val homeTeam: Team,
    val awayTeam: Team,
    val homeScore: Score = Score(0),
    val awayScore: Score = Score(0),
    val reason: Reason? = null
) {
    /**
     * Computes the total score of both teams by adding home and away scores.
     */
    val totalScore: Score
        get() = homeScore + awayScore
}
