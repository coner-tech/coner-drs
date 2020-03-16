package org.coner.drs.ui.home

import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.io.File
import tornadofx.getValue
import tornadofx.setValue

class HomeModel : ViewModel() {


    val katanaScopeProperty = SimpleObjectProperty<HomeKatanaScope>(this, "katanaScope")
    var katanaScope by katanaScopeProperty

    val pathToDigitalRawSheetsDatabaseProperty = SimpleObjectProperty<File>(this, "pathToDigitalRawSheetsDatabase")
    var pathToDigitalRawSheetsDatabase by pathToDigitalRawSheetsDatabaseProperty

    val pathToCrispyFishDatabaseProperty = SimpleObjectProperty<File>(this, "pathToCrispyFishDatabase")
    var pathToCrispyFishDatabase by pathToCrispyFishDatabaseProperty

}