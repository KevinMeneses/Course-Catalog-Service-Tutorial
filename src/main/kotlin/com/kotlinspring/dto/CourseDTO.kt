package com.kotlinspring.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CourseDTO(
    val id: Int?,
    @get:NotBlank(message = "course name must not be blank")
    val name: String,
    @get:NotBlank(message = "course category must not be blank")
    val category: String,
    @get:NotNull(message = "instructor id must not be null")
    val instructorId: Int? = null
)
