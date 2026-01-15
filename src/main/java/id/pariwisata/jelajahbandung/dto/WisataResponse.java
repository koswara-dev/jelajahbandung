package id.pariwisata.jelajahbandung.dto;

import id.pariwisata.jelajahbandung.model.Wisata;
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

    public static WisataResponse fromEntity(Wisata wisata) {
        return WisataResponse.builder()
                .id(wisata.getId())
                .nama(wisata.getNama())
                .deskripsi(wisata.getDeskripsi())
                .alamat(wisata.getAlamat())
                .jamBuka(wisata.getJamBuka())
                .hargaTiket(wisata.getHargaTiket())
                .urlGambar(wisata.getUrlGambar())
                .latitude(wisata.getLatitude())
                .longitude(wisata.getLongitude())
                .build();
    }
}
