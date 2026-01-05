package patil.parshwa.blog.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import patil.parshwa.blog.dto.PostSummaryDto;
import patil.parshwa.blog.repositories.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final PostRepository postRepository;

    public List<PostSummaryDto> getFeed() {
        return postRepository.findAll()
                .stream()
                .map(PostSummaryDto::new)
                .toList();
    }
}
