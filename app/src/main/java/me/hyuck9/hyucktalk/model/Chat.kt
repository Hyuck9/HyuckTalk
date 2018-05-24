package me.hyuck9.hyucktalk.model

data class Chat(
        var users: HashMap<String, Boolean> = HashMap(),
        var comments: HashMap<String, Comment> = HashMap()) {

    companion object {
        data class Comment(val uid:String? = null, val message: String? = null)
    }

}