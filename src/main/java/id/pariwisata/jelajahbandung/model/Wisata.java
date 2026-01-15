package id.pariwisata.jelajahbandung.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wisata")
public class Wisata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    private String alamat;
    private String jamBuka;
    private Double hargaTiket;
    private String urlGambar;
    private Double latitude;
    private Double longitude;
}
