package com.jtc.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.dto.CourseDTO;
import com.jtc.dto.StudentDTO;
import com.jtc.security.JwtTokenUtil;
import com.jtc.service.CourseService;
import com.jtc.service.StudentService;
import com.jtc.util.EmailService;
import com.jtc.util.OTPService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class StudentRestController {

	

	private final StudentService studentService;
	private final CourseService courseService;
	private final EmailService emailService;
	private final OTPService otpService;
	private final JwtTokenUtil jwtTokenUtil;



	// Authentication endpoints

	public StudentRestController(StudentService studentService, CourseService courseService, EmailService emailService,
			OTPService otpService, JwtTokenUtil jwtTokenUtil) {
		super();
		this.studentService = studentService;
		this.courseService = courseService;
		this.emailService = emailService;
		this.otpService = otpService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	
	//UPDATE student5 SET is_Admin = true WHERE email = 'Pdipankar28@gmail.com'; first time

	
	@PostMapping("/auth/send-otp")
	public ResponseEntity<?> sendOtp(@RequestParam String email) {
		try {
			String result = otpService.sendOtp(email);
			if (result.startsWith("Error") || result.contains("not registered")) {
				return ResponseEntity.badRequest().body(result);
			}
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error: " + e.getMessage());
		}
	}
//http://localhost:8181/api/auth/verity-otp?email=pdipankar28@gmail.com&otp=686613
	@PostMapping("/auth/verify-otp")
	public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
	    try {
	        if (otpService.verifyOtp(email, otp)) {
	            StudentDTO student = studentService.getStudentByEmail(email);
	            String role = student.isAdmin() ? "ROLE_ADMIN" : "ROLE_STUDENT";
	            
	            // Generate JWT token
	            String token = jwtTokenUtil.generateToken(
	                email, 
	                Collections.singletonList(role)
	            );

	            return ResponseEntity.ok(Map.of(
	                "message", "Authentication successful",
	                "token", token,
	                "role", role.replace("ROLE_", ""),
	                "studentId", student.getStudentId()
	            ));
	        }
	        return ResponseEntity.badRequest().body("Invalid OTP verification");
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
	    }
	}

	@PostMapping("/auth/logout")
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return ResponseEntity.ok("Logged out successfully");
	}

	// Student endpoints

	@PostMapping("/students/register")
	public ResponseEntity<StudentDTO> registerStudent(@RequestBody StudentDTO studentDTO) {
		return ResponseEntity.ok(studentService.registerStudent(studentDTO));
	}
//http://localhost:8181/api/students
	@GetMapping("/students")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<StudentDTO>> getAllStudents() {
		return ResponseEntity.ok(studentService.getAllStudents());
	}
//http://localhost:8181/api/students/2
	@GetMapping("/students/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
	public ResponseEntity<StudentDTO> getStudent(@PathVariable int id, Authentication authentication) {
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"))) {
			StudentDTO currentStudent = studentService.getStudentByEmail(authentication.getName());
			if (currentStudent.getId() != id) {
				return ResponseEntity.status(403).build();
			}
		}
		return ResponseEntity.ok(studentService.getStudent(id));
	}
//http://localhost:8181/api/students/2
	@PutMapping("/students/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<StudentDTO> updateStudent(@PathVariable int id, @RequestBody StudentDTO studentDTO) {
		return ResponseEntity.ok(studentService.updateStudent(id, studentDTO));
	}
//http://localhost:8181/api/students/2
	@DeleteMapping("/students/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
		studentService.deleteStudent(id);
		return ResponseEntity.ok().build();
	}

	// Course endpoints

	@GetMapping("/courses")
	public ResponseEntity<List<CourseDTO>> getAllCourses() {
		return ResponseEntity.ok(courseService.getAllCourses());
	}
//http://localhost:8181/api/courses
	@PostMapping("/courses")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CourseDTO> addCourse(@RequestBody CourseDTO courseDTO) {
		return ResponseEntity.ok(courseService.addCourse(courseDTO));
	}

	@PutMapping("/courses/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CourseDTO> updateCourse(@PathVariable int id, @RequestBody CourseDTO courseDTO) {
		return ResponseEntity.ok(courseService.updateCourse(id, courseDTO));
	}
	//http://localhost:8181/api/courses/1

	@DeleteMapping("/courses/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
		courseService.deleteCourse(id);
		return ResponseEntity.ok().build();
	}

	// Dashboard endpoints

	@GetMapping("/dashboard/student")
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<StudentDTO> getStudentDashboard(Authentication authentication) {
		String email = authentication.getName();
		StudentDTO student = studentService.getStudentByEmail(email);
		return ResponseEntity.ok(student);
	}

	@GetMapping("/dashboard/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<StudentDTO>> getAdminDashboard() {
		return ResponseEntity.ok(studentService.getAllStudents());
	}

	// Admin-specific endpoints

	@PutMapping("/admin/students/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<StudentDTO> adminUpdateStudent(@PathVariable int id, @RequestBody StudentDTO studentDTO) {
		return ResponseEntity.ok(studentService.adminUpdateStudent(id, studentDTO));
	}
//PUT /api/admin/set-admin-access?email=pdipankar28@gmail.com&isAdmin=true

	@PutMapping("/admin/set-admin-access")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> setAdminAccess(@RequestParam String email, @RequestParam boolean isAdmin) {
		studentService.setAdminAccess(email, isAdmin);
		return ResponseEntity.ok().build();
	}
}