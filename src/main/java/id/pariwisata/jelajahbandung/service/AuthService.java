package id.pariwisata.jelajahbandung.service;

import id.pariwisata.jelajahbandung.config.JwtService;
import id.pariwisata.jelajahbandung.dto.AuthResponse;
import id.pariwisata.jelajahbandung.dto.LoginRequest;
import id.pariwisata.jelajahbandung.dto.RegisterRequest;
import id.pariwisata.jelajahbandung.dto.VerifyEmailRequest;
import id.pariwisata.jelajahbandung.model.Role;
import id.pariwisata.jelajahbandung.model.User;
import id.pariwisata.jelajahbandung.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final EmailService emailService;

        public AuthResponse register(RegisterRequest request) {
                if (repository.findByEmail(request.getEmail()).isPresent()) {
                        throw new RuntimeException("Email already exists");
                }
                String otp = generateOtp();
                var user = User.builder()
                                .fullName(request.getFullName())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(Role.USER)
                                .isEmailVerified(false)
                                .otp(otp)
                                .otpExpiration(LocalDateTime.now().plusMinutes(10))
                                .build();
                repository.save(user);
                emailService.sendOtp(user.getEmail(), otp);
                var jwtToken = jwtService.generateToken(user);
                return AuthResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        public void verifyEmail(VerifyEmailRequest request) {
                var user = repository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                if (user.isEmailVerified()) {
                        throw new RuntimeException("Email is already verified");
                }

                if (user.getOtpExpiration().isBefore(LocalDateTime.now())) {
                        throw new RuntimeException("OTP has expired");
                }

                if (!user.getOtp().equals(request.getOtp())) {
                        throw new RuntimeException("Invalid OTP");
                }

                user.setEmailVerified(true);
                user.setOtp(null);
                user.setOtpExpiration(null);
                repository.save(user);
        }

        private String generateOtp() {
                Random random = new Random();
                int otp = 100000 + random.nextInt(900000);
                return String.valueOf(otp);
        }

        public AuthResponse authenticate(LoginRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
                var user = repository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("User not found"));
                var jwtToken = jwtService.generateToken(user);
                return AuthResponse.builder()
                                .token(jwtToken)
                                .build();
        }
}
