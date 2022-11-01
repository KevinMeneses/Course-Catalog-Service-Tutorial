package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.service.CourseService
import com.kotlinspring.util.courseDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(CourseController::class)
@AutoConfigureWebTestClient
class CourseControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMockk: CourseService

    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(null, "Course 1", "Testing", 1)
        every { courseServiceMockk.addCourse(any()) } returns courseDTO(id = 1)

        val savedCourseDTO = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody
        Assertions.assertTrue(savedCourseDTO?.id != null)
    }

    @Test
    fun addCourseValidation() {
        val courseDTO = CourseDTO(null, "", "", 1)
        every { courseServiceMockk.addCourse(any()) } returns courseDTO(id = 1)

        val response = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("course category must not be blank, course name must not be blank", response)
    }

    @Test
    fun addCourseRuntimeException() {
        val courseDTO = CourseDTO(null, "Course 1", "Testing", 1)
        val exceptionMessage = "An unexpected error has occurred"
        every { courseServiceMockk.addCourse(any()) } throws RuntimeException(exceptionMessage)

        val response = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(exceptionMessage, response)
    }

    @Test
    fun retrieveAllCourses() {
        every { courseServiceMockk.retrieveAllCourses(any()) } returns listOf(
            courseDTO(1), courseDTO(2), courseDTO(3)
        )
        val courseDTOList = webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(3, courseDTOList?.size)
    }

    @Test
    fun updateCourseById() {
        val courseDTO = courseDTO()
        every { courseServiceMockk.updateCourseById(any(), any()) } returns courseDTO(100)
        val updatedCourse = webTestClient.put()
            .uri("/v1/courses/{courseId}", 100)
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(courseDTO.name, updatedCourse?.name)
        Assertions.assertEquals(courseDTO.category, updatedCourse?.category)
    }

    @Test
    fun deleteCourseById() {
        every { courseServiceMockk.deleteCourseById(any()) } just runs

        webTestClient.delete()
            .uri("/v1/courses/{courseId}", 100)
            .exchange()
            .expectStatus().isNoContent
    }
}