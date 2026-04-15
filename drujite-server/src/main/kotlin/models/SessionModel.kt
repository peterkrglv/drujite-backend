package models

data class SessionModel(
    val description: String,
    val endDate: String,
    val id: Int,
    val imageUrl: String?,
    val name: String,
    val startDate: String,
    val qrLink: String? = null,
)
