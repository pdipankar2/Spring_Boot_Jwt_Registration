package com.jtc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jtc.dto.CourseDTO;
import com.jtc.entity.Course;
import com.jtc.repo.CourseRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CourseService {
	
	
	private final CourseRepository courseRepository;
	
	

    public CourseService(CourseRepository courseRepository) {
		super();
		this.courseRepository = courseRepository;
	}

	public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CourseDTO addCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setCourse(courseDTO.getCourse());
        Course savedCourse = courseRepository.save(course);
        return mapToDTO(savedCourse);
    }

    @Transactional
    public CourseDTO updateCourse(int id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        course.setCourse(courseDTO.getCourse());
        Course updatedCourse = courseRepository.save(course);
        return mapToDTO(updatedCourse);
    }

    @Transactional
    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }

    private CourseDTO mapToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
       
        dto.setCourse(course.getCourse());
        return dto;
    }

}
