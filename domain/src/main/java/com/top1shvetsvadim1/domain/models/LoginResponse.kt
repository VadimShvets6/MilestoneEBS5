package com.top1shvetsvadim1.domain.models

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "email")
    val email: String?,
    @Json(name = "password")
    val password: String?,
)