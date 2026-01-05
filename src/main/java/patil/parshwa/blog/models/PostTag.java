package patil.parshwa.blog.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostTag {
    @EmbeddedId
    private PostTagId postTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "tagId")
    @JoinColumn(name = "tag_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "postId")
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;
}

