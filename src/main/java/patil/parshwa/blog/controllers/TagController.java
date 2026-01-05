package patil.parshwa.blog.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patil.parshwa.blog.dto.PostSummaryDto;
import patil.parshwa.blog.dto.TagRequestDto;
import patil.parshwa.blog.dto.TagResponseDto;
import patil.parshwa.blog.models.Tag;
import patil.parshwa.blog.services.PostTagService;
import patil.parshwa.blog.services.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final PostTagService postTagService;

    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getAllTags() {
        // Implementation for fetching all tags goes here
        List<TagResponseDto> tags = tagService.getAllTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<TagResponseDto> getTags(@PathVariable long tagId) {
        // Implementation for fetching tags goes here
        TagResponseDto tagResponseDto = tagService.getTag(tagId);
        return new ResponseEntity<>(tagResponseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagResponseDto> createTag(@RequestBody TagRequestDto tagRequestDto) {
        // Implementation for creating a tag goes here
        TagResponseDto tagResponseDto = tagService.addTag(tagRequestDto);
        return new ResponseEntity<>(tagResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<TagResponseDto> updateTag(@RequestBody TagRequestDto tagRequestDto, @PathVariable long tagId) {
        // Implementation for updating a tag goes here
        TagResponseDto tagResponseDto = tagService.updateTag(tagRequestDto, tagId);
        return new ResponseEntity<>(tagResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable long tagId) {
        // Implementation for deleting a tag goes here
        tagService.deleteTag(tagId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{tagId}/posts")
    public ResponseEntity<List<PostSummaryDto>> getPostsByTag(@PathVariable long tagId) {
        List<PostSummaryDto> posts = postTagService.getPostsByTagId(tagId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
