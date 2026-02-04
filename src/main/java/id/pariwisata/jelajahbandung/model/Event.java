package id.pariwisata.jelajahbandung.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    private String lokasi;

    private Double latitude;

    private Double longitude;

    private String urlGambar;

    @Enumerated(EnumType.STRING)
    private KategoriEvent kategori;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    private java.time.LocalDate tanggalMulai;
    private java.time.LocalDate tanggalSelesai;
    private java.time.LocalTime waktuMulai;
    private java.time.LocalTime waktuSelesai;

    private java.math.BigDecimal htmRegular;
    private java.math.BigDecimal htmVip;

    private String statusHtm;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
