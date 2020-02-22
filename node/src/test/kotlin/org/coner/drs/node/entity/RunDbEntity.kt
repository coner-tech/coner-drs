package org.coner.drs.node.entity

import assertk.Assert
import assertk.all
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import org.coner.drs.node.db.entity.RunDbEntity
import java.math.BigDecimal
import java.util.*

fun Assert<RunDbEntity>.idIsEqualTo(other: RunDbEntity) {
    idIsEqualTo(other.id)
}

fun Assert<RunDbEntity>.idIsEqualTo(expected: UUID) {
    prop(RunDbEntity::id).isEqualTo(expected)
}

fun Assert<RunDbEntity>.sequenceIsEqualTo(other: RunDbEntity) {
    hasSequence(other.sequence)
}

fun Assert<RunDbEntity>.hasSequence(sequence: Int) {
    prop(RunDbEntity::sequence).isEqualTo(sequence)
}

fun Assert<RunDbEntity>.registrationIsEqualTo(other: RunDbEntity) {
    hasRegistration(other.category, other.handicap, other.number)
}

fun Assert<RunDbEntity>.hasRegistration(expected: Array<String>) {
    require(expected.size == 3) { "Must be a goofy 3-element Array" }
    hasRegistration(
            category = expected[0],
            handicap = expected[1],
            number = expected[2]
    )
}

fun Assert<RunDbEntity>.hasRegistration(category: String, handicap: String, number: String) {
    all {
        prop(RunDbEntity::category).isEqualTo(category)
        prop(RunDbEntity::handicap).isEqualTo(handicap)
        prop(RunDbEntity::number).isEqualTo(number)
    }
}

fun Assert<RunDbEntity>.registrationIsEmpty() {
    all {
        prop(RunDbEntity::category).isEmpty()
        prop(RunDbEntity::handicap).isEmpty()
        prop(RunDbEntity::number).isEmpty()
    }
}

fun Assert<RunDbEntity>.rawTimeIsEqualTo(other: RunDbEntity) {
    hasRawTime(other.rawTime)
}

fun Assert<RunDbEntity>.hasRawTime(expected: BigDecimal?) {
    prop(RunDbEntity::rawTime).isEqualTo(expected)
}

