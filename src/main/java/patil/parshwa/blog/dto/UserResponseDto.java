package patil.parshwa.blog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patil.parshwa.blog.models.User;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}