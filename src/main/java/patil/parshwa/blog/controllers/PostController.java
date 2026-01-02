package patil.parshwa.blog.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import patil.parshwa.blog.dto.PostRequestDto;
import patil.parshwa.blog.dto.PostResponseDto;
import patil.parshwa.blog.services.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto postRequestDto) {
        return new ResponseEntity<>(postService.createPost(postRequestDto), HttpStatus.CREATED);
    }
}
