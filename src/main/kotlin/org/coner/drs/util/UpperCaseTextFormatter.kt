package org.coner.drs.util

import javafx.scene.control.TextFormatter

class UpperCaseTextFormatter : TextFormatter<String>({ change ->
    change.text = change.text?.toUpperCase()
    change
})