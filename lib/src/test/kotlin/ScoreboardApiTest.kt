import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.sportradar.ScoreboardApi
import org.sportradar.domain.model.Match
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
        val match1 = Match(Team("Mexico"), Team("Canada"), Score(0), Score(0))
        val match2 = Match(Team("Slovenia"), Team("Croatia"), Score(0), Score(0))
        val match3 = Match(Team("Australia"), Team("Germany"), Score(0), Score(0))

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
        fail("Test not implemented yet")
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
        fail("Test not implemented yet")
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
        fail("Test not implemented yet")
    }

    @Test
    fun `should allow a team to start a new match after finishing the previous one`() {
        fail("Test not implemented yet")
    }

    @Test
    fun `should allow finishing a match without updating the score`() {
        fail("Test not implemented yet")
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
        fail("Test not implemented yet")
    }

    @Test
    fun `should allow updating scores with the same values`() {
        fail("Test not implemented yet")
    }

    @Test
    fun `should not allow updating scores for a non-existing match`() {
        fail("Test not implemented yet")
    }

    @Test
    fun `should allow lowering scores with a valid reason`() {
        fail("Test not implemented yet")
    }

    @Test
    fun `should correctly update overall scores for all matches`() {
        fail("Test not implemented yet")
    }

    // Match Restrictions Tests
    @Test
    fun `should not allow a team to play two matches at the same time`() {
        fail("Test not implemented yet")
    }

    @Test
    fun `should handle a large number of matches without performance issues`() {
        fail("Test not implemented yet")
    }
}