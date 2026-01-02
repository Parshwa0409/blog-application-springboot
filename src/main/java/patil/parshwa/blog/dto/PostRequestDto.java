package patil.parshwa.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {

    @NotBlank(message = "Title is mandatory")
    @Size(max = 64, message = "Title must be at most 64 characters long")
    private String title;

    @NotBlank(message = "Content is mandatory")
    @Size(min = 10, message = "Content must be at least 10 characters long")
    private String content;
}
