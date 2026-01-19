package id.pariwisata.jelajahbandung.repository;

import id.pariwisata.jelajahbandung.model.Wisata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface WisataRepository extends JpaRepository<Wisata, Long> {
    List<Wisata> findByNamaContainingIgnoreCase(String nama);

    List<Wisata> findByKategoriId(Long kategoriId);

    List<Wisata> findByNamaContainingIgnoreCaseAndKategoriId(String nama, Long kategoriId);

    Page<Wisata> findByNamaContainingIgnoreCase(String nama, Pageable pageable);

    Page<Wisata> findByKategoriId(Long kategoriId, Pageable pageable);

    Page<Wisata> findByNamaContainingIgnoreCaseAndKategoriId(String nama, Long kategoriId, Pageable pageable);
}
