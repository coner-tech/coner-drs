package org.coner.drs.util

import org.assertj.core.api.SoftAssertions
import org.coner.drs.di.KatanaModules
import org.coner.drs.di.NumberFormats
import org.junit.Before
import org.junit.Test
import org.rewedigital.katana.Component
import java.math.BigDecimal
import java.text.NumberFormat

class NumberFormatsTest {

    private lateinit var component: Component

    @Before
    fun before() {
        component = Component(KatanaModules.numberFormats)
    }

    @Test
    fun runTimes() {
        val runTimeFormat: NumberFormat = component.injectNow(NumberFormats.RUN_TIME)
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