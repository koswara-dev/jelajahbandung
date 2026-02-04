package id.pariwisata.jelajahbandung.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifikasi")
public class Notifikasi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String judul;

    @Column(columnDefinition = "TEXT")
    private String pesan;

    private String tipe; // e.g., "INFO", "EVENT"

    @Builder.Default
    private boolean dibaca = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
