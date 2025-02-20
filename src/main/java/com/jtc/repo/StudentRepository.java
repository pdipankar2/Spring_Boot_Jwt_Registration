package com.jtc.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jtc.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
	
	

	Optional<Student> findByEmail(String email);
    Optional<Student> findByStudentId(String studentId);
    
    @Query("SELECT MAX(s.studentId) FROM Student s")
    String findMaxStudentId();
    
    @Modifying
    @Query("UPDATE Student s SET s.isAdmin = ?2 WHERE s.email = ?1")
    void updateAdminStatus(String email, boolean isAdmin);

}
