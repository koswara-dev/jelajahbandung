package id.pariwisata.jelajahbandung.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KategoriRequest {
    @NotBlank(message = "Nama kategori cannot be blank")
    private String nama;

    private String deskripsi;
    private String urlGambar;
}
