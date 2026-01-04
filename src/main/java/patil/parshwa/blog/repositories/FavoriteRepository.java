package patil.parshwa.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import patil.parshwa.blog.models.FavId;
import patil.parshwa.blog.models.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavId> {
    boolean existsByFavId(FavId favId);
}
