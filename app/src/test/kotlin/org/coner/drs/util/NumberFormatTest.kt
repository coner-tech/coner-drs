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

package org.coner.drs.util

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class NumberFormatTest {


    @BeforeEach
    fun before() {
    }

    @Test
    fun runTimes() {
        val runTimeFormat = NumberFormat.forRunTimes()
        val data = listOf(
                0 to "0.000",
                BigDecimal.valueOf(0, 3) to "0.000",
                34.567 to "34.567",
                BigDecimal.valueOf(34567, 3) to "34.567",
                45.678 to "45.678",
                BigDecimal.valueOf(45678, 3) to "45.678",
                56.789 to "56.789",
                BigDecimal.valueOf(56789, 3) to "56.789",
                67.890 to "67.890",
                BigDecimal.valueOf(67890, 3) to "67.890",
                1000 to "1000.000"

        )
        SoftAssertions.assertSoftly { softly ->
            data.forEach { softly.assertThat(runTimeFormat.format(it.first)).isEqualTo(it.second) }
            softly.assertAll()
        }
    }
}