package patil.parshwa.blog.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patil.parshwa.blog.services.LikeService;

@RestController
@RequestMapping("/api/v1/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    @PostMapping
    public ResponseEntity<Void> likePost(@PathVariable long postId){
        likeService.likePost(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikePost(@PathVariable long postId){
        likeService.deleteLike(postId);
        return ResponseEntity.noContent().build();
    }
}
