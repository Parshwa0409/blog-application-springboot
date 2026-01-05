package patil.parshwa.blog.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PostTagId implements Serializable {
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "post_id")
    private Long postId;
}
