package id.pariwisata.jelajahbandung.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyPhoneRequest {
    private String phoneNumber;
    private String otp;
}
