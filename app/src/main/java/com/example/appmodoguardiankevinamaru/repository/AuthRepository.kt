package com.example.appmodoguardiankevinamaru.repository

data class UserData(
    val email: String,
    val role: String,
    val name: String
)

class AuthRepository {
    private val testUsers = mapOf(
        "am.cornejo@duocuc.cl" to Pair("123456", UserData("am.cornejo@duocuc.cl", "Admin", "Administrador")),
        "ke.fariasm@duocuc.cl" to Pair("123456", UserData("ke.fariasm@duocuc.cl", "Supervisor", "Supervisor")),
        "kevinfariasmena7@gmail.com" to Pair("123456", UserData("kevinfariasmena7@gmail.com", "Operador", "Operador"))
    )

    fun login(email: String, pass: String): UserData? {
        val userPair = testUsers[email.lowercase().trim()]
        return if (userPair != null && userPair.first == pass) {
            userPair.second
        } else {
            null
        }
    }
}