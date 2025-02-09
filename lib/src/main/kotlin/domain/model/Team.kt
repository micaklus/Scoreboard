package org.sportradar.domain.model

data class Team(val name: String){
    init {
        require(name.isNotBlank()) { "Team name cannot be empty or blank. Please provide a valid name for the team." }
        require(name.matches(Regex("^[a-zA-Z ]+\$"))) { "Invalid team name: $name. Only letters and spaces are allowed." }
    }
}