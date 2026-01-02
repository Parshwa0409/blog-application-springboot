package patil.parshwa.blog.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import patil.parshwa.blog.dto.PostRequestDto;
import patil.parshwa.blog.dto.PostResponseDto;
import patil.parshwa.blog.models.Post;
import patil.parshwa.blog.repositories.PostRepository;
import patil.parshwa.blog.security.UserFacade;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserFacade userFacade;
    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        Long now = System.currentTimeMillis();
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
}
