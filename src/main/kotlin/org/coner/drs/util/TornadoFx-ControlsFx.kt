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
