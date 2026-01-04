package patil.parshwa.blog.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import patil.parshwa.blog.dto.UserResponseDto;
import patil.parshwa.blog.repositories.UserRepository;
import patil.parshwa.blog.security.UserFacade;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserFacade userFacade;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUser() {
        return new UserResponseDto(userFacade.getCurrentUser());
    }
}