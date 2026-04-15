package ru.drujite.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddSessionByQRRequest(
    val qr: String,
)
