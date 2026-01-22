package id.pariwisata.jelajahbandung.repository;

import id.pariwisata.jelajahbandung.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
