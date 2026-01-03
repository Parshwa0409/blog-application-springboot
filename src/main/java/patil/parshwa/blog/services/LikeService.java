package patil.parshwa.blog.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import patil.parshwa.blog.error.ResourceNotFoundException;
import patil.parshwa.blog.models.Like;
import patil.parshwa.blog.models.LikeId;
import patil.parshwa.blog.models.Post;
import patil.parshwa.blog.models.User;
import patil.parshwa.blog.repositories.LikeRepository;
import patil.parshwa.blog.repositories.PostRepository;
import patil.parshwa.blog.security.UserFacade;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final UserFacade userFacade;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public void likePost(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        User user = userFacade.getCurrentUser();
        LikeId likeId = new LikeId(user.getId(), postId);
        if (likeRepository.existsByLikeId(likeId)) {
            return;
        }
        likeRepository.save(new Like(likeId, user, post, System.currentTimeMillis()));
    }

    public void deleteLike(long postId) {
        User user = userFacade.getCurrentUser();
        LikeId likeId = new LikeId(user.getId(), postId);
        if (!likeRepository.existsByLikeId(likeId))
            return;

        likeRepository.deleteById(likeId);
    }
}
