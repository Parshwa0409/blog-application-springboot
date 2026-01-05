package patil.parshwa.blog.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import patil.parshwa.blog.dto.PostSummaryDto;
import patil.parshwa.blog.error.ForbiddenException;
import patil.parshwa.blog.error.ResourceNotFoundException;
import patil.parshwa.blog.models.Post;
import patil.parshwa.blog.models.PostTag;
import patil.parshwa.blog.models.PostTagId;
import patil.parshwa.blog.models.Tag;
import patil.parshwa.blog.repositories.PostRepository;
import patil.parshwa.blog.repositories.PostTagRepository;
import patil.parshwa.blog.repositories.TagRepository;
import patil.parshwa.blog.security.UserFacade;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostTagService {
    private final PostTagRepository postTagRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserFacade userFacade;

    private void validatePostAuthor(Post post) {
        long userId = userFacade.getCurrentUser().getId();
        long postAuthorId = post.getAuthor().getId();
        if (userId != postAuthorId) {
            throw new ForbiddenException("You are not authorized to perform this action");
        }
    }

    public void addTagToPost(Long postId, Long tagId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post","id", postId));

        validatePostAuthor(post);

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag","id", tagId));

        PostTagId postTagId = new PostTagId(tagId, postId);

        if(postTagRepository.existsByPostTagId(postTagId)) {
            return;
        }

        PostTag postTag = patil.parshwa.blog.models.PostTag.builder()
                .postTagId(postTagId)
                .post(post)
                .tag(tag)
                .build();
        postTagRepository.save(postTag);
    }

    public void deleteTagFromPost(Long postId, Long tagId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post","id", postId));

        validatePostAuthor(post);

        tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag","id", tagId));

        PostTagId postTagId = new PostTagId(tagId, postId);

        if(!postTagRepository.existsByPostTagId(postTagId)) {
            return;
        }

        postTagRepository.deleteById(postTagId);
    }

    public List<PostSummaryDto> getPostsByTagId(Long tagId) {
        tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag","id", tagId));

        List<PostTag> posts = postTagRepository.findAllByTagId(tagId);

        return posts.stream()
                .map(pt -> new PostSummaryDto(pt.getPost()))
                .toList();
    }
}
