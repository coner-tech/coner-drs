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
