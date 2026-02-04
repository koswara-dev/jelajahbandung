package id.pariwisata.jelajahbandung.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    private String nama;
    private String lokasi;
    private Double latitude;
    private Double longitude;
    private String kategori;
    private String deskripsi;
    private java.time.LocalDate tanggalMulai;
    private java.time.LocalDate tanggalSelesai;
    private java.time.LocalTime waktuMulai;
    private java.time.LocalTime waktuSelesai;
    private java.math.BigDecimal htmRegular;
    private java.math.BigDecimal htmVip;
    private String statusHtm;
    private MultipartFile gambar;
}
