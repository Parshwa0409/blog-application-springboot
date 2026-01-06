package patil.parshwa.blog.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import patil.parshwa.blog.dto.PostSummaryDto;
import patil.parshwa.blog.services.FeedService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed")
public class FeedController {
    private final FeedService feedService;

    @GetMapping
    public List<PostSummaryDto> getFeed() {
        return feedService.getFeed();
    }
}
