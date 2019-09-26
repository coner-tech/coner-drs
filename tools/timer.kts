#!/usr/bin/env kscript

import java.io.File
import java.io.FileNotFoundException
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

val outputFile: File = File(args[0]).apply {
    if (!exists()) throw FileNotFoundException(args[0])
}
println("Output: ${outputFile.absolutePath}")

val digitsFormat = NumberFormat.getIntegerInstance(Locale.US).apply {
    minimumIntegerDigits = 3
    maximumIntegerDigits = 3
}

while(true) {
    val seconds = (40 until 50).random()
    val milliseconds = (0..999).random()
    val formattedSeconds = digitsFormat.format(seconds)
    val formattedMilliseconds = digitsFormat.format(milliseconds)
    val encoded = " ${formattedMilliseconds.reversed()}${formattedSeconds.reversed()}"
    println("${formattedSeconds}.${formattedMilliseconds} == $encoded")
    outputFile.appendText("$encoded\n")
    Thread.sleep(TimeUnit.SECONDS.toMillis(20))
}