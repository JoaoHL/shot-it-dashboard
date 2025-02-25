package br.com.acgj.shotit.utils

val WHITE_SPACE_PATTERN: Regex = Regex("\\s+")

fun normalizeFilename(filename: String) = filename.replace(WHITE_SPACE_PATTERN, "_")
