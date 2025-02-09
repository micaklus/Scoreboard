package org.sportradar.domain.model

data class Score(val value: Int) {
    init {
        require(value >= 0) { "Score cannot be negative" }
        require(value <= 2000) { "Too big score for a soccer match" }
    }

    operator fun plus(other: Score): Score {
        return Score(this.value + other.value)
    }
}

