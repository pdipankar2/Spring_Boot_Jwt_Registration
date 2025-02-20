package com.jtc.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jtc.dto.StudentDTO;
import com.jtc.entity.Student;
import com.jtc.repo.StudentRepository;
import com.jtc.util.OTPService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class StudentService {
	
	

    private final StudentRepository studentRepository;
    private final OTPService otpService;

    public StudentService(StudentRepository studentRepository, OTPService otpService) {
		super();
		this.studentRepository = studentRepository;
		this.otpService = otpService;
	}


    public StudentDTO registerStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setPhone(studentDTO.getPhone());
        student.setCity(studentDTO.getCity());
        
        String lastId = studentRepository.findMaxStudentId();
        String newId = generateStudentId(lastId);
        student.setStudentId(newId);
        
        Student savedStudent = studentRepository.save(student);
        return mapToDTO(savedStudent);
    }

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudent(int id) {
        return studentRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }

    public StudentDTO getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }

    @Transactional
    public StudentDTO updateStudent(int id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        
        student.setName(studentDTO.getName());
        student.setPhone(studentDTO.getPhone());
        student.setCity(studentDTO.getCity());
        
        Student updatedStudent = studentRepository.save(student);
        return mapToDTO(updatedStudent);
    }

    @Transactional
    public StudentDTO adminUpdateStudent(int id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setPhone(studentDTO.getPhone());
        student.setCity(studentDTO.getCity());
        student.setAdmin(studentDTO.isAdmin());
        
        Student updatedStudent = studentRepository.save(student);
        return mapToDTO(updatedStudent);
    }

    @Transactional
    public void deleteStudent(int id) {
        studentRepository.deleteById(id);
    }

    @Transactional
    public void setAdminAccess(String email, boolean isAdmin) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        student.setAdmin(isAdmin);
        studentRepository.save(student);
    }

    private StudentDTO mapToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setStudentId(student.getStudentId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setCity(student.getCity());
        dto.setCourse(student.getCourse() != null ? student.getCourse().getCourse() : null);
        dto.setAdmin(student.isAdmin());
        return dto;
    }

    private String generateStudentId(String lastId) {
        if (lastId == null) {
            return "JTC-001";
        }
        int numberPart = Integer.parseInt(lastId.substring(4));
        return "JTC-" + String.format("%03d", numberPart + 1);
    }
}
