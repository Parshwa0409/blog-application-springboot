package patil.parshwa.blog.services;

import patil.parshwa.blog.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getAllUsers();

    UserResponseDto getUser();
}