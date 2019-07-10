package org.coner.drs.domain.service

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import assertk.assertions.prop
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.payload.RegistrationSelectionCandidate
import org.coner.drs.test.TornadoFxScopeExtension
import org.coner.drs.test.fixture.domain.entity.registration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import tornadofx.*
import java.io.File

@ExtendWith(TornadoFxScopeExtension::class)
class RegistrationServiceTest {

    private lateinit var service: RegistrationService

    @BeforeEach
    fun before(scope: Scope) {
        service = find(scope)
    }

    enum class NumbersFieldTokensParam(
            val input: String?,
            val expected: List<String>
    ) {
        EMPTY("", listOf("")),
        NULL(null, emptyList()),
        ONE_TOKEN("8", listOf("8")),
        TWO_TOKENS("8 STR", listOf("8", "STR")),
        THREE_TOKENS("1 HS NOV", listOf("1", "HS", "NOV")),
        CONCATENATION("1HSNOV", listOf("1HSNOV"))
    }
    @ParameterizedTest
    @EnumSource(NumbersFieldTokensParam::class)
    fun `numbers field tokens`(param: NumbersFieldTokensParam) {
        val actual = service.findNumbersFieldTokens(param.input)

        assertThat(actual).isEqualTo(param.expected)
    }


    enum class NumbersFieldContainsNumbersTokensParam(
            val input: List<String>,
            val expected: Boolean
    ) {
        EMPTY_LIST(emptyList(), false),
        ONE_TOKEN_VALID(listOf("2"), false),
        TWO_TOKENS_VALID(listOf("8", "STR"), true),
        THREE_TOKENS_VALID(listOf("1", "NOV", "HS"), true),
        ONE_TOKEN_INVALID(listOf("HS"), false),
        TWO_TOKENS_INVALID(listOf("STR", "8"), false),
        THREE_TOKENS_INVALID(listOf("NOV", "HS", "1"), false)
    }

    @ParameterizedTest
    @EnumSource(NumbersFieldContainsNumbersTokensParam::class)
    fun `numbers field should contain tokens`(param: NumbersFieldContainsNumbersTokensParam) {
        val actual = service.findNumbersFieldContainsNumbersTokens(param.input)

        assertThat(actual).isEqualTo(param.expected)
    }

    enum class NumbersFieldArbitraryRegistrationParam(
            val input: List<String>?,
            val expected: Registration?
    ) {
        NULL(null, null),
        EMPTY(emptyList(), null),
        ONE_TOKEN(listOf("8"), null),
        TWO_TOKENS(listOf("8", "STR"), Registration(number = "8", category = "", handicap = "STR")),
        THREE_TOKENS(listOf("1", "NOV", "HS"), Registration(number = "1", category = "NOV", handicap = "HS")),
        FOUR_TOKENS(listOf("1", "NOV", "HS", "DERP"), null)
    }

    @ParameterizedTest
    @EnumSource(NumbersFieldArbitraryRegistrationParam::class)
    fun `numbers field arbitrary registration`(param: NumbersFieldArbitraryRegistrationParam) {
        val actual = service.findNumbersFieldArbitraryRegistration(param.input)

        if (param.expected != null) {
            assertThat(actual).all {
                prop(Registration::number).isEqualTo(param.expected.number)
                prop(Registration::category).isEqualTo(param.expected.category)
                prop(Registration::handicap).isEqualTo(param.expected.handicap)
            }
        } else {
            assertThat(actual).isNull()
        }
    }

    enum class AutoSelectionCandidateParam(
            val inRegistrations: List<Registration>,
            val inNumbersField: String,
            val expected: RegistrationSelectionCandidate?
    ) {
        THSCC_2019_POINTS_5_4CS(
                inRegistrations = listOf(
                        registration("14 CS"),
                        registration("14 NOV CS"),
                        registration("4 CS"),
                        registration("44 CSP")
                ),
                inNumbersField = "4CS",
                expected = RegistrationSelectionCandidate(
                        registration = registration("4 CS"),
                        levenshteinDistanceToNumbersField = 1
                )
        ),
        THSCC_2019_POINTS_5_8S(
                inRegistrations = listOf(
                        registration("18 NOV ES"),
                        registration("18 SM"),
                        registration("48 SM"),
                        registration("8 STR")
                        // omitting many for brevity
                ),
                inNumbersField = "8S",
                expected = null
        ),
        THSCC_2019_POINTS_5_8ST(
                inRegistrations = listOf(
                        registration("8 NOV STX"),
                        registration("8 STR"),
                        registration("87 NOV STH")
                ),
                inNumbersField = "8ST",
                expected = RegistrationSelectionCandidate(
                        registration = registration("8 STR"),
                        levenshteinDistanceToNumbersField = 2
                )
        ),
        THSCC_2019_POINTS_5_24STU(
                inRegistrations = listOf(
                        registration("24 STU"),
                        registration("42 NOV STU")
                ),
                inNumbersField = "24STU",
                expected = RegistrationSelectionCandidate(
                        registration = registration("24 STU"),
                        levenshteinDistanceToNumbersField = 1
                )
        ),
        THSCC_2019_POINTS_5_44STS(
                inRegistrations = listOf(
                        registration("140 NOV STR"),
                        registration("24 STU"),
                        registration("4 STS"),
                        registration("40 STR"),
                        registration("417 NOV STH"),
                        registration("42 NOV STU"),
                        registration("44 STS"),
                        registration("45 STH"),
                        registration("64 STR")
                ),
                inNumbersField = "44STS",
                expected = RegistrationSelectionCandidate(
                        registration = registration("44 STS"),
                        levenshteinDistanceToNumbersField = 1
                )
        )
    }

    @ParameterizedTest
    @EnumSource(AutoSelectionCandidateParam::class)
    fun `it should find auto selection candidate`(param: AutoSelectionCandidateParam) {
        val actual = service.findAutoSelectionCandidate(param.inRegistrations, param.inNumbersField)

        if (param.expected != null) {
            assertThat(actual).all {
                prop(RegistrationSelectionCandidate::registration).all {
                    prop(Registration::number).isEqualTo(param.expected.registration.number)
                    prop(Registration::category).isEqualTo(param.expected.registration.category)
                    prop(Registration::handicap).isEqualTo(param.expected.registration.handicap)
                }
                prop(RegistrationSelectionCandidate::levenshteinDistanceToNumbersField)
                        .isEqualTo(param.expected.levenshteinDistanceToNumbersField)
            }
        } else {
            assertThat(actual).isNull()
        }
    }
}