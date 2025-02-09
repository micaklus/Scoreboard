package org.sportradar.domain.repository

import org.sportradar.domain.model.Match
import org.sportradar.domain.model.Team

interface ScoreboardRepository {
    fun addMatch(match: Match)
    fun updateMatch(match: Match)
    fun deleteMatch(match: Match)
    fun findMatch(homeTeam: Team, awayTeam: Team): Match?
    fun getAllOngoingMatches(): List<Match>

}