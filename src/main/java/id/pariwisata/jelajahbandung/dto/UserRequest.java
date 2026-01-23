package id.pariwisata.jelajahbandung.dto;

import id.pariwisata.jelajahbandung.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String email;
    private String password;
    private String fullName;
    private Role role;
    private MultipartFile foto;
}
