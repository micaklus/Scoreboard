package org.sportradar.domain.model

data class Team(val name: String){
    init {
        require(name.isNotBlank()) { "Team name cannot be empty or blank. Please provide a valid name for the team." }
        require(name.matches(validNameRegex)) { "Invalid team name: $name. Only letters and spaces are allowed." }
    }
    companion object {
        // Precompiled regex: Unicode letters, spaces, and 1-50 chars
        private val validNameRegex = Regex("^[\\p{L} ]{1,50}\$")
    }

}