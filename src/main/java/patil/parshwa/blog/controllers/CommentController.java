package patil.parshwa.blog.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patil.parshwa.blog.dto.CommentRequestDto;
import patil.parshwa.blog.dto.CommentResponseDto;
import patil.parshwa.blog.services.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable long postId, @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return new ResponseEntity<>(commentService.addComment(postId, commentRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable long postId, @PathVariable long commentId) {
        return new ResponseEntity<>(commentService.getComment(postId, commentId), HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable long postId, @PathVariable long commentId, @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return new ResponseEntity<>(commentService.updateComment(postId, commentId, commentRequestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long postId, @PathVariable long commentId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.noContent().build();
    }
}
