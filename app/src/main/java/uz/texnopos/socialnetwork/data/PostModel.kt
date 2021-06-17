package uz.texnopos.socialnetwork.data

data class PostModel(
    var id: String = "",
    var userId: String = "",
    var theme: String = "",
    var username: String = "",
    var text: String = "",
    var likes: Int = 0,
    var dislikes: Int = 0,
    var comments: MutableList<Map<String, String>> = mutableListOf()
)