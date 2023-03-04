package com.idd.dia.application.domain

@JvmInline
value class MemoContent(
    val content: String
) {
    init {
        require(content.isNotBlank())
    }
}
