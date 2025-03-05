package br.com.acgj.shotit.core.infra.mailer

@DslMarker
annotation class EmailDsl

@EmailDsl
class EmailBuilder {
    var title: String = ""
    private val bodyContent = StringBuilder()
    private val ctaContent = StringBuilder()

    fun title(value: String) {
        title = value
    }

    fun callToAction(init: CallToActionBuilder.() -> Unit){
        val cta = CallToActionBuilder().apply(init)
        ctaContent.append(cta.build())
    }

    fun body(init: BodyBuilder.() -> Unit) {
        val bodyBuilder = BodyBuilder().apply(init)
        bodyContent.append(bodyBuilder.build())
    }

    fun build(): String {
        return """
            <html>
                <head><title>$title</title></head>
                <body>
                    $bodyContent
                    <hr/>
                    $ctaContent
                </body>
            </html>
        """.trimIndent()
    }
}

@EmailDsl
class CallToActionBuilder {
    private val content = StringBuilder()

    fun message(text: String){
        content.append("<p style=\"margin-bottom:1rem\"> $text </p>")
    }

    fun link(href: String, text: String){
        content.append("""
            <a
                style="display:block; background-color: purple; padding: 1rem; max-width:300px; margin: 2rem 0; text-align: center; font-weight: bold; border-radius: 10px;"
                href="$href"
            >
                $text
            </a>
        """.trimIndent())
    }

    fun build(): String = content.toString()
}

@EmailDsl
class BodyBuilder {
    private val content = StringBuilder()

    fun p(text: String) {
        content.append("<p>$text</p>")
    }

    fun strong(text: String) {
        content.append("<strong>$text</strong>")
    }

    fun build(): String = content.toString()
}

fun email(init: EmailBuilder.() -> Unit): String {
    return EmailBuilder().apply(init).build()
}