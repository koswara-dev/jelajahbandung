package id.pariwisata.jelajahbandung.repository;

import id.pariwisata.jelajahbandung.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByNamaContainingIgnoreCase(String nama, Pageable pageable);
}
