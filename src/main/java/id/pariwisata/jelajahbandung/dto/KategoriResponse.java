package id.pariwisata.jelajahbandung.dto;

import id.pariwisata.jelajahbandung.model.Kategori;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class KategoriResponse {
    private Long id;
    private String nama;
    private String deskripsi;
    private String urlGambar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static KategoriResponse fromEntity(Kategori kategori) {
        return KategoriResponse.builder()
                .id(kategori.getId())
                .nama(kategori.getNama())
                .deskripsi(kategori.getDeskripsi())
                .urlGambar(kategori.getUrlGambar())
                .createdAt(kategori.getCreatedAt())
                .updatedAt(kategori.getUpdatedAt())
                .build();
    }
}
