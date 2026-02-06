package id.pariwisata.jelajahbandung.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifikasiResponse {
    private Long id;
    private String judul;
    private String pesan;
    private String tipe;
    private boolean dibaca;
    private LocalDateTime createdAt;
}
