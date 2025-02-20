package com.jtc.dto;

public class StudentDTO {
	

    private int id;
    private String studentId;
    private String name;
    private String email;
    private String phone;
    private String city;
    private String course;
    private boolean isAdmin;
    
    
    
    
    
	public StudentDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StudentDTO(int id, String studentId, String name, String email, String phone, String city, String course,
			boolean isAdmin) {
		super();
		this.id = id;
		this.studentId = studentId;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.city = city;
		this.course = course;
		this.isAdmin = isAdmin;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	@Override
	public String toString() {
		return "StudentDTO [id=" + id + ", studentId=" + studentId + ", name=" + name + ", email=" + email + ", phone="
				+ phone + ", city=" + city + ", course=" + course + ", isAdmin=" + isAdmin + "]";
	}
    
    
    

}
