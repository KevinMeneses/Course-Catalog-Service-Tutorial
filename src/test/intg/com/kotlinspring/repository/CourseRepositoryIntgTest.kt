package com.kotlinspring.repository

import com.kotlinspring.util.PostgresSQLContainerInitializer
import com.kotlinspring.util.courseEntityList
import com.kotlinspring.util.instructorEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryIntgTest: PostgresSQLContainerInitializer() {

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()

        val instructor = instructorEntity()
        instructorRepository.save(instructor)

        val courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    fun findByNameContaining() {
        val result = courseRepository.findByNameContaining("2")
        println("courses: $result")
        Assertions.assertEquals(1, result.size)
    }

    @Test
    fun findCoursesThatContains() {
        val result = courseRepository.findCoursesThatContains("2")
        println("courses: $result")
        Assertions.assertEquals(1, result.size)
    }

    @ParameterizedTest
    @MethodSource("courseAndSize")
    fun findCoursesThatContainsSecondApproach(name: String, expectedSize: Int) {
        val result = courseRepository.findCoursesThatContains(name)
        println("courses: $result")
        Assertions.assertEquals(expectedSize, result.size)
    }

    companion object {
        @JvmStatic
        fun courseAndSize(): Stream<Arguments> {
            return Stream.of(Arguments.arguments("Course", 3), Arguments.arguments("1", 1), Arguments.arguments("3", 1))
        }
    }
}