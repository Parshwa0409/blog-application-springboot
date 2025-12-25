package patil.parshwa.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import patil.parshwa.blog.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
