package com.kotlinspring.util

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.entity.Instructor

fun courseEntityList() = listOf(
    Course(1, "Course 1", "Test"),
    Course(2, "Course 2", "Test"),
    Course(3, "Course 3", "Test")
)

fun courseEntityList(instructor: Instructor? = null) = listOf(
    Course(1, "Course 1", "Test", instructor),
    Course(2, "Course 2", "Test", instructor),
    Course(3, "Course 3", "Test", instructor)
)

fun courseDTO(
    id: Int? = null,
    name: String = "Course 1",
    category: String = "Testing"
) = CourseDTO(
    id = id,
    name = name,
    category = category
)

fun instructorDTO(
    id: Int? = null,
    name: String = "Kevin Meneses"
) = InstructorDTO(
    id = id,
    name = name
)

fun instructorEntity(
    id: Int? = null,
    name: String = "Kevin Meneses"
) = Instructor(
    id = id,
    name = name
)