package patil.parshwa.blog.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import patil.parshwa.blog.dto.PostRequestDto;
import patil.parshwa.blog.dto.PostResponseDto;
import patil.parshwa.blog.error.ForbiddenException;
import patil.parshwa.blog.error.ResourceNotFoundException;
import patil.parshwa.blog.models.Post;
import patil.parshwa.blog.models.User;
import patil.parshwa.blog.repositories.PostRepository;
import patil.parshwa.blog.security.UserFacade;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserFacade userFacade;
    private final PostRepository postRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        long now = System.currentTimeMillis();
        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .createdAt(now)
                .updatedAt(now)
                .author(userFacade.getCurrentUser())
                .build();

        postRepository.save(post);

        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(long postId, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        validatePostAuthor(post);

        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setUpdatedAt(System.currentTimeMillis());

        postRepository.save(post);

        return new PostResponseDto(post);
    }

    @Transactional
    public void deletePost(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        validatePostAuthor(post);

        postRepository.delete(post);
    }

    private void validatePostAuthor(Post post) {
        User currentUser = userFacade.getCurrentUser();
        if (!post.getAuthor().equals(currentUser)) {
            throw new ForbiddenException("You are not authorized to perform this action");
        }
    }
}
