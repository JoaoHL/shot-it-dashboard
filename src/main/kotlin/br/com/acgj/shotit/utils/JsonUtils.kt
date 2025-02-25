package br.com.acgj.shotit.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun <T> parse(json: String, target: Class<T>): T {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(json, target)
}