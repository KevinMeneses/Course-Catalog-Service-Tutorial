package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.exception.CourseNotFoundException
import com.kotlinspring.exception.InstructorNotValidException
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val instructorService: InstructorService
) {

    companion object : KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO {
        val instructorOptional = instructorService.findByInstructorId(courseDTO.instructorId!!)
        if (instructorOptional.isPresent.not()) throw InstructorNotValidException("Instructor not valid for the id: ${courseDTO.instructorId}")
        val courseEntity = courseDTO.run { Course(null, name, category, instructorOptional.get()) }
        courseRepository.save(courseEntity)
        logger.info("Course saved as: $courseEntity")
        return courseEntity.run { CourseDTO(id, name, category, instructor!!.id) }
    }

    fun retrieveAllCourses(courseName: String?): List<CourseDTO> {
        val courses = if (!courseName.isNullOrEmpty())
            courseRepository.findByNameContaining(courseName)
        else courseRepository.findAll()

        return courses.map {
            CourseDTO(it.id, it.name, it.category, it.instructor!!.id)
        }
    }

    fun updateCourseById(courseDTO: CourseDTO, courseId: Int): CourseDTO {
        return courseRepository.findById(courseId).run {
            if (isPresent) {
                get().apply {
                    name = courseDTO.name
                    category = courseDTO.category
                }.let {
                    CourseDTO(it.id, it.name, it.category)
                }
            } else throw CourseNotFoundException("No course found for the passed in id: $courseId")
        }
    }

    fun deleteCourseById(courseId: Int) {
        courseRepository.findById(courseId).run {
            if (isPresent) courseRepository.deleteById(courseId)
            else throw CourseNotFoundException("No course found for the passed in id: $courseId")
        }
    }
}