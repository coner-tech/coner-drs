package org.coner.drs.node.payload

import assertk.Assert
import assertk.all
import assertk.assertions.hasSize
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import org.coner.drs.node.db.entity.RunDbEntity
import java.util.*

fun Assert<AlterDriverSequenceResult>.hasRunsCount(expected: Int) {
    prop(AlterDriverSequenceResult::runs).hasSize(expected)
}

fun Assert<AlterDriverSequenceResult>.runAt(index: Int): Assert<RunDbEntity> {
    return prop(AlterDriverSequenceResult::runs).index(index)
}

fun Assert<AlterDriverSequenceResult>.insertedRunIdMatches(expected: RunDbEntity) {
    prop(AlterDriverSequenceResult::insertedRunId).isEqualTo(expected.id)
}

fun Assert<AlterDriverSequenceResult>.shiftedRunIds(body: Assert<Set<UUID>>.() -> Unit) {
    prop(AlterDriverSequenceResult::shiftedRunIds).all(body)
}