package ru.drujite.util

import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

private val log = LoggerFactory.getLogger("GetRedirectUrl")

fun getRedirectedUrl(url: String): String? {
    try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.instanceFollowRedirects = false
        connection.connect()
        val redirectedUrl = connection.getHeaderField("Location")
        connection.disconnect()
        return redirectedUrl
    } catch (e: IOException) {
        log.warn("Failed to resolve redirect for {}: {}", url, e.message, e)
        return url
    }
}
