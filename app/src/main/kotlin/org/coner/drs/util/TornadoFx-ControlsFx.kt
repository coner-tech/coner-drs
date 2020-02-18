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

package org.coner.drs.util

import impl.org.controlsfx.autocompletion.SuggestionProvider
import javafx.scene.control.TextField
import javafx.util.StringConverter
import org.controlsfx.control.textfield.AutoCompletionBinding
import org.controlsfx.control.textfield.TextFields

private const val deprecationMessage = "Replace upon release of TornadoFX-ControlsFX 0.1.2"

@Deprecated(message = deprecationMessage)
fun <T> TextField.bindAutoCompletion(vararg suggestions: T, op: (AutoCompletionBinding<T>.() -> Unit)? = null) {
    val binding = TextFields.bindAutoCompletion(this, *suggestions)
    if (op != null) binding.apply(op)
}

@Deprecated(message = deprecationMessage)
fun <T> TextField.bindAutoCompletion(suggestionsList: List<T>, op: (AutoCompletionBinding<T>.() -> Unit)? = null) {
    val binding = TextFields.bindAutoCompletion(this, suggestionsList)
    if (op != null) binding.apply(op)
}

@Deprecated(message = deprecationMessage)
fun <T> TextField.bindAutoCompletion(suggestionsProvider: () -> Collection<T>, op: (AutoCompletionBinding<T>.() -> Unit)? = null) {
    val binding = TextFields.bindAutoCompletion(this) { suggestionsProvider() }
    if (op != null) binding.apply(op)
}

@Deprecated(message = deprecationMessage)
fun <T> TextField.bindAutoCompletion(suggestionsProvider: () -> Collection<T>, converter: StringConverter<T>, op: (AutoCompletionBinding<T>.() -> Unit)? = null) {
    val binding = TextFields.bindAutoCompletion(this, { suggestionsProvider() }, converter)
    if (op != null) binding.apply(op)
}

@Deprecated(message = deprecationMessage)
fun <T> TextField.bindAutoCompletion(suggestionProvider: SuggestionProvider<T>, op: (AutoCompletionBinding<T>.() -> Unit)? = null) {
    val binding = TextFields.bindAutoCompletion(this, suggestionProvider)
    if (op != null) binding.apply(op)
}

@Deprecated(message = deprecationMessage)
fun <T> AutoCompletionBinding<T>.onAutoCompleted(op: (AutoCompletionBinding.AutoCompletionEvent<T>) -> Unit) = setOnAutoCompleted { op(it) }
