package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.level_up_gamer.data.model.CategoriaProducto
import com.example.level_up_gamer.data.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.data.model.GameDto
import com.example.level_up_gamer.data.remote.GameRetrofitInstance
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

data class CatalogoUiState(
    val categorias: List<CategoriaProducto> = emptyList()
)

open class CatalogoViewModel : ViewModel() {

    protected val _uiState = MutableStateFlow(CatalogoUiState())
    val uiState: StateFlow<CatalogoUiState> = _uiState
    protected var productosLocales = mutableListOf<CategoriaProducto>()

    init {
        inicializarDatosLocales()
        cargarCatalogo()
    }

    private fun inicializarDatosLocales() {
        productosLocales = mutableListOf(
            CategoriaProducto(
                nombre = "Consolas",
                productos = listOf(
                    Producto(1, "PlayStation 5", "Consola de última generación con SSD ultrarrápido.", "https://drive.google.com/uc?export=view&id=1Q6MKGW7gm_NQIXZakoJsuiSA_FoJQKF1", "$499.990"),
                    Producto(2, "Xbox Series X", "La consola más potente de Microsoft con 1 TB SSD y 4K nativo.", "https://drive.google.com/uc?export=view&id=1vI9A1KcWqp9IOw4h9QNeMG_E6x2L2_7i", "$479.990"),
                    Producto(3, "Nintendo Switch OLED", "Versión con pantalla OLED vibrante y dock mejorado.", "https://drive.google.com/uc?export=view&id=1Zxem1wgmk3AEgdgC-fPd8PIUqynCriu2", "$349.990")
                )
            ),
            CategoriaProducto(
                nombre = "PC Gamer",
                productos = listOf(
                    Producto(4, "PC Gamer ASUS ROG Strix", "Potencia extrema para gaming competitivo y multitarea.", "https://drive.google.com/uc?export=view&id=1aJDFQUVFeUkWduMEFVJf6rpN8o3-Xr_h", "$1.899.990"),
                    Producto(5, "MSI Trident X", "Compacta pero poderosa, con RTX 4070 Ti y refrigeración líquida.", "https://drive.google.com/uc?export=view&id=1IMVGUAoIQGqHFF3-C0k5ZX59vOzitqek", "$2.499.990"),
                    Producto(6, "HP Omen Obelisk", "Diseño sobrio y rendimiento de alta gama para eSports.", "https://drive.google.com/uc?export=view&id=1s2TIc4lPDoXxUdajZplQI2xJs5vamBuO", "$1.599.990")
                )
            ),
            CategoriaProducto(
                nombre = "Periféricos",
                productos = listOf(
                    Producto(7, "Mouse Logitech G502 HERO", "Sensor óptico HERO 25K con precisión y ajuste de peso.", "https://drive.google.com/uc?export=view&id=1LD0cFuocEhHE1IJ7iIOjXUUXrpy1EnTY", "$49.990"),
                    Producto(8, "Auriculares HyperX Cloud II", "Audio envolvente 7.1 con micrófono desmontable.", "https://drive.google.com/uc?export=view&id=1S-4uATWHwZPdTI0BcJzO0A6VT0lxuxlo", "$99.990"),
                    Producto(9, "Controlador Inalámbrico Xbox Series X", "Diseño ergonómico con respuesta háptica mejorada.", "https://drive.google.com/uc?export=view&id=11Kve9sgiIzC-tgvJlSsCGcwCkzisoQ7-", "$59.990")
                )
            ),
            CategoriaProducto(
                nombre = "Accesorios",
                productos = listOf(
                    Producto(10, "Mousepad Razer Goliathus Extended", "Superficie RGB con iluminación personalizable.", "https://drive.google.com/uc?export=view&id=1x4E81LfLs-7JwgmhJIm_nBy39uNV9sO5", "$39.990"),
                    Producto(11, "Silla Gamer Secretlab Titan", "Comodidad premium con soporte lumbar ajustable.", "https://drive.google.com/uc?export=view&id=1Xa_9Lv482MF0y_lN-VgglkbQGdDdn8DC", "$459.990"),
                    Producto(12, "Polera Gamer \"Level-Up\"", "Personalízala con tu gamer tag o diseño favorito.", "https://drive.google.com/uc?export=view&id=1o0pf2rq3ptkGfYVrYlAnncyt8yTC7OpF", "$24.990")
                )
            ),
            CategoriaProducto(
                nombre = "Juegos de Mesa",
                productos = listOf(
                    Producto(13, "Catan", "Estrategia y comercio para dominar la isla.", "https://drive.google.com/uc?export=view&id=1cWDjuXW173tOVMZ3RDpixYaUWRJ_PCjM", "$29.990"),
                    Producto(14, "Carcassonne", "Construye ciudades y caminos medievales con tus amigos.", "https://drive.google.com/uc?export=view&id=1tvFFmkR5vA6ilMt2DpCLWxrehVp9Suh5", "$24.990")
                )
            )
        )
    }
    open fun editarProductoLocal(productoEditado: Producto) {
        val nuevasCategorias = productosLocales.map { categoria ->
            val productosActualizados = categoria.productos.map { prod ->
                if (prod.id == productoEditado.id) productoEditado else prod
            }
            categoria.copy(productos = productosActualizados)
        }.toMutableList()
        productosLocales = nuevasCategorias
        cargarCatalogo()
    }
    fun obtenerCategoriasLocales(): List<Producto> {
        return productosLocales.flatMap { it.productos }
    }

    open fun cargarCatalogo() {
        viewModelScope.launch {

            try {
                val listaOfertasApi = GameRetrofitInstance.api.getAllGames()
                val formatoChile = NumberFormat.getInstance(Locale("es", "CL"))
                val valorDolar = 928.0
                val productosApi = listaOfertasApi
                    .take(10)
                    .map { dto ->
                        val precioUsd = dto.originalPrice.toDoubleOrNull() ?: 0.0
                        val precioClp = (precioUsd * valorDolar).toInt()
                        val precioFormateado = formatoChile.format(precioClp)
                        Producto(
                            id = dto.id.hashCode(),
                            nombre = dto.title,
                            descripcion = "",
                            imagen = dto.thumbnail,
                            precio = "$${precioFormateado}"
                        )
                    }
                val categoriaApi = CategoriaProducto(
                    nombre = "Productos Online",
                    productos = productosApi
                )

                val catalogoCompleto = productosLocales + categoriaApi
                _uiState.value = CatalogoUiState(categorias = catalogoCompleto)

            } catch (e: Exception) {
                println("Error en API: ${e.message}. Mostrando solo local.")
                _uiState.value = CatalogoUiState(categorias = productosLocales)
            }
        }
    }
}