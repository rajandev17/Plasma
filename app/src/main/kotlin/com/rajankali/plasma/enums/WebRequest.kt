package com.rajankali.plasma.enums

enum class WebRequest(val key: Int, val url : String, val title: String) {
    STACK_OVERFLOW(1, "https://stackoverflow.com/users/3159267/rajan-ks", "StackOverflow | Rajan Ks"),
    LINKED_IN(2, "file:///android_asset/rajan.html", "LinkedIn | Rajan Ks"),
    GITHUB(3, "https://github.com/rajandev17", "GitHub | Rajan Ks"),
    MEDIUM(4, "https://medium.com/@rajanks", "Medium | Rajan Ks");

    companion object{
        operator fun get(key: Int): WebRequest{
            return when(key){
                STACK_OVERFLOW.key -> STACK_OVERFLOW
                LINKED_IN.key -> LINKED_IN
                GITHUB.key -> GITHUB
                MEDIUM.key -> MEDIUM
                else -> STACK_OVERFLOW
            }
        }
    }


}