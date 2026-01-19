package id.pariwisata.jelajahbandung.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WisataResponse {
    private Long id;
    private String nama;
    private String deskripsi;
    private String alamat;
    private String jamBuka;
    private Double hargaTiket;
    private String urlGambar;
    private Double latitude;
    private Double longitude;
    private Long kategoriId;
    private String kategoriNama;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
