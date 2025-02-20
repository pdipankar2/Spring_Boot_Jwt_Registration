package com.jtc.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.jtc.entity.Student;
import com.jtc.repo.StudentRepository;

@Service
public class OTPService {

	

  /*  private final StudentRepository studentRepository;
    private final EmailService emailService;
   
    
    public OTPService(StudentRepository studentRepository, EmailService emailService) {
		super();
		this.studentRepository = studentRepository;
		this.emailService = emailService;
	}

	private final Map<String, OTPData> otpStore = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();
    
    
    

    public String sendOtp(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email not registered"));

        String otp = generateOTP();
        otpStore.put(email, new OTPData(otp, LocalDateTime.now()));

        String subject = "Your OTP for Authentication";
        String body = String.format("""
            <html>
                <body>
                    <h2>Authentication OTP</h2>
                    <p>Your OTP for authentication is: <strong>%s</strong></p>
                    <p>This OTP will expire in 5 minutes.</p>
                    <p>If you didn't request this OTP, please ignore this email.</p>
                </body>
            </html>
            """, otp);

        emailService.sendEmail(email, subject, body);
        return "OTP sent successfully";
    }

    public boolean verifyOtp(String email, String otp) {
        OTPData otpData = otpStore.get(email);
        if (otpData == null) {
            throw new IllegalArgumentException("No OTP request found");
        }

        if (LocalDateTime.now().isAfter(otpData.timestamp.plusMinutes(5))) {
            otpStore.remove(email);
            throw new IllegalArgumentException("OTP has expired");
        }

        if (!otpData.otp.equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        // Remove the used OTP
        otpStore.remove(email);

        return true;
    }

    private String generateOTP() {
        return String.format("%06d", random.nextInt(1000000));
    }

    private static class OTPData {
        final String otp;
        final LocalDateTime timestamp;

        OTPData(String otp, LocalDateTime timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }
    }
	*/
	
	   private final StudentRepository studentRepository;
	    private final EmailService emailService;
	    
	    
	    
	    public OTPService(StudentRepository studentRepository, EmailService emailService) {
			super();
			this.studentRepository = studentRepository;
			this.emailService = emailService;
		}

		private final Map<String, OTPData> otpStore = new ConcurrentHashMap<>();
	    private final SecureRandom random = new SecureRandom();

	    public String sendOtp(String email) {
	        try {
	            // First check if the email exists
	            Optional<Student> student = studentRepository.findByEmail(email);
	            if (student.isEmpty()) {
	                return "Email not registered. Please register first.";
	            }

	            // Generate and store OTP
	            String otp = generateOTP();
	            otpStore.put(email, new OTPData(otp, LocalDateTime.now()));

	            // Prepare email content
	            String subject = "Your Authentication OTP";
	            String body = String.format("""
	                <html>
	                    <body>
	                        <h2>Authentication OTP</h2>
	                        <p>Hello %s,</p>
	                        <p>Your OTP for authentication is: <strong>%s</strong></p>
	                        <p>This OTP will expire in 5 minutes.</p>
	                        <p>If you didn't request this OTP, please ignore this email.</p>
	                    </body>
	                </html>
	                """, student.get().getName(), otp);

	            // Send email
	            emailService.sendEmail(email, subject, body);
	            return "OTP sent successfully to " + email;
	            
	        } catch (Exception e) {
	            return "Error sending OTP: " + e.getMessage();
	        }
	    }

	    public boolean verifyOtp(String email, String otp) {
	        try {
	            OTPData otpData = otpStore.get(email);
	            if (otpData == null) {
	                throw new IllegalArgumentException("No OTP request found. Please request a new OTP.");
	            }

	            // Check OTP expiration
	            if (LocalDateTime.now().isAfter(otpData.timestamp.plusMinutes(5))) {
	                otpStore.remove(email);
	                throw new IllegalArgumentException("OTP has expired. Please request a new OTP.");
	            }

	            // Verify OTP
	            if (!otpData.otp.equals(otp)) {
	                throw new IllegalArgumentException("Invalid OTP. Please try again.");
	            }

	            // Remove used OTP
	            otpStore.remove(email);
	            return true;

	        } catch (Exception e) {
	            throw new IllegalArgumentException(e.getMessage());
	        }
	    }

	    private String generateOTP() {
	        return String.format("%06d", random.nextInt(1000000));
	    }

	    private static class OTPData {
	        final String otp;
	        final LocalDateTime timestamp;

	        OTPData(String otp, LocalDateTime timestamp) {
	            this.otp = otp;
	            this.timestamp = timestamp;
	        }
	    }
}
