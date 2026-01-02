package patil.parshwa.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import patil.parshwa.blog.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
