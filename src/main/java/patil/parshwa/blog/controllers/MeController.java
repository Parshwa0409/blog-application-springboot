package patil.parshwa.blog.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import patil.parshwa.blog.dto.PostSummaryDto;
import patil.parshwa.blog.dto.UserResponseDto;
import patil.parshwa.blog.services.FavoriteService;
import patil.parshwa.blog.services.PostService;
import patil.parshwa.blog.services.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/me")
public class MeController {
    private final UserService userService;
    private final PostService postService;
    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<UserResponseDto> getMe() {
        return ResponseEntity.ok(userService.getUser());
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostSummaryDto>> getMyPosts() {
        List<PostSummaryDto> posts = postService.getMyPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<PostSummaryDto>> getMyFavPosts() {
        List<PostSummaryDto> favPosts = favoriteService.getMyFavPosts();
        return ResponseEntity.ok(favPosts);
    }
}
