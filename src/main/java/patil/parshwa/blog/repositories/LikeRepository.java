package patil.parshwa.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import patil.parshwa.blog.models.Like;
import patil.parshwa.blog.models.LikeId;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    boolean existsByLikeId(LikeId likeId);
}

