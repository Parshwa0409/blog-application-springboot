package patil.parshwa.blog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import patil.parshwa.blog.dto.LoginRequestDto;
import patil.parshwa.blog.dto.*;
import patil.parshwa.blog.models.RefreshToken;
import patil.parshwa.blog.models.User;
import patil.parshwa.blog.repositories.UserRepository;
import patil.parshwa.blog.security.AuthUtil;

@Service
public class AuthService {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private AuthUtil authUtil;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private RefreshTokenService refreshTokenService;

    public LoginResponseDto login(LoginRequestDto requestDto) {
        // The actual login logic will be implemented here, but delegated to AuthenticationManager and UserDetailsServiceImpl.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(),
                        requestDto.getPassword()
                )
        );

        // If authentication is successful, return a response DTO (token generation can be added here)
        // If authenticated the securityContext will hold the authenticated user details
        User user = (User) authentication.getPrincipal();

        String accessToken = authUtil.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return new LoginResponseDto(accessToken, refreshToken.getToken(), user.getId());
    }

    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto requestDto) {
        return refreshTokenService.findByToken(requestDto.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = authUtil.generateAccessToken(user);
                    return new RefreshTokenResponseDto(accessToken, requestDto.getToken());
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    public SignUpResponseDto signup(SignUpRequestDto requestDto) {
        // create new user in the database
        User user = userRepository.findByUsername(requestDto.getUsername()).orElse(null);

        if(user != null) {
            throw new RuntimeException("Username already exists");
        }

        User newUser = User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();

        User savedUser = userRepository.save(newUser);

        return new SignUpResponseDto(savedUser.getId(), savedUser.getUsername());
    }
}
