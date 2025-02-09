import org.sportradar.domain.model.Team

object TeamNameGenerator {
    private const val TEAM_PREFIX = "Team"

    fun generateTeamNames(count: Int): List<Team> {
        val teamNames = mutableListOf<Team>()
        var currentName = ""
        repeat(count) {
            currentName = generateNextName(currentName)
            teamNames.add(Team("$TEAM_PREFIX$currentName"))
        }
        return teamNames
    }

    internal fun generateNextName(current: String): String {
        if (current.isEmpty()) return "A"
        val lastChar = current.last()
        return if (lastChar < 'Z') {
            // Increment the last character
            current.dropLast(1) + (lastChar + 1)
        } else {
            // Roll over and add an additional letter (e.g., Z -> AA)
            generateNextName(current.dropLast(1)) + "A"
        }
    }

}