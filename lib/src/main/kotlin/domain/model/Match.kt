package org.sportradar.domain.model

data class Match(
    val id: Long,
    val homeTeam: Team,
    val awayTeam: Team,
    private var _homeScore: Score = Score(0),
    private var _awayScore: Score = Score(0)
) {
    // Public read-only accessors for immutability
    val homeScore: Score get() = _homeScore
    val awayScore: Score get() = _awayScore

    val totalScore: Score
        get() = _homeScore + _awayScore

    // Internal update function (only Scoreboard can use it)
    internal fun updateScore(homeScore: Score, awayScore: Score) {
        _homeScore = homeScore
        _awayScore = awayScore
    }
}
