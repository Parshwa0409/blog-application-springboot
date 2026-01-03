package patil.parshwa.blog.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "likes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @EmbeddedId
    private LikeId likeId;

    @ManyToOne
    @MapsId(value = "userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId(value = "postId")
    @JoinColumn(name = "post_id")
    private Post post;

    private long createdAt;
}
