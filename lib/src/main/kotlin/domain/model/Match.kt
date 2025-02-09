package org.sportradar.domain.model

data class Match(
    val id: Long,
    val homeTeam: Team,
    val awayTeam: Team,
    val homeScore: Score = Score(0),
    val awayScore: Score = Score(0),
    val reason : Reason? = null
) {
    val totalScore: Score
        get() = homeScore + awayScore
}
