import org.junit.jupiter.api.Assertions.assertEquals
import org.sportradar.ScoreboardApi
import org.sportradar.data.repository.InMemoryScoreboardRepository
import org.sportradar.domain.model.Score
import org.sportradar.domain.model.Team
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.Test

class ScoreboardApiThreadSafetyTest {
    private val api = ScoreboardApi.create()

    @Test
    fun `test concurrent startMatch operations`() {
        val numThreads = 10000
        val executor = Executors.newFixedThreadPool(numThreads)

        // Teams for distinct matches
        // Retrieve list of Team objects from the generator
        val allStartTeams: List<Team> = TeamNameGenerator.generateTeamNames(numThreads * 2)

        // Pair up teams as homeTeam and awayTeam for matches
        val teams = allStartTeams.chunked(2).map { Pair(it[0], it[1]) }


        // Each thread will start a match
        for ((homeTeam, awayTeam) in teams) {
            executor.submit {
                api.startMatch(homeTeam, awayTeam)
            }
        }

        // Wait for all threads to finish
        executor.shutdown()
        executor.awaitTermination(10, TimeUnit.SECONDS)

        // Assert all matches were added correctly
        val ongoingMatches = api.getAllOngoingMatches()
        assertEquals(numThreads, ongoingMatches.size)

        // Assert there are no duplicate teams in matches
        val allTeams = ongoingMatches.flatMap { listOf(it.homeTeam, it.awayTeam) }.toSet()
        assertEquals(numThreads * 2, allTeams.size)
    }

    @Test
    fun `test concurrent updateScore operations`() {
        val numThreads = 1999
        val executor = Executors.newFixedThreadPool(numThreads)

        val homeTeam = Team("Home")
        val awayTeam = Team("Away")

        // Start the match
        api.startMatch(homeTeam, awayTeam)

        // Each thread updates the score
        for (i in 1..numThreads) {
            executor.submit {
                api.updateScore(homeTeam, awayTeam, Score(i), Score(i))
            }
        }

        // Wait for all threads to finish
        executor.shutdown()
        executor.awaitTermination(10, TimeUnit.SECONDS)

        // Verify the final score is consistent with the last thread's update
        val updatedMatch = api.getAllOngoingMatches().find { it.homeTeam == homeTeam && it.awayTeam == awayTeam }
        assertEquals(numThreads, updatedMatch?.homeScore?.value)
        assertEquals(numThreads, updatedMatch?.awayScore?.value)
    }

    @Test
    fun `test concurrent finishMatch operations`() {
        val homeTeam = Team("Home")
        val awayTeam = Team("Away")

        // Start a match to be finished
        api.startMatch(homeTeam, awayTeam)

        val numThreads = 1000
        val executor = Executors.newFixedThreadPool(numThreads)

        // Multiple threads will try to finish the same match
        for (i in 1..numThreads) {
            executor.submit {
                try {
                    api.finishMatch(homeTeam, awayTeam)
                } catch (e: Exception) {
                    // Ignore exceptions, as some threads will not find the match
                }
            }
        }

        // Wait for all threads to finish
        executor.shutdown()
        executor.awaitTermination(10, TimeUnit.SECONDS)

        // Assert match is no longer in the repository
        val ongoingMatches = api.getAllOngoingMatches()
        assertEquals(0, ongoingMatches.size)
    }
}
