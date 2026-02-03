package id.pariwisata.jelajahbandung.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FonteeService {

    @Value("${fontee.token}")
    private String token;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendOtp(String target, String otp) {
        String url = "https://api.fonnte.com/send";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        Map<String, String> body = new HashMap<>();
        body.put("target", target);
        body.put("message", "Your OTP code is: " + otp);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to send OTP via Fontee: " + response.getBody());
            }
            log.info("OTP sent successfully to {}", target);
        } catch (Exception e) {
            log.error("Error sending OTP via Fontee", e);
            throw new RuntimeException("Error sending OTP via Fontee", e);
        }
    }
}
