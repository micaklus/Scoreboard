/**
 * @file ScoreboardApi.kt
 * This file contains the implementation of the `ScoreboardApi` class within the `org.sportradar` package.
 *
 * The `ScoreboardApi` class acts as the main interface to manage sports matches, handling operations such as
 * starting matches, updating scores, finishing matches, and retrieving ongoing matches. It interacts with a
 * `ScoreboardRepository` to manage match data persistently or in-memory.
 */

package org.sportradar

import org.sportradar.data.repository.InMemoryScoreboardRepository
import org.sportradar.domain.model.Match
import org.sportradar.domain.model.Reason
import org.sportradar.domain.model.Score
import org.sportradar.domain.model.Team
import org.sportradar.domain.repository.ScoreboardRepository

/**
 * ScoreboardApi is a high-level API for managing a scoreboard of sports matches.
 * It provides methods to start, update, and finish matches, as well as query ongoing matches.
 * The API relies on a `ScoreboardRepository` to persist match data.
 *
 * @constructor Creates a `ScoreboardApi` instance with a given repository for storing and retrieving match data.
 * @param repository The repository responsible for handling match data persistence.
 *
 * **Key Functionalities:**
 * - **Start a Match:** Ensures rules are followed such as unique teams and non-duplication of matches.
 * - **Update Scores:** Updates match scores with validation, including checks for score lowering.
 * - **Finish a Match:** Terminates an ongoing match and removes it from the repository.
 * - **Query Matches:** Retrieves a list of all ongoing matches.
 *
 * **Example Usage:**
 * ```
 * val api = ScoreboardApi.create()
 * val homeTeam = Team("Team A")
 * val awayTeam = Team("Team B")
 * api.startMatch(homeTeam, awayTeam)
 * ```
 */
public class ScoreboardApi private constructor(private val repository: ScoreboardRepository) {

    /**
     * Starts a new match between two teams.
     *
     * @param homeTeam The home team. Must be a unique team.
     * @param awayTeam The away team. Must be different from the home team.
     * @throws IllegalArgumentException If the teams are not unique, one of the teams is already in a match,
     *                                  or a match between the given teams already exists.
     */
    public fun startMatch(homeTeam: Team, awayTeam: Team) {
        require(homeTeam != awayTeam) { "Teams must have unique names" }
        val matches = repository.getAllOngoingMatches()
        require(repository.findMatch(homeTeam, awayTeam) == null) {
            "Match between these teams already exists"
        }
        require(!repository.isTeamInAnyMatch(homeTeam)) {
            "${homeTeam.name} is already playing another match"
        }
        require(!repository.isTeamInAnyMatch(awayTeam)) {
            "${awayTeam.name} is already playing another match"
        }
        repository.addMatch(Match(0, homeTeam, awayTeam, Score(0), Score(0)))
    }

    /**
     * Updates the score of an ongoing match between two teams.
     *
     * @param homeTeam The home team for which the score will be updated.
     * @param awayTeam The away team for which the score will be updated.
     * @param homeScore The new score value for the home team.
     * @param awayScore The new score value for the away team.
     * @param reasonForUpdate A reason for updating the score. This is mandatory if the new score
     *                        is lower than the current score.
     * @throws IllegalArgumentException If the match is not found or if the score is being lowered without a valid reason.
     */
    public fun updateScore(
        homeTeam: Team,
        awayTeam: Team,
        homeScore: Score,
        awayScore: Score,
        reasonForUpdate: Reason? = null
    ) {
        val match = repository.findMatch(homeTeam, awayTeam) ?: throw IllegalArgumentException("Match not found")
        if (homeScore.value < match.homeScore.value || awayScore.value < match.awayScore.value) {
            require(!reasonForUpdate?.message.isNullOrBlank()) {
                "A valid reason must be provided for lowering the score"
            }
        }
        repository.updateMatch(Match(match.id, homeTeam, awayTeam, homeScore, awayScore))
    }

    /**
     * Finishes and removes a match between two teams from the scoreboard.
     *
     * @param homeTeam The home team of the match to be removed.
     * @param awayTeam The away team of the match to be removed.
     * @throws IllegalArgumentException If the match does not exist.
     */
    public fun finishMatch(homeTeam: Team, awayTeam: Team) {
        val match = repository.findMatch(homeTeam, awayTeam)
        match?.let { repository.deleteMatch(it) } ?: throw IllegalArgumentException("Match not found")
    }


    /**
     * Retrieves a list of all ongoing matches currently stored in the scoreboard.
     *
     * @return A list of `Match` objects representing all ongoing matches.
     */
    public fun getAllOngoingMatches(): List<Match> {
        return repository.getAllOngoingMatches()
    }

    /**
     * Factory method to create a new instance of `ScoreboardApi` with an in-memory repository.
     *
     * @return A new instance of `ScoreboardApi` using `InMemoryScoreboardRepository` for match storage.
     */
    public companion object {
        public fun create(): ScoreboardApi {
            val repository = InMemoryScoreboardRepository()
            return ScoreboardApi(repository)
        }
    }
}