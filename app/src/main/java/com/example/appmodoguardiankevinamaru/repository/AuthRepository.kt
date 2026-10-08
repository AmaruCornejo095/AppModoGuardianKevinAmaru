package com.example.appmodoguardiankevinamaru.repository

data class UserData(
    val email: String,
    val role: String,
    val name: String
)

class AuthRepository {
    companion object {
        private val testUsers = mutableMapOf(
            "am.cornejo@duocuc.cl" to Pair("123456", UserData("am.cornejo@duocuc.cl", "Admin", "Administrador")),
            "ke.fariasm@duocuc.cl" to Pair("123456", UserData("ke.fariasm@duocuc.cl", "Supervisor", "Supervisor")),
            "kevinfariasmena7@gmail.com" to Pair("123456", UserData("kevinfariasmena7@gmail.com", "Operador", "Operador"))
        )
    }

    fun login(email: String, pass: String): UserData? {
        val cleanEmail = email.lowercase().trim()
        val cleanPass = pass.trim()
        val userPair = testUsers[cleanEmail]
        return if (userPair != null && userPair.first == cleanPass) {
            userPair.second
        } else {
            null
        }
    }

    fun registerUser(email: String, pass: String, name: String, role: String = "Operador"): Boolean {
        val cleanEmail = email.lowercase().trim()
        if (testUsers.containsKey(cleanEmail)) return false
        testUsers[cleanEmail] = Pair(pass.trim(), UserData(cleanEmail, role, name))
        return true
    }
}