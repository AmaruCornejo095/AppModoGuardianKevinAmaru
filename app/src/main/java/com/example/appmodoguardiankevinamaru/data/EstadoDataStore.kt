package com.example.appmodoguardiankevinamaru.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")

class EstadoDataStore(private val context: Context) {

    private val MODO_ESPECIAL_KEY = booleanPreferencesKey(name = "modo_especial")

    val modoEspecialFlow: Flow<Boolean> = context.dataStore.data.map { preferencias ->
        preferencias[MODO_ESPECIAL_KEY] ?: false
    }

    suspend fun guardarModoEspecial(valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[MODO_ESPECIAL_KEY] = valor
        }
    }

    suspend fun guardarEstado(valor: Boolean) {
        guardarModoEspecial(valor)
    }

    fun obtenerEstado(): Flow<Boolean?> {
        return context.dataStore.data.map { preferencias ->
            preferencias[MODO_ESPECIAL_KEY]
        }
    }
}