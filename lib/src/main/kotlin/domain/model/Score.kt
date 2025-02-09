package org.sportradar.domain.model

data class Score(val value: Int) {
    init {
        require(value >= 0) { "Score cannot be negative" }
        require(value <= 200) { "To big score for a soccer match" }
    }
}

