package patil.parshwa.blog.dto;

import lombok.Getter;
import lombok.Setter;
import patil.parshwa.blog.models.Post;
import patil.parshwa.blog.models.User;

import java.util.List;

@Getter
@Setter
public class FavoriteResponseDto {
    private User user;

    private List<Post> posts;
}
