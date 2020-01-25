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

package org.coner.style

import tornadofx.*


object ConerLogoPalette {
    val ORANGE = c("#F15A24")
    val ORANGE_SHADOW = c("#D33C0D")
    val DARK_GRAY = c("#808080")

    object Triad {

        /**
         * Coner Logo Orange
         */
        val PRIMARY = ConerLogoPalette.ORANGE
        /**
         * Light Orange
         */
        val PRIMARY_LIGHTER_1 = c("#FF7F4F")
        /**
         * Very Light Orange
         */
        val PRIMARY_LIGHTER_2 = c("#FF9C78")
        /**
         * Strong Orange
         */
        val PRIMARY_DARKER_1 = c("#C73D0A")
        /**
         * Dark Orange (Brown Tone)
         */
        val PRIMARY_DARKER_2 = c("#9E2B00")

        /**
         * Dark Blue
         */
        val SECONDARY_1 = c("#1F679B")
        /**
         * Dark Moderate Blue
         */
        val SECONDARY_1_LIGHTER_1 = c("#3A78A4")
        /**
         * Moderate Blue
         */
        val SECONDARY_1_LIGHTER_2 = c("#5F97BE")
        /**
         * Dark Blue (darker than `SECONDARY_1`)
         */
        val SECONDARY_1_DARKER_1 = c("#0E5080")
        /**
         * Very Dark Blue
         */
        val SECONDARY_1_DARKER_2 = c("#063E65")

        /**
         * Strong Green
         */
        val SECONDARY_2 = c("#77D720")
        /**
         * Soft green
         */
        val SECONDARY_2_LIGHTER_1 = c("#91E447")
        /**
         * Soft green (lighter than `LIGHTER_1`)
         */
        val SECONDARY_2_LIGHTER_2 = c("#AAEC6F")
        /**
         * Dark green
         */
        val SECONDARY_2_DARKER_1 = c("#58B209")
        /**
         * Dark green (darker than `DARKER_1`)
         */
        val SECONDARY_2_DARKER_2 = c("#428D00")

        /**
         * Dark Cyan - Lime Green
         */
        val COMPLEMENT = c("#19A666")
        /**
         * Dark moderate cyan - lime green
         */
        val COMPLEMENT_LIGHTER_1 = c("#37B079")
        /**
         * Moderate cyan - lime green
         */
        val COMPLEMENT_LIGHTER_2 = c("#5DC797")
        /**
         * Dark cyan - lime green
         */
        val COMPLEMENT_DARKER_1 = c("#07894E")
        /**
         * Very dark cyan - lime green
         */
        val COMPLEMENT_DARKER_2 = c("#006D3B")
    }
}


object ConerExtendedPalette {
    val TRAFFIC_CONE_ORANGE = c("#FF7900")
    val SAFETY_ORANGE = c("#FF6700")
}