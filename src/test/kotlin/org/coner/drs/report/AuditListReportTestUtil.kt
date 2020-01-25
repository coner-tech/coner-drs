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

package org.coner.drs.report

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object AuditListReportTestUtil {

    fun assert(actual: String) = assertThat(Jsoup.parse(actual)).all {
        hasTitleElementText("THSCC Points Autocross #9 @ Danville Regional Airport - 2019-10-27 - Audit List")
        onRunTableRowNumber(1, "first clean run") {
            sequenceIsEqualTo("1")
            numbersIsEqualTo("58 FS")
            timeIsEqualTo("64.645")
            penaltyIsBlank()
        }
        onRunTableRowNumber(2, "first coned run") {
            sequenceIsEqualTo("2")
            numbersIsEqualTo("15 SSC")
            timeIsEqualTo("59.887")
            penaltyIsOnly("+2")
        }
        onRunTableRowNumber(3, "first dnf run") {
            sequenceIsEqualTo("3")
            numbersIsEqualTo("52 GS")
            timeIsEqualTo("54.645")
            penaltyIsOnly("DNF")
        }
        onRunTableRowNumber(94, "first rerun") {
            sequenceIsEqualTo("94")
            numbersIsEqualTo("40 GS")
            timeIsEqualTo("55.349")
            penaltyIsOnly("RRN")
        }
        onRunTableRowNumber(60, "first run with multiple penalty types") {
            sequenceIsEqualTo("60")
            numbersIsEqualTo("58 NOV GS")
            timeIsEqualTo("60.152")
            hasPenalty(1, "DNF")
            hasPenalty(2, "+1")
        }
        onRunTableRowNumber(165, "first run with time with trailing zeroes") {
            sequenceIsEqualTo("165")
            numbersIsEqualTo("11 MAC CAM-C")
            timeIsEqualTo("56.800")
            penaltyIsBlank()
        }
    }
}



private fun Assert<Document>.hasTitleElementText(titleText: String) {
    transform("title element") { it.select("html head title").text() }.isEqualTo(titleText)
}

private fun Assert<Document>.onRunTableRowNumber(n: Int, name: String? = null, body: Assert<Elements>.() -> Unit) {
    transform(name ?: this.name) { it.select("table tbody tr:nth-child($n)") }.all(body)
}

private fun Assert<Elements>.sequenceIsEqualTo(expected: String) {
    transform("sequence of $name") { it.select("td.sequence").text() }.isEqualTo(expected)
}

private fun Assert<Elements>.numbersIsEqualTo(expected: String) {
    transform("numbers of $name") { it.select("td.numbers").text() }.isEqualTo(expected)
}

private fun Assert<Elements>.timeIsEqualTo(expected: String) {
    transform("time of $name") { it.select("td.time").text() }.isEqualTo(expected)
}

private fun Assert<Elements>.penaltyIsBlank() {
    transform("penalty of $name") { it.select("td.penalty").hasText() }.isFalse()
}

private fun Assert<Elements>.penaltyIsOnly(expected: String) {
    transform("penalty of $name") { it.select("td.penalty span:only-child").text() }.isEqualTo(expected)
}

private fun Assert<Elements>.hasPenalty(n: Int, expected: String) {
    transform("nth-penalty($n) of $name") { it.select("td.penalty span:nth-child($n)").text() }.isEqualTo(expected)
}

