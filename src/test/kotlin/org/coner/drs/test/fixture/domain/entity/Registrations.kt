package org.coner.drs.test.fixture.domain.entity

import org.coner.drs.domain.entity.Registration

fun registration(numbers: String): Registration {
    val tokens = numbers.split(" ")
    return when (tokens.size) {
        2 -> Registration(
                number = tokens[0],
                category = "",
                handicap = tokens[1]
        )
        3 -> Registration(
                number = tokens[0],
                category = tokens[1],
                handicap = tokens[2]
        )
        else -> throw IllegalArgumentException(
                "Numbers \"$numbers\" didn't have either two or three tokens, had ${tokens.size}"
        )
    }
}