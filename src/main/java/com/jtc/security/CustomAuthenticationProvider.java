package com.jtc.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.jtc.entity.Student;
import com.jtc.repo.StudentRepository;
import com.jtc.util.OTPService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	  private final StudentRepository studentRepository;
	    private final OTPService otpService;
	    
	    

	    public CustomAuthenticationProvider(StudentRepository studentRepository, OTPService otpService) {
			super();
			this.studentRepository = studentRepository;
			this.otpService = otpService;
		}

		@Override
	    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	        String email = authentication.getName();
	        String otp = authentication.getCredentials().toString();

	        if (otpService.verifyOtp(email, otp)) {
	            Student student = studentRepository.findByEmail(email)
	                    .orElseThrow(() -> new BadCredentialsException("User not found"));

	            List<GrantedAuthority> authorities = new ArrayList<>();
	            authorities.add(new SimpleGrantedAuthority(student.isAdmin() ? "ROLE_ADMIN" : "ROLE_STUDENT"));

	            return new UsernamePasswordAuthenticationToken(email, null, authorities);
	        } else {
	            throw new BadCredentialsException("Invalid OTP");
	        }
	    }

	    @Override
	    public boolean supports(Class<?> authentication) {
	        return authentication.equals(UsernamePasswordAuthenticationToken.class);
	    }

}
