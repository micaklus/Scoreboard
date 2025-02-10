/**
 * @file Reason.kt
 * This file contains the implementation of the `Reason` class, part of the `org.sportradar.domain.model` package.
 *
 * The `Reason` class represents an explanatory or descriptive message, such as why a match was canceled or postponed.
 */

package org.sportradar.domain.model

/**
 * Encapsulates a reason or message related to a sports match.
 *
 * @property message A textual description of the reason. Must not be empty or blank.
 * @throws IllegalArgumentException If the message is empty or blank.
 *
 * Example:
 * ```
 * val cancellationReason = Reason("Match postponed due to inclement weather.")
 * println(cancellationReason.message) // Output: Match postponed due to inclement weather.
 * ```
 */
data class Reason(val message: String) {
    init {
        require(message.isNotBlank()) { "Reason message cannot be empty or blank" }
        require(message.length <= 200)  { "Reason message to big. Max 200 chars allowed" }
    }
}