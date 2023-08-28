package com.example.exchangecurrency.utils

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets

internal fun MockWebServer.enqueueResponse(
    fileName: String,
    mockResponse: (String) -> MockResponse
) {
    val inputStream = javaClass.classLoader?.getResourceAsStream("api-response/$fileName")

    val source = inputStream?.let { inputStream.source().buffer() }
    source?.let {
        enqueue(
            mockResponse.invoke(source.readString(StandardCharsets.UTF_8))
        )
    }
}
