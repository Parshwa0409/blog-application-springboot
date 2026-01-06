package patil.parshwa.blog.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patil.parshwa.blog.dto.PostRequestDto;
import patil.parshwa.blog.dto.PostResponseDto;
import patil.parshwa.blog.services.PostService;
import patil.parshwa.blog.services.PostTagService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;
    private final PostTagService postTagService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto postRequestDto) {
        return new ResponseEntity<>(postService.createPost(postRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @Valid @RequestBody PostRequestDto postRequestDto) {
        return ResponseEntity.ok(postService.updatePost(postId, postRequestDto));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/tags/{tagId}")
    public ResponseEntity<Void> addTagToPost(@PathVariable Long postId, @PathVariable Long tagId) {
        postTagService.addTagToPost(postId, tagId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/tags/{tagId}")
    public ResponseEntity<Void> deleteTagFromPost(@PathVariable Long postId, @PathVariable Long tagId) {
        postTagService.deleteTagFromPost(postId, tagId);
        return ResponseEntity.noContent().build();
    }
}
