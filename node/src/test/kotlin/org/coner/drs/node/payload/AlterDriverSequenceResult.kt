package org.coner.drs.node.payload

import assertk.Assert
import assertk.all
import assertk.assertions.*
import org.coner.drs.node.db.entity.RunDbEntity
import java.util.*

fun Assert<AlterDriverSequenceResult>.hasRunsCount(expected: Int) {
    prop(AlterDriverSequenceResult::runs).hasSize(expected)
}

fun Assert<AlterDriverSequenceResult>.runAt(index: Int): Assert<RunDbEntity> {
    return prop(AlterDriverSequenceResult::runs).index(index)
}

fun Assert<AlterDriverSequenceResult>.insertedRunIdMatchesRunAtIndex(index: Int) {
    given { result -> runAt(index).transform { it.id }.isEqualTo(result.insertedRunId) }
}

fun Assert<AlterDriverSequenceResult>.shiftedRunIds(body: Assert<Set<UUID>>.() -> Unit) {
    prop(AlterDriverSequenceResult::shiftedRunIds).all(body)
}

fun Assert<AlterDriverSequenceResult>.shiftedRunIdsContainsRunIdAtIndex(index: Int) {
    given { result -> runAt(index).given { prop(AlterDriverSequenceResult::shiftedRunIds).contains(it.id) } }
}