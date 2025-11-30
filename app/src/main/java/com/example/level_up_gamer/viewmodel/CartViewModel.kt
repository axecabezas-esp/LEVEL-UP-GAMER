package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.level_up_gamer.data.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.Locale

data class CartUiState(
    val productosEnCarrito: List<Producto> = emptyList(),
    val total: String = "$0"
)

open class CartViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState

    fun agregarProducto(producto: Producto) {
        _uiState.update { currentState ->
            val nuevaLista = currentState.productosEnCarrito + producto
            currentState.copy(productosEnCarrito = nuevaLista)
        }
        calcularTotal()
    }

    fun removerProducto(producto: Producto) {
        _uiState.update { currentState ->
            val nuevaLista = currentState.productosEnCarrito.toMutableList()
            nuevaLista.remove(producto)
            currentState.copy(productosEnCarrito = nuevaLista)
        }
        calcularTotal()
    }

    fun vaciarCarrito() {
        _uiState.update { it.copy(productosEnCarrito = emptyList()) }
        calcularTotal()
    }

    private fun calcularTotal() {
        val totalNumerico = _uiState.value.productosEnCarrito.sumOf { producto ->
            limpiarPrecio(producto.precio)
        }

        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        val totalFormateado = formatoMoneda.format(totalNumerico)

        _uiState.update { it.copy(total = totalFormateado) }
    }

    private fun limpiarPrecio(precioStr: String): Int {
        if (precioStr.equals("Gratis", ignoreCase = true)) return 0
        val soloNumeros = precioStr.replace("$", "").replace(".", "").trim()
        return soloNumeros.toIntOrNull() ?: 0
    }
}