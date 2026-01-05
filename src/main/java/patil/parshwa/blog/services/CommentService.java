package patil.parshwa.blog.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import patil.parshwa.blog.dto.CommentRequestDto;
import patil.parshwa.blog.dto.CommentResponseDto;
import patil.parshwa.blog.error.ForbiddenException;
import patil.parshwa.blog.error.ResourceNotFoundException;
import patil.parshwa.blog.models.Comment;
import patil.parshwa.blog.models.Post;
import patil.parshwa.blog.models.User;
import patil.parshwa.blog.repositories.CommentRepository;
import patil.parshwa.blog.repositories.PostRepository;
import patil.parshwa.blog.security.UserFacade;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserFacade userFacade;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDto addComment(long postId, CommentRequestDto commentRequestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        long now = System.currentTimeMillis();

        Comment comment = Comment.builder()
                .author(userFacade.getCurrentUser())
                .post(post)
                .content(commentRequestDto.getContent())
                .createdAt(now)
                .updatedAt(now)
                .build();

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional(readOnly = true)
    public CommentResponseDto getComment(long postId, long commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(long postId, long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        validateCommentAuthor(comment);

        comment.setContent(commentRequestDto.getContent());
        comment.setUpdatedAt(System.currentTimeMillis());
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public void deleteComment(long postId, long commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        validateCommentAuthor(comment);

        commentRepository.delete(comment);
    }

    private void validateCommentAuthor(Comment comment) {
        long userId = userFacade.getCurrentUser().getId();
        long commentAuthorId = comment.getAuthor().getId();
        if (commentAuthorId != userId) {
            throw new ForbiddenException("You are not authorized to perform this action");
        }
    }
}
