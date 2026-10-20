package com.example.appmodoguardiankevinamaru.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appmodoguardiankevinamaru.data.EstadoDataStore
import com.example.appmodoguardiankevinamaru.data.UsuarioDao
import com.example.appmodoguardiankevinamaru.model.UsuarioEntity
import com.example.appmodoguardiankevinamaru.repository.AuthRepository
import com.example.appmodoguardiankevinamaru.repository.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UsuarioUiState(
    val listaUsuarios: List<UsuarioEntity> = emptyList(),
    val modoEspecial: Boolean = false,
    val usuarioAutenticado: UserData? = null
)

class UsuarioViewModel(
    private val usuarioDao: UsuarioDao,
    private val estadoDataStore: EstadoDataStore,
    val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            usuarioDao.obtenerTodos().collect { usuarios ->
                _uiState.update { it.copy(listaUsuarios = usuarios) }
            }
        }
        viewModelScope.launch {
            estadoDataStore.modoEspecialFlow.collect { modo ->
                _uiState.update { it.copy(modoEspecial = modo) }
            }
        }
    }

    fun guardarUsuario(usuario: UsuarioEntity) {
        viewModelScope.launch {
            usuarioDao.insertar(usuario)
        }
    }

    fun eliminarUsuario(usuario: UsuarioEntity) {
        viewModelScope.launch {
            usuarioDao.eliminar(usuario)
        }
    }

    fun toggleModoEspecial(activo: Boolean) {
        viewModelScope.launch {
            estadoDataStore.guardarModoEspecial(activo)
        }
    }
}

class UsuarioViewModelFactory(
    private val usuarioDao: UsuarioDao,
    private val estadoDataStore: EstadoDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsuarioViewModel(usuarioDao, estadoDataStore) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}