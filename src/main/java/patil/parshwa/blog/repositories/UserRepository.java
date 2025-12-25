package patil.parshwa.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import patil.parshwa.blog.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
