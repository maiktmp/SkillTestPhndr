package mx.com.maiktmp.web.api.models

data class GenericResponseAPI<T>(
    var success: Boolean = false,

    var message: String? = "",

    var data: T? = null,

    var code: Int? = 0,
)
