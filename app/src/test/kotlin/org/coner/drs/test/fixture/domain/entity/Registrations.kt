/*
 * Coner Digital Raw Sheets - reduce the drag of working autocross raw sheets
 * Copyright (C) 2018-2020 Carlton Whitehead
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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