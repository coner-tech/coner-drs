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

package org.coner.drs.test.page.fast

import org.coner.drs.test.page.PreviewAlteredDriverSequencePage
import org.coner.drs.test.page.SpecifyDriverSequenceAlterationPage
import org.coner.drs.test.page.real.RealAlterDriverSequencePage
import org.testfx.api.FxRobot

class FastAlterDriverSequencePage(robot: FxRobot) : RealAlterDriverSequencePage(robot) {

    override fun clickOkButton() {
        okButton().fire()
    }

    override fun clickCancelButton() {
        cancelButton().fire()
    }

    override fun toSpecifyDriverSequenceAlterationPage(): SpecifyDriverSequenceAlterationPage {
        return FastSpecifyDriverSequenceAlterationPage(robot)
    }

    override fun toPreviewAlteredDriverSequencePage(): PreviewAlteredDriverSequencePage {
        return FastPreviewAlteredDriverSequencePage(robot)
    }
}