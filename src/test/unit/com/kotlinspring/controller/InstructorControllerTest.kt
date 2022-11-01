package com.kotlinspring.controller

import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.service.InstructorService
import com.kotlinspring.util.instructorDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(InstructorController::class)
@AutoConfigureWebTestClient
class InstructorControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var instructorServiceMockk: InstructorService

    @Test
    fun addCourse() {
        val courseDTO = InstructorDTO(null, "Kevin Meneses")
        every { instructorServiceMockk.createInstructor(any()) } returns instructorDTO(id = 1)

        val savedInstructorDTO = webTestClient.post()
            .uri("/v1/instructors")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(InstructorDTO::class.java)
            .returnResult()
            .responseBody
        Assertions.assertTrue(savedInstructorDTO?.id != null)
    }

    @Test
    fun createInstructorValidation() {
        val instructorDTO = InstructorDTO(null, "")
        every { instructorServiceMockk.createInstructor(any()) } returns instructorDTO(id = 1)

        val response = webTestClient.post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("instructor name must not be blank", response)
    }
}