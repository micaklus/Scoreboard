package org.sportradar.data.repository

import org.sportradar.domain.model.Match
import org.sportradar.domain.model.Team
import org.sportradar.domain.repository.ScoreboardRepository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class InMemoryScoreboardRepository : ScoreboardRepository {

    private val matchCounter = AtomicLong(0)
    private val matches: MutableMap<Pair<Team, Team>, Match> = ConcurrentHashMap()


    override fun addMatch(match: Match) {
        val id = matchCounter.getAndIncrement() // Generate unique ID
        val key = Pair(match.homeTeam, match.awayTeam)
        matches[key] = match.copy(id = id)

    }

    override fun updateMatch(match: Match) {
        val key = Pair(match.homeTeam, match.awayTeam)
        matches[key] = match
    }

    override fun isTeamInAnyMatch(team: Team): Boolean {
        return matches.any { it.component1().first == team || it.component1().second == team }
    }


    override fun deleteMatch(match: Match) {
        val key = Pair(match.homeTeam, match.awayTeam)
        matches.remove(key)
    }

    override fun findMatch(homeTeam: Team, awayTeam: Team): Match? {
        val key = Pair(homeTeam, awayTeam)
        return matches[key]

    }

    override fun getAllOngoingMatches(): List<Match> {
        return matches.values.toList().sortedWith(compareByDescending<Match> { it.totalScore.value }.thenByDescending { it.id })
    }

}