package id.pariwisata.jelajahbandung.repository;

import id.pariwisata.jelajahbandung.model.Wisata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WisataRepository extends JpaRepository<Wisata, Long> {
    List<Wisata> findByNamaContainingIgnoreCase(String nama);
}
