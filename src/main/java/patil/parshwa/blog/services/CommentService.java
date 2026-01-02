package patil.parshwa.blog.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import patil.parshwa.blog.dto.CommentRequestDto;
import patil.parshwa.blog.dto.CommentResponseDto;
import patil.parshwa.blog.error.ResourceNotFoundException;
import patil.parshwa.blog.models.Comment;
import patil.parshwa.blog.models.Post;
import patil.parshwa.blog.repositories.CommentRepository;
import patil.parshwa.blog.repositories.PostRepository;
import patil.parshwa.blog.security.UserFacade;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserFacade userFacade;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private void assertPostExists(long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
    }

    public CommentResponseDto addComment(long postId, CommentRequestDto commentRequestDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        long now = System.currentTimeMillis();

        Comment comment = Comment
                .builder()
                .author(userFacade.getCurrentUser())
                .post(post)
                .content(commentRequestDto.getContent())
                .createdAt(now)
                .updatedAt(now)
                .build();

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    public CommentResponseDto getComment(long postId, long commentId) {
        assertPostExists(postId);

        Comment comment = commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        return new CommentResponseDto(comment);
    }

    public CommentResponseDto updateComment(long postId, long commentId, CommentRequestDto commentRequestDto) {
        assertPostExists(postId);

        Comment comment = commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));


        comment.setContent(commentRequestDto.getContent());
        comment.setUpdatedAt(System.currentTimeMillis());
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    public void deleteComment(long postId, long commentId) {
        assertPostExists(postId);

        Comment comment = commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        commentRepository.delete(comment);
    }
}
