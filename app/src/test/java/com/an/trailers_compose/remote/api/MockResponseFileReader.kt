package com.an.trailers_compose.remote.api

import java.io.InputStreamReader

class MockResponseFileReader(path: String) {

    val content: String

    init {
        val reader = InputStreamReader(
            this.javaClass.classLoader?.
            getResourceAsStream(path)
        )
        content = reader.readText()
        reader.close()
    }
}