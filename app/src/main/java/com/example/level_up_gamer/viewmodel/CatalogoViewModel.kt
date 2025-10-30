package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.level_up_gamer.model.CategoriaProducto
import com.example.level_up_gamer.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CatalogoUiState(
    val categorias: List<CategoriaProducto> = emptyList()
)

class CatalogoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogoUiState())
    val uiState: StateFlow<CatalogoUiState> = _uiState

    init {
        cargarCatalogo()
    }

    private fun cargarCatalogo() {
        val categoriasEspecificas = listOf(
            CategoriaProducto(
                nombre = "Consolas",
                productos = listOf(
                    Producto(1, "PlayStation 5", "Consola de última generación con SSD ultrarrápido.", "play5", "$499.990"),
                    Producto(2, "Xbox Series X", "La consola más potente de Microsoft con 1 TB SSD y 4K nativo.", "xboxseriesx", "$479.990"),
                    Producto(3, "Nintendo Switch OLED", "Versión con pantalla OLED vibrante y dock mejorado.", "sitch", "$349.990")
                )
            ),
            CategoriaProducto(
                nombre = "PC Gamer",
                productos = listOf(
                    Producto(4, "PC Gamer ASUS ROG Strix", "Potencia extrema para gaming competitivo y multitarea.", "pc", "$1.899.990"),
                    Producto(5, "MSI Trident X", "Compacta pero poderosa, con RTX 4070 Ti y refrigeración líquida.", "msitrident", "$2.499.990"),
                    Producto(6, "HP Omen Obelisk", "Diseño sobrio y rendimiento de alta gama para eSports.", "pc2", "$1.599.990")
                )
            ),
            CategoriaProducto(
                nombre = "Periféricos",
                productos = listOf(
                    Producto(7, "Mouse Logitech G502 HERO", "Sensor óptico HERO 25K con precisión y ajuste de peso.", "mouse", "$49.990"),
                    Producto(8, "Auriculares HyperX Cloud II", "Audio envolvente 7.1 con micrófono desmontable.", "gamer", "$99.990"),
                    Producto(9, "Controlador Inalámbrico Xbox Series X", "Diseño ergonómico con respuesta háptica mejorada.", "mandoxbox", "$59.990")
                )
            ),
            CategoriaProducto(
                nombre = "Accesorios",
                productos = listOf(
                    Producto(10, "Mousepad Razer Goliathus Extended", "Superficie RGB con iluminación personalizable.", "mousepad", "$39.990"),
                    Producto(11, "Silla Gamer Secretlab Titan", "Comodidad premium con soporte lumbar ajustable.", "sillagamer", "$459.990"),
                    Producto(12, "Polera Gamer \"Level-Up\"", "Personalízala con tu gamer tag o diseño favorito.", "poleragamerlevelupgamer2", "$24.990")
                )
            ),
            CategoriaProducto(
                nombre = "Juegos de Mesa",
                productos = listOf(
                    Producto(13, "Catan", "Estrategia y comercio para dominar la isla.", "catan", "$29.990"),
                    Producto(14, "Carcassonne", "Construye ciudades y caminos medievales con tus amigos.", "carcasone", "$24.990")
                )
            )
        )

        val todosLosProductos = categoriasEspecificas.flatMap { it.productos }
        val categoriaTodos = CategoriaProducto(nombre = "Todos", productos = todosLosProductos)

        _uiState.value = CatalogoUiState(categorias = listOf(categoriaTodos) + categoriasEspecificas)
    }
}
