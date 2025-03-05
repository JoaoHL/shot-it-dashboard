package br.com.acgj.shotit.core.infra.mailer

interface EmailTemplate {
    val subject: String
    fun body(): String
}
