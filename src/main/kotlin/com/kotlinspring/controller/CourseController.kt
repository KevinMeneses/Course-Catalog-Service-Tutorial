package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/courses")
@Validated
class CourseController(private val courseService: CourseService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody @Valid courseDTO: CourseDTO) = courseService.addCourse(courseDTO)

    @GetMapping
    fun retrieveAllCourses(@RequestParam("course_name", required = false) courseName: String?) =
        courseService.retrieveAllCourses(courseName)

    @PutMapping("/{course_id}")
    fun updateCourseById(
        @RequestBody courseDTO: CourseDTO,
        @PathVariable("course_id") courseId: Int
    ) = courseService.updateCourseById(courseDTO, courseId)

    @DeleteMapping("/{course_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourseById(@PathVariable("course_id") courseId: Int) = courseService.deleteCourseById(courseId)
}