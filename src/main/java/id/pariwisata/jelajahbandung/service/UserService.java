package id.pariwisata.jelajahbandung.service;

import id.pariwisata.jelajahbandung.dto.PagedResponse;
import id.pariwisata.jelajahbandung.dto.UserRequest;
import id.pariwisata.jelajahbandung.dto.UserResponse;
import id.pariwisata.jelajahbandung.exception.ResourceNotFoundException;
import id.pariwisata.jelajahbandung.model.Role;
import id.pariwisata.jelajahbandung.model.User;
import id.pariwisata.jelajahbandung.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;
    private final FonteeService fonteeService;

    public PagedResponse<UserResponse> getAllUsers(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> users;

        if (search != null && !search.isEmpty()) {
            users = userRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search,
                    pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        Page<UserResponse> userResponses = users.map(this::mapToResponse);

        return PagedResponse.fromPage(userResponses);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToResponse(user);
    }

    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        String urlFoto = null;
        if (request.getFoto() != null && !request.getFoto().isEmpty()) {
            urlFoto = fileStorageService.storeFile(request.getFoto());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .urlFoto(urlFoto)
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!user.getEmail().equals(request.getEmail()) && userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setRole(request.getRole() != null ? request.getRole() : user.getRole());

        if (request.getFoto() != null && !request.getFoto().isEmpty()) {
            String urlFoto = fileStorageService.storeFile(request.getFoto());
            user.setUrlFoto(urlFoto);
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    @Transactional
    public void sendPhoneOtp(Long userId, String phoneNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Use supplied phone number if provided, otherwise update user record
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // Check if phone number is already used by another user
            userRepository.findByPhoneNumber(phoneNumber)
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(userId)) {
                            throw new IllegalArgumentException("Phone number already in use");
                        }
                    });
            user.setPhoneNumber(phoneNumber);
        } else if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        String otp = generateOtp();
        user.setPhoneOtp(otp);
        user.setPhoneOtpExpiration(LocalDateTime.now().plusMinutes(10));
        user.setPhoneVerified(false);
        userRepository.save(user);

        fonteeService.sendOtp(user.getPhoneNumber(), otp);
    }

    @Transactional
    public void verifyPhoneOtp(Long userId, String otp) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (user.isPhoneVerified()) {
            throw new RuntimeException("Phone is already verified");
        }

        if (user.getPhoneOtpExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        if (!user.getPhoneOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        user.setPhoneVerified(true);
        user.setPhoneOtp(null);
        user.setPhoneOtpExpiration(null);
        userRepository.save(user);
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .urlFoto(user.getUrlFoto())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
