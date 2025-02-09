package org.sportradar.domain.model

data class Reason (val message: String) {
    init {
        require(message.isNotBlank()) { "Reason message cannot be empty or blank" }
    }
}