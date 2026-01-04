package patil.parshwa.blog.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patil.parshwa.blog.dto.FavoriteRequestDto;
import patil.parshwa.blog.services.FavoriteService;

@RestController
@RequestMapping("/api/v1/users/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> addFavorite(@Valid @RequestBody FavoriteRequestDto favoriteRequestDto) {
        favoriteService.addFavorite(favoriteRequestDto.getPostId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@Valid @RequestBody FavoriteRequestDto favoriteRequestDto) {
        favoriteService.removeFavorite(favoriteRequestDto.getPostId());
        return ResponseEntity.noContent().build();
    }
}
