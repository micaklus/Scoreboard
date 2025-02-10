/**
 * @file Team.kt
 * This file contains the implementation of the `Team` class, which is a part of the `org.sportradar.domain.model` package.
 *
 * The `Team` class represents a sport team with validation for name constraints.
 * It ensures that the team name is not blank and conforms to specified naming rules, such as allowing only letters,
 * spaces, and a length limit of 1 to 50 characters.
 */

package org.sportradar.domain.model
/**
 * Represents a team in a sport.
 *
 * @property name The name of the team. It must not be empty or blank and can only include letters, spaces, and must be between 1 to 50 characters.
 * @throws IllegalArgumentException If the name doesn't meet the validation requirements.
 *
 * Example:
 * ```
 * val team = Team("Manchester United")
 * ```
 */

data class Team(val name: String){
    init {
        require(name.isNotBlank()) { "Team name cannot be empty or blank. Please provide a valid name for the team." }
        require(name.matches(validNameRegex)) { "Invalid team name. Only letters and spaces are allowed, with a maximum length of 50 characters." }
    }
    private companion object {
        // Precompiled regex: Unicode letters, spaces, and 1-50 chars
        private val validNameRegex = Regex("^[\\p{L} ]{1,50}\$")
    }

}