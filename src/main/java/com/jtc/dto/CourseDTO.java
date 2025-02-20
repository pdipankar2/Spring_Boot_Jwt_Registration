package com.jtc.dto;

public class CourseDTO {
	

	    private int id;
	    private String course;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getCourse() {
			return course;
		}
		public void setCourse(String course) {
			this.course = course;
		}
		public CourseDTO(int id, String course) {
			super();
			this.id = id;
			this.course = course;
		}
		public CourseDTO() {
			super();
			// TODO Auto-generated constructor stub
		}
		
	    
	    

}
