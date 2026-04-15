package ru.drujite.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddImageRequest(
    val entityType: String,
    val id: Int,
)
