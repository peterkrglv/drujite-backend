package models

data class SessionModel(
    val id: Int,
    val name: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val imageUrl: String?,
    val qrLink: String? = null,
)
