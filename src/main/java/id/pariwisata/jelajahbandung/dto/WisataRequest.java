package id.pariwisata.jelajahbandung.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WisataRequest {
    @NotBlank(message = "Nama cannot be blank")
    private String nama;

    @NotBlank(message = "Deskripsi cannot be blank")
    private String deskripsi;

    @NotBlank(message = "Alamat cannot be blank")
    private String alamat;

    @NotBlank(message = "Jam Buka cannot be blank")
    private String jamBuka;

    @NotNull(message = "Harga Tiket cannot be null")
    @DecimalMin(value = "0.0", message = "Harga Tiket must be greater than 0")
    private Double hargaTiket;

    @NotBlank(message = "URL Gambar cannot be blank")
    private String urlGambar;

    @NotNull(message = "Latitude cannot be null")
    private Double latitude;

    @NotNull(message = "Longitude cannot be null")
    private Double longitude;

    @NotNull(message = "Kategori ID cannot be null")
    private Long kategoriId;
}
