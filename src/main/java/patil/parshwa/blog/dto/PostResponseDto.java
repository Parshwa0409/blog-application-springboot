package patil.parshwa.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patil.parshwa.blog.models.Comment;
import patil.parshwa.blog.models.Post;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorUsername = post.getAuthor().getUsername();
        this.comments = post.getComments().stream().map(CommentResponseDto::new).toList();
        this.likeCount = (long) post.getLikes().size();
        this.commentCount = (long) this.comments.size();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private List<CommentResponseDto> comments;
    private Long likeCount;
    private Long commentCount;
    private Long createdAt;
    private Long updatedAt;
}
