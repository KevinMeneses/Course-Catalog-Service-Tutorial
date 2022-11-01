package com.kotlinspring.dto

import javax.validation.constraints.NotBlank

data class InstructorDTO(
    val id: Int?,
    @get:NotBlank(message = "instructor name must not be blank")
    val name: String,
)
