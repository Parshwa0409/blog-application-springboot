package patil.parshwa.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import patil.parshwa.blog.models.FavId;
import patil.parshwa.blog.models.Favorite;
import patil.parshwa.blog.models.Post;
import patil.parshwa.blog.models.User;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavId> {
    boolean existsByFavId(FavId favId);
    List<Favorite> findByUser(User user);
}
