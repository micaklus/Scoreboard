package org.sportradar.data.repository

import org.sportradar.domain.model.Match
import org.sportradar.domain.model.Team
import org.sportradar.domain.repository.ScoreboardRepository

class InMemoryScoreboardRepository : ScoreboardRepository {
    private val matches = mutableListOf<Match>()

    override fun addMatch(match: Match) {
       matches.add(match)
    }

    override fun updateMatch(match: Match) {
      //Placeholder because match is already mutable and changed in Api
    }

    override fun deleteMatch(match: Match) {
       matches.remove(match)
    }

    override fun findMatch(homeTeam: Team, awayTeam: Team): Match? {
       return matches.find { it.homeTeam == homeTeam && it.awayTeam == awayTeam}
    }

    override fun getAllOngoingMatches(): List<Match> {
        return matches.sortedWith(compareByDescending<Match> { it.totalScore.value }.thenByDescending { it.id })
    }

}