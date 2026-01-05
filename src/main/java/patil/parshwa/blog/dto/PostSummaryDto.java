package patil.parshwa.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patil.parshwa.blog.models.Post;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostSummaryDto {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private Long likeCount;
    private List<CommentResponseDto> comments;
    private Long commentCount;
    private Long createdAt;
    private Long updatedAt;

    public PostSummaryDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorName = post.getAuthor().getUsername();
        this.likeCount = (long) post.getLikes().size();
        this.comments = post.getComments().stream().map(CommentResponseDto::new).toList();
        this.commentCount = (long) post.getComments().size();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
