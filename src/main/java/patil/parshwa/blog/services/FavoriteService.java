package patil.parshwa.blog.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import patil.parshwa.blog.error.ResourceNotFoundException;
import patil.parshwa.blog.models.FavId;
import patil.parshwa.blog.models.Favorite;
import patil.parshwa.blog.models.Post;
import patil.parshwa.blog.models.User;
import patil.parshwa.blog.repositories.FavoriteRepository;
import patil.parshwa.blog.repositories.PostRepository;
import patil.parshwa.blog.security.UserFacade;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserFacade userFacade;
    private final PostRepository postRepository;

    public void addFavorite(long postId) {
        User user = userFacade.getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Post", "id", postId)
                );

        FavId favId = new FavId(user.getId(), postId);
        if (favoriteRepository.existsByFavId(favId)) {
            return; // Favorite already exists
        }

        favoriteRepository.save(new Favorite(favId, user, post, System.currentTimeMillis()));
    }

    public void removeFavorite(long postId) {
        User user = userFacade.getCurrentUser();
        postRepository.findById(postId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Post", "id", postId)
                );

        FavId favId = new FavId(user.getId(), postId);
        if (!favoriteRepository.existsByFavId(favId)) {
            return; // Favorite doesn't exist
        }

        favoriteRepository.existsByFavId(favId);
    }
}
