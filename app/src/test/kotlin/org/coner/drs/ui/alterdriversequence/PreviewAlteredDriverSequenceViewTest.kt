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

package org.coner.drs.ui.alterdriversequence

import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import me.carltonwhitehead.tornadofx.junit5.Init
import me.carltonwhitehead.tornadofx.junit5.TornadoFxViewExtension
import me.carltonwhitehead.tornadofx.junit5.View
import org.assertj.core.api.Assertions.assertThat
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.test.fixture.domain.entity.RunEvents
import org.coner.drs.test.page.fast.FastPreviewAlteredDriverSequencePage
import org.coner.drs.test.page.real.RealPreviewAlteredDriverSequencePage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxRobot
import tornadofx.*

@ExtendWith(TornadoFxViewExtension::class, MockKExtension::class)
class PreviewAlteredDriverSequenceViewTest {

    @View
    private lateinit var view: PreviewAlteredDriverSequenceView
    private lateinit var model: PreviewAlteredDriverSequenceModel
    private lateinit var controller: PreviewAlteredDriverSequenceController

    private lateinit var realPage: RealPreviewAlteredDriverSequencePage
    private lateinit var fastPage: FastPreviewAlteredDriverSequencePage

    @Init
    fun init(scope: Scope) {
        view = find(scope)
        model = find(scope)
        controller = find(scope)
    }

    @BeforeEach
    fun beforeEach(robot: FxRobot) {
        realPage = RealPreviewAlteredDriverSequencePage(robot)
        fastPage = FastPreviewAlteredDriverSequencePage(robot)
    }


}
