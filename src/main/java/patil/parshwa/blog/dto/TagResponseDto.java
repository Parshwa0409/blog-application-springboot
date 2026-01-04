package patil.parshwa.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patil.parshwa.blog.models.Tag;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagResponseDto {

    public TagResponseDto(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }

    private long id;
    private String name;
}
