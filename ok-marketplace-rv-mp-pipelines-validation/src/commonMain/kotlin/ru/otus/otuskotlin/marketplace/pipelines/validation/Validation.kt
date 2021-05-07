package ru.otus.otuskotlin.marketplace.pipelines.validation

import ru.otus.otuskotlin.marketplace.pipelines.kmp.Pipeline

fun <C> Pipeline.Builder<C>.validation(block: ValidationBuilder<C>.() -> Unit) {
    execute(ValidationBuilder<C>().apply(block).build())
}
