package remote.dto

data class PostDto(
    val userId: Int,
    val id: String? = null,
    val title: String,
    val body: String,
    val imageName: String? = null,
    val imageUrl: String? = null
)