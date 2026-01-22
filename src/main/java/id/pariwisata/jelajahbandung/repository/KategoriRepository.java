package id.pariwisata.jelajahbandung.repository;

import id.pariwisata.jelajahbandung.model.Kategori;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KategoriRepository extends JpaRepository<Kategori, Long> {
    Page<Kategori> findByNamaContainingIgnoreCase(String nama, Pageable pageable);
}
