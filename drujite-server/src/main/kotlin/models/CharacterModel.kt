package models

data class CharacterModel(
    val clanId: Int,
    val id: Int,
    val name: String,
    val story: String,
    val imageUrl: String? = null,
)
