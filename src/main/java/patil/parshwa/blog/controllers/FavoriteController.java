package patil.parshwa.blog.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patil.parshwa.blog.services.FavoriteService;

@RestController
@RequestMapping("/api/v1/posts/{postId}/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> addFavorite(@PathVariable long postId) {
        favoriteService.addFavorite(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@PathVariable long postId) {
        favoriteService.removeFavorite(postId);
        return ResponseEntity.noContent().build();
    }
}
