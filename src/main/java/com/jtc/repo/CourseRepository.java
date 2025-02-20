package com.jtc.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtc.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {

}
