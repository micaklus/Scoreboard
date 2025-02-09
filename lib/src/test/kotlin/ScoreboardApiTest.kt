import TeamNameGenerator.generateTeamNames
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.sportradar.ScoreboardApi
import org.sportradar.domain.model.Match
import org.sportradar.domain.model.Reason
import org.sportradar.domain.model.Score
import org.sportradar.domain.model.Team
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ScoreboardApiTest {
    private lateinit var scoreboardApi: ScoreboardApi

    @BeforeEach
    fun setup() {
        scoreboardApi = ScoreboardApi.create()
    }

    // Match summary
    @Test
    fun `should return all ongoing matches from repository`() {
        // Arrange: Start 3 matches
        val match1 = Match(0,Team("Mexico"), Team("Canada"), Score(0), Score(0))
        val match2 = Match(1,Team("Slovenia"), Team("Croatia"), Score(0), Score(0))
        val match3 = Match(2,Team("Australia"), Team("Germany"), Score(0), Score(0))

        scoreboardApi.startMatch(match1.homeTeam, match1.awayTeam)
        scoreboardApi.startMatch(match2.homeTeam, match2.awayTeam)
        scoreboardApi.startMatch(match3.homeTeam, match3.awayTeam)

        // Act: Get all ongoing matches
        val matches = scoreboardApi.getAllOngoingMatches()

        // Assert: Check that the size of the matches is 3
        assertEquals(3, matches.size)

        // Assert: Check that all matches are present in the list
        assertTrue(matches.contains(match1))
        assertTrue(matches.contains(match2))
        assertTrue(matches.contains(match3))

    }


    // Match Creation Tests
    @Test
    fun `should not allow starting a match with duplicate team names`() {
        val teamName = Team("HomeTeam")

        // Attempt to start a match where both teams have the same name
        val exception = assertThrows<IllegalArgumentException> {
            scoreboardApi.startMatch(teamName, teamName)
        }

        // Verify that the exception message matches the expected validation rule
        assertEquals("Teams must have unique names", exception.message)
    }

    @Test
    fun `should start a new match with initial score 0-0`() {
        scoreboardApi.startMatch(Team("Mexico"), Team("Canada"))
        val matches = scoreboardApi.getAllOngoingMatches()
        assertEquals(1, matches.size)
        assertEquals(Team("Mexico"), matches[0].homeTeam)
        assertEquals(Team("Canada"), matches[0].awayTeam)
        assertEquals(0, matches[0].homeScore.value)
        assertEquals(0, matches[0].awayScore.value)
    }

    @Test
    fun `should not allow starting a match with blank team names`() {
        val exception = assertThrows<IllegalArgumentException> {
            scoreboardApi.startMatch(Team(""), Team("Brazil"))
        }
        assertEquals("Team name cannot be empty or blank. Please provide a valid name for the team.", exception.message)
    }

    // Managing Matches Tests
    @Test
    fun `should finish a match and remove it from the scoreboard`() {
        scoreboardApi.startMatch(Team("Mexico"), Team("Canada"))
        scoreboardApi.finishMatch(Team("Mexico"), Team("Canada"))
        assertTrue(scoreboardApi.getAllOngoingMatches().isEmpty())
    }

    @Test
    fun `should throw error when finishing a non-existing match`() {
        val exception = assertThrows<IllegalArgumentException> {
            scoreboardApi.finishMatch(Team("Slovenia"), Team("Brazil"))
        }
        assertEquals("Match not found", exception.message)
    }

    @Test
    fun `should allow a team to start a new match after finishing the previous one`() {
        scoreboardApi.startMatch(Team("Mexico"), Team("Canada"))
        scoreboardApi.finishMatch(Team("Mexico"), Team("Canada"))

        assertDoesNotThrow {
            scoreboardApi.startMatch(Team("Mexico"), Team("France"))
        }
    }

    @Test
    fun `should allow finishing a match without updating the score`() {
        scoreboardApi.startMatch(Team("Mexico"), Team("Canada"))
        scoreboardApi.finishMatch(Team("Mexico"), Team("Canada"))

        assertTrue(scoreboardApi.getAllOngoingMatches().isEmpty())
    }

    // Score Updates Tests
    @Test
    fun `should allow update match`() {
        scoreboardApi.startMatch(Team("Mexico"), Team("Canada"))
        scoreboardApi.updateScore(Team("Mexico"), Team("Canada"), Score(1), Score(0))
        val matches = scoreboardApi.getAllOngoingMatches()
        assertEquals(1, matches.size)
        assertEquals(Team("Mexico"), matches[0].homeTeam)
        assertEquals(Team("Canada"), matches[0].awayTeam)
        assertEquals(1, matches[0].homeScore.value)
        assertEquals(0, matches[0].awayScore.value)
    }

    @Test
    fun `should not allow negative scores during updates`() {
        scoreboardApi.startMatch(Team("Mexico"), Team("Canada"))

        val exception = assertThrows<IllegalArgumentException> {
            scoreboardApi.updateScore(Team("Mexico"), Team("Canada"), Score(-4), Score(2))
        }
        assertEquals("Score cannot be negative", exception.message)

        val exception2 = assertThrows<IllegalArgumentException> {
            scoreboardApi.updateScore(Team("Mexico"), Team("Canada"), Score(3), Score(-2))
        }
        assertEquals("Score cannot be negative", exception2.message)
    }

    @Test
    fun `should allow updating scores with the same values`() {
        scoreboardApi.startMatch(Team("Mexico"), Team("Canada"))
        scoreboardApi.updateScore(Team("Mexico"), Team("Canada"), Score(2), Score(1))
        scoreboardApi.updateScore(Team("Mexico"), Team("Canada"), Score(2), Score(1)) // Update with the same values

        val match = scoreboardApi.getAllOngoingMatches().first()
        assertEquals(2, match.homeScore.value)
        assertEquals(1, match.awayScore.value)
    }

    @Test
    fun `should not allow updating scores for a non-existing match`() {
        val exception = assertThrows<IllegalArgumentException> {
            scoreboardApi.updateScore(Team("Slovenia") ,Team("Brazil"),Score(3),Score(4))
        }
        assertEquals("Match not found", exception.message)
    }

    @Test
    fun `should handle large number of matches without issue`() {
        val count = 1000
        val teams = generateTeamNames(count * 2)
        for (i in 0 until count) {
            scoreboardApi.startMatch(teams[i], teams[i + count])
        }
        assertEquals(count, scoreboardApi.getAllOngoingMatches().size)
    }

    @Test
    fun `should correctly update overall scores for all matches`() {
        val teamNames = generateTeamNames(51) // Generate 11 unique team names

        val expectedScores = mutableMapOf<String, Pair<Int, Int>>() // Map to store expected scores
        for (i in 0 until teamNames.size / 2) {
            val homeTeam = teamNames[i * 2]
            val awayTeam = teamNames[i * 2 + 1]

            println("Starting match: $homeTeam vs $awayTeam")

            // Start the match
            scoreboardApi.startMatch(homeTeam, awayTeam)

            // Assign realistic scores
            val homeScore = i * 2 % 10
            val awayScore = i * 3 % 10
            println("Updating score: $homeTeam ($homeScore) - $awayTeam ($awayScore)")
            scoreboardApi.updateScore(homeTeam, awayTeam, Score(homeScore), Score(awayScore))

            // Save expected scores in a map using the team names as key
            expectedScores["$homeTeam vs $awayTeam"] = Pair(homeScore, awayScore)
        }

        // Verify scores are applied correctly for each match in the summary
        val summary = scoreboardApi.getAllOngoingMatches()
        for (match in summary) {
            val expected = expectedScores["${match.homeTeam} vs ${match.awayTeam}"]

            // Check if the match exists in the expected scores
            if (expected == null) fail("Unexpected match found: ${match.homeTeam} vs ${match.awayTeam}")

            val expectedHomeScore = expected.first
            val expectedAwayScore = expected.second

            // Custom messages for detailed error reporting
            val errorMessageHome = "Failed for match '${match.homeTeam} vs ${match.awayTeam}': home team expected $expectedHomeScore, but got ${match.homeScore}"
            val errorMessageAway = "Failed for match '${match.homeTeam} vs ${match.awayTeam}': away team expected $expectedAwayScore, but got ${match.awayScore}"

            assertEquals(expectedHomeScore, match.homeScore.value, errorMessageHome)
            assertEquals(expectedAwayScore, match.awayScore.value, errorMessageAway)
        }
    }

    // Match Restrictions Tests
    @Test
    fun `should not allow a team to play two matches at the same time`() {
        scoreboardApi.startMatch(Team("Mexico"), Team("Canada"))

        val exception1 = assertThrows<IllegalArgumentException> {
            scoreboardApi.startMatch(Team("Mexico"), Team("Brazil"))
        }
        assertEquals("Mexico is already playing another match", exception1.message)

        val exception2 = assertThrows<IllegalArgumentException> {
            scoreboardApi.startMatch(Team("Spain"), Team("Canada"))
        }
        assertEquals("Canada is already playing another match", exception2.message)
    }

    @Test
    fun `should handle a large number of matches without performance issues`() {
        val count = 1000
        val teams = generateTeamNames(count * 2)
        for (i in 0 until count) {
            scoreboardApi.startMatch(teams[i], teams[i + count])
        }
        assertEquals(count, scoreboardApi.getAllOngoingMatches().size)
    }

    @Test
    fun `should return summary sorted by total score and most recent match`() {
        scoreboardApi.startMatch(Team("Mexico"), Team("Canada"))
        scoreboardApi.updateScore(Team("Mexico"), Team("Canada"), Score(0), Score(5))

        scoreboardApi.startMatch(Team("Spain"), Team("Brazil"))
        scoreboardApi.updateScore(Team("Spain"), Team("Brazil"), Score(10), Score(2))

        scoreboardApi.startMatch(Team("Germany"), Team("France"))
        scoreboardApi.updateScore(Team("Germany"), Team("France"), Score(2), Score(2))

        scoreboardApi.startMatch(Team("Uruguay"), Team("Italy"))
        scoreboardApi.updateScore(Team("Uruguay"), Team("Italy"), Score(6), Score(6))

        scoreboardApi.startMatch(Team("Argentina"), Team("Australia"))
        scoreboardApi.updateScore(Team("Argentina"), Team("Australia"), Score(3), Score(1))

        val summary = scoreboardApi.getAllOngoingMatches()
        assertEquals(Team("Uruguay"), summary[0].homeTeam)
        assertEquals(Team("Spain"), summary[1].homeTeam)
        assertEquals(Team("Mexico"), summary[2].homeTeam)
        assertEquals(Team("Argentina"), summary[3].homeTeam)
        assertEquals(Team("Germany"), summary[4].homeTeam)
    }

    @Test
    fun `should allow lowering scores with valid reason`() {
        scoreboardApi.startMatch(Team("Spain"), Team("Portugal"))
        scoreboardApi.updateScore(Team("Spain"), Team("Portugal"), Score(2), Score(1))// Original score

        scoreboardApi.updateScore(Team("Spain"), Team("Portugal"),Score(1),Score(1), Reason("Goal canceled by VAR"))

        val match = scoreboardApi.getAllOngoingMatches().first()
        assertEquals(1, match.homeScore.value)
        assertEquals(1, match.awayScore.value)
    }

    @Test
    fun `should not allow lowering scores without valid reason`() {
        scoreboardApi.startMatch(Team("Spain"), Team("Portugal"))
        scoreboardApi.updateScore(Team("Spain"), Team("Portugal"), Score(2), Score(1))// Original score

        val exception = assertThrows<IllegalArgumentException> {
            scoreboardApi.updateScore(Team("Spain"), Team("Portugal"),Score(1),Score(1))
        }
        assertEquals("A valid reason must be provided for lowering the score", exception.message)
        val match = scoreboardApi.getAllOngoingMatches().first()
        assertEquals(2, match.homeScore.value)
        assertEquals(1, match.awayScore.value)
    }
}