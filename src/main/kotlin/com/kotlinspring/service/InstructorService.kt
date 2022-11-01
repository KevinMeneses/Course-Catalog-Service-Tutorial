package com.kotlinspring.service

import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.entity.Instructor
import com.kotlinspring.repository.InstructorRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class InstructorService(private val instructorRepository: InstructorRepository) {
    fun createInstructor(instructorDTO: InstructorDTO): InstructorDTO {
        val instructorEntity = instructorDTO.run { Instructor(id, name) }
        instructorRepository.save(instructorEntity)
        return instructorEntity.run { InstructorDTO(id, name) }
    }

    fun findByInstructorId(instructorId: Int): Optional<Instructor> {
        return instructorRepository.findById(instructorId)
    }
}
