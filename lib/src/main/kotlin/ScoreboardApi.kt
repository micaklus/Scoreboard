package org.sportradar

import org.sportradar.data.repository.InMemoryScoreboardRepository
import org.sportradar.domain.model.Match
import org.sportradar.domain.model.Reason
import org.sportradar.domain.model.Score
import org.sportradar.domain.model.Team
import org.sportradar.domain.repository.ScoreboardRepository
import java.util.concurrent.atomic.AtomicLong


class ScoreboardApi(private val repository: ScoreboardRepository) {

    fun startMatch(homeTeam: Team, awayTeam: Team) {
        require(homeTeam != awayTeam) { "Teams must have unique names" }
        val matches = repository.getAllOngoingMatches()
        require(matches.none { it.homeTeam == homeTeam || it.awayTeam == homeTeam }) {
            "${homeTeam.name} is already playing another match"
        }
        require(matches.none { it.homeTeam == awayTeam || it.awayTeam == awayTeam }) {
            "${awayTeam.name} is already playing another match"
        }
        require(matches.none { it.homeTeam == homeTeam && it.awayTeam == awayTeam }) {
            "Match between these teams already exists"
        }
        repository.addMatch(Match(0,homeTeam, awayTeam, Score(0), Score(0)))
    }

    fun updateScore(homeTeam: Team, awayTeam: Team, homeScore: Score, awayScore: Score, reasonForUpdate: Reason? = null) {
        val match = repository.findMatch(homeTeam, awayTeam) ?: throw IllegalArgumentException("Match not found")
        if (homeScore.value < match.homeScore.value || awayScore.value < match.awayScore.value) {
            require(!reasonForUpdate?.message.isNullOrBlank()) {
                "A valid reason must be provided for lowering the score"
            }
        }
        match.updateScore(homeScore, awayScore)
        repository.updateMatch(match)
    }

    fun finishMatch(homeTeam: Team, awayTeam: Team) {
        val match = repository.findMatch(homeTeam, awayTeam)
        match?.let { repository.deleteMatch(it) } ?: throw IllegalArgumentException("Match not found")
    }

    fun getAllOngoingMatches(): List<Match> {
        return repository.getAllOngoingMatches()
    }

    companion object {
        fun create(): ScoreboardApi {
            val repository = InMemoryScoreboardRepository()
            return ScoreboardApi(repository)
        }
    }
}