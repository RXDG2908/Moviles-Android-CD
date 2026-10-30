package com.tecsup.huellitasvet.model

// Reglas de los formularios de usuario (registro y administrador). null = sin error.
object Validaciones {
    private val FORMATO_CORREO = Regex("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")
    private val FORMATO_CELULAR = Regex("^9\\d{8}$") // celular peruano: 9 dígitos, empieza con 9

    fun nombre(valor: String): String? =
        if (valor.trim().length < 3) "Escribe tu nombre completo" else null

    fun correo(valor: String): String? = when {
        valor.isBlank() -> "Ingresa tu correo"
        !FORMATO_CORREO.matches(valor.trim()) -> "Correo no válido (ejemplo: ana@correo.com)"
        else -> null
    }

    fun telefono(valor: String): String? = when {
        valor.isBlank() -> "Ingresa tu celular"
        !FORMATO_CELULAR.matches(valor.filter { it.isDigit() }) -> "Celular de 9 dígitos que empiece con 9"
        else -> null
    }

    fun distrito(valor: String): String? =
        if (valor !in Distritos.lima) "Elige tu distrito" else null

    fun clave(valor: String): String? =
        if (valor.length < 4) "Mínimo 4 caracteres" else null
}
