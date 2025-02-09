import kotlin.test.Test
import kotlin.test.fail

class ScoreboardTest {

    // Match Creation Tests
    @Test
    fun `should not allow starting a match with duplicate team names`() {
        fail("Test not implemented yet")
    }

    @Test
    fun `should start a new match with initial score 0-0`() {
        fail("Test not implemented yet")
    }

    @Test
    fun `should not allow starting a match with blank team names`() {
        fail("Test not implemented yet")
    }

    // Managing Matches Tests
    @Test
    fun `should finish a match and remove it from the scoreboard`() {
        fail("Test not implemented yet")
    }

    @Test
    fun `should remove finished matches from the summary`() {
        fail("Test not implemented yet")
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