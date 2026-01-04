package patil.parshwa.blog.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import patil.parshwa.blog.dto.TagRequestDto;
import patil.parshwa.blog.dto.TagResponseDto;
import patil.parshwa.blog.error.ResourceNotFoundException;
import patil.parshwa.blog.models.Tag;
import patil.parshwa.blog.repositories.TagRepository;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public TagResponseDto addTag(TagRequestDto tagRequestDto) {
        Tag tag = Tag.builder()
                .name(tagRequestDto.getName())
                .build();
        return new TagResponseDto(tagRepository.save(tag));
    }

    public TagResponseDto updateTag(TagRequestDto tagRequestDto, long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag","id", tagId));
        tag.setName(tagRequestDto.getName());
        return new TagResponseDto(tagRepository.save(tag));
    }

    public void deleteTag(long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag","id", tagId));
        tagRepository.delete(tag);
    }

    public TagResponseDto getTag(long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag","id", tagId));
        return new TagResponseDto(tag);
    }
}
